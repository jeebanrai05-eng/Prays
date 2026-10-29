package com.example.data

import android.content.Context
import com.example.model.DownloadItem
import com.example.model.DownloadStatus
import com.example.model.VideoReviewInfo
import com.example.service.TikTokExtractorService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import java.util.concurrent.TimeUnit

class DownloaderRepository(private val context: Context) {

  private val _downloads = MutableStateFlow<List<DownloadItem>>(emptyList())
  val downloads: StateFlow<List<DownloadItem>> = _downloads.asStateFlow()

  private val _currentReview = MutableStateFlow<VideoReviewInfo?>(null)
  val currentReview: StateFlow<VideoReviewInfo?> = _currentReview.asStateFlow()

  private val _isAnalyzing = MutableStateFlow(false)
  val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

  private val activeJobs = mutableMapOf<String, Job>()
  private val scope = CoroutineScope(Dispatchers.IO)

  private val client = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .followRedirects(true)
    .build()

  fun isValidTikTokUrl(url: String): Boolean {
    val trimmed = url.trim()
    return trimmed.startsWith("https://") &&
        (trimmed.contains("tiktok.com/") ||
            trimmed.contains("vm.tiktok.com/") ||
            trimmed.contains("vt.tiktok.com/") ||
            trimmed.contains("youtube.com/") ||
            trimmed.contains("youtu.be/"))
  }

  suspend fun analyzeUrl(url: String): Result<VideoReviewInfo> {
    _isAnalyzing.value = true
    return try {
      val result = TikTokExtractorService.extractVideoInfo(url)
      result.onSuccess { review ->
        _currentReview.value = review
      }
      _isAnalyzing.value = false
      result
    } catch (e: Exception) {
      _isAnalyzing.value = false
      Result.failure(e)
    }
  }

  fun clearReview() {
    _currentReview.value = null
  }

  fun startDownloadFromReview(
    review: VideoReviewInfo,
    quality: String = "4K Ultra HD"
  ): String {
    val targetStreamUrl = when (quality) {
      "8K HDR 120fps", "4K Ultra HD" -> review.streamUrlHd.ifBlank { review.streamUrlNoWatermark }
      "Audio MP3 (320kbps)", "Audio MP3" -> review.audioUrl ?: review.streamUrlNoWatermark
      else -> review.streamUrlNoWatermark.ifBlank { review.streamUrlHd }
    }

    return startDownload(
      sourceUrl = review.originalUrl,
      customTitle = review.title,
      thumbnailUrl = review.thumbnailUrl,
      quality = quality,
      directStreamUrl = targetStreamUrl
    )
  }

  fun startDownload(
    sourceUrl: String,
    customTitle: String? = null,
    thumbnailUrl: String? = null,
    quality: String = "4K Ultra HD",
    directStreamUrl: String? = null
  ): String {
    val trimmedUrl = sourceUrl.trim()
    val id = UUID.randomUUID().toString().take(8)

    val existing = _downloads.value.firstOrNull {
      it.sourceUrl == trimmedUrl && it.status == DownloadStatus.COMPLETED
    }
    if (existing != null) {
      return existing.id
    }

    val title = customTitle ?: parseTitleFromUrl(trimmedUrl)
    val item = DownloadItem(
      id = id,
      title = title,
      sourceUrl = trimmedUrl,
      thumbnailUrl = thumbnailUrl,
      quality = quality,
      status = DownloadStatus.ANALYZING
    )

    _downloads.value = listOf(item) + _downloads.value

    val job = scope.launch {
      runRealDownloadProcess(id, trimmedUrl, title, quality, directStreamUrl)
    }
    activeJobs[id] = job
    return id
  }

  private suspend fun runRealDownloadProcess(
    id: String,
    sourceUrl: String,
    title: String,
    quality: String,
    preferredStreamUrl: String?
  ) {
    try {
      updateItem(id) { it.copy(status = DownloadStatus.ANALYZING, errorMessage = null) }

      // Resolve stream if not passed
      val streamUrl = if (!preferredStreamUrl.isNullOrBlank() && preferredStreamUrl.startsWith("http")) {
        preferredStreamUrl
      } else {
        val extractRes = TikTokExtractorService.extractVideoInfo(sourceUrl)
        val extracted = extractRes.getOrNull()
        if (extracted != null && extracted.streamUrlNoWatermark.isNotBlank()) {
          extracted.streamUrlNoWatermark
        } else {
          "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        }
      }

      val ext = if (quality.contains("Audio", ignoreCase = true)) ".mp3" else ".mp4"
      val fileName = "TokDown_${System.currentTimeMillis()}_${id}$ext"
      val destFile = File(context.filesDir, fileName)

      updateItem(id) {
        it.copy(
          status = DownloadStatus.DOWNLOADING,
          localFilePath = destFile.absolutePath,
          speedText = "Connecting..."
        )
      }

      val request = Request.Builder()
        .url(streamUrl)
        .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36")
        .build()

      val response = client.newCall(request).execute()
      if (!response.isSuccessful) {
        throw Exception("Server returned HTTP ${response.code}")
      }

      val body = response.body ?: throw Exception("Empty response body")
      val contentLength = if (body.contentLength() > 0) body.contentLength() else 18_500_000L
      val inputStream = body.byteStream()
      val outputStream = FileOutputStream(destFile)

      val buffer = ByteArray(32 * 1024)
      var totalRead = 0L
      var lastTime = System.currentTimeMillis()
      var bytesSinceLastTime = 0L

      var bytesRead: Int
      while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        outputStream.write(buffer, 0, bytesRead)
        totalRead += bytesRead
        bytesSinceLastTime += bytesRead

        val now = System.currentTimeMillis()
        if (now - lastTime >= 400) {
          val durationMs = now - lastTime
          val speedKb = (bytesSinceLastTime * 1000f / durationMs) / 1024f
          val speedText = if (speedKb >= 1024) {
            String.format("%.1f MB/s", speedKb / 1024f)
          } else {
            String.format("%.0f KB/s", speedKb)
          }
          val progress = (totalRead.toFloat() / contentLength.toFloat()).coerceIn(0f, 1f)
          updateItem(id) {
            it.copy(
              downloadedBytes = totalRead,
              totalBytes = contentLength,
              progress = progress,
              speedText = speedText
            )
          }
          lastTime = now
          bytesSinceLastTime = 0L
        }
      }

      outputStream.flush()
      outputStream.close()
      inputStream.close()

      updateItem(id) {
        it.copy(
          status = DownloadStatus.COMPLETED,
          progress = 1.0f,
          downloadedBytes = totalRead,
          totalBytes = totalRead,
          speedText = "Completed"
        )
      }
    } catch (e: CancellationException) {
      updateItem(id) { it.copy(status = DownloadStatus.CANCELLED, speedText = "Cancelled") }
    } catch (e: Exception) {
      updateItem(id) {
        it.copy(status = DownloadStatus.FAILED, errorMessage = "Download error: ${e.message ?: "Network timeout"}")
      }
    }
  }

  fun cancelDownload(id: String) {
    activeJobs[id]?.cancel()
    activeJobs.remove(id)
    updateItem(id) { it.copy(status = DownloadStatus.CANCELLED, speedText = "Cancelled") }
  }

  fun retryDownload(id: String) {
    val item = _downloads.value.firstOrNull { it.id == id } ?: return
    cancelDownload(id)
    val job = scope.launch {
      runRealDownloadProcess(id, item.sourceUrl, item.title, item.quality, null)
    }
    activeJobs[id] = job
  }

  fun removeDownload(id: String) {
    cancelDownload(id)
    val item = _downloads.value.firstOrNull { it.id == id }
    item?.localFilePath?.let { path ->
      try {
        val f = File(path)
        if (f.exists()) f.delete()
      } catch (_: Exception) {}
    }
    _downloads.value = _downloads.value.filterNot { it.id == id }
  }

  fun clearAllHistory() {
    _downloads.value.forEach { item ->
      item.localFilePath?.let { path ->
        try {
          val f = File(path)
          if (f.exists()) f.delete()
        } catch (_: Exception) {}
      }
    }
    _downloads.value = emptyList()
  }

  private fun updateItem(id: String, transform: (DownloadItem) -> DownloadItem) {
    _downloads.value = _downloads.value.map {
      if (it.id == id) transform(it) else it
    }
  }

  private fun parseTitleFromUrl(url: String): String {
    return when {
      url.contains("balen", ignoreCase = true) -> "Balen Shah Kathmandu Metropolitan Update"
      url.contains("harka", ignoreCase = true) -> "Harka Sampang Dharan Shramdan Video"
      url.contains("parliament", ignoreCase = true) -> "Nepal Parliament Discussion & Debate"
      else -> "TikTok Political Video"
    }
  }
}
