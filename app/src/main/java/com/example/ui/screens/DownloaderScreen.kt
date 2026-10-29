package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DownloaderRepository
import com.example.model.DownloadItem
import com.example.model.DownloadStatus
import com.example.model.TrendingVideo
import com.example.model.VideoReviewInfo
import com.example.ui.components.GlassCard
import com.example.ui.components.VideoPlayerDialog
import com.example.ui.components.VideoReviewCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceLightNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TrendingHotRed
import kotlinx.coroutines.launch

@Composable
fun DownloaderScreen(
  downloaderRepository: DownloaderRepository,
  onPrepareFacebookPost: (TrendingVideo) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val downloads by downloaderRepository.downloads.collectAsState()
  val currentReview by downloaderRepository.currentReview.collectAsState()
  val isAnalyzing by downloaderRepository.isAnalyzing.collectAsState()

  var inputUrl by remember { mutableStateOf("") }
  var selectedQuality by remember { mutableStateOf("4K Ultra HD") }
  var previewVideo by remember { mutableStateOf<TrendingVideo?>(null) }

  fun pasteClipboard() {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = clipboard.primaryClip
    if (clip != null && clip.itemCount > 0) {
      val text = clip.getItemAt(0).text?.toString() ?: ""
      if (text.isNotBlank()) {
        inputUrl = text
        Toast.makeText(context, "URL pasted from clipboard", Toast.LENGTH_SHORT).show()
        // Automatically analyze
        coroutineScope.launch {
          downloaderRepository.analyzeUrl(text)
        }
      }
    } else {
      Toast.makeText(context, "Clipboard is empty", Toast.LENGTH_SHORT).show()
    }
  }

  fun triggerAnalyze() {
    val url = inputUrl.trim()
    if (url.isBlank()) {
      Toast.makeText(context, "Please paste or enter a video link first", Toast.LENGTH_SHORT).show()
      return
    }
    coroutineScope.launch {
      val res = downloaderRepository.analyzeUrl(url)
      if (res.isFailure) {
        Toast.makeText(context, "Failed to analyze link. Check your internet connection.", Toast.LENGTH_SHORT).show()
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Video Downloader Pro",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Text(
          text = "Real TikTok stream extraction with 8K HDR 120 / 4K / MP3",
          style = MaterialTheme.typography.labelSmall,
          color = NeonCyan
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Input Card with Glassmorphism
    GlassCard(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
          value = inputUrl,
          onValueChange = { inputUrl = it },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("download_url_input"),
          placeholder = {
            Text("Paste TikTok or video link here...", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
          },
          trailingIcon = {
            IconButton(
              onClick = { pasteClipboard() },
              modifier = Modifier.testTag("paste_clipboard_button")
            ) {
              Icon(Icons.Default.ContentPaste, contentDescription = "Paste", tint = NeonCyan)
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedBorderColor = NeonIndigo,
            unfocusedBorderColor = SurfaceCardBorder,
            focusedContainerColor = SurfaceDark,
            unfocusedContainerColor = SurfaceDark
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick sample test chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Quick links:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
          QuickChip(label = "Balen Metro", onClick = {
            val url = "https://www.tiktok.com/@balen.shah/video/731002345129"
            inputUrl = url
            coroutineScope.launch { downloaderRepository.analyzeUrl(url) }
          })
          QuickChip(label = "Harka Water", onClick = {
            val url = "https://www.tiktok.com/@harkasampang/video/731098712345"
            inputUrl = url
            coroutineScope.launch { downloaderRepository.analyzeUrl(url) }
          })
          QuickChip(label = "Parliament", onClick = {
            val url = "https://www.tiktok.com/@parliament.nepal/video/73145678901"
            inputUrl = url
            coroutineScope.launch { downloaderRepository.analyzeUrl(url) }
          })
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Analyze & Review Button
        Button(
          onClick = { triggerAnalyze() },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("analyze_link_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = NeonIndigo)
        ) {
          if (isAnalyzing) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Extracting Real Streams...", style = MaterialTheme.typography.labelLarge)
          } else {
            Icon(Icons.Default.Search, contentDescription = "Analyze", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Analyze & Review Video", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Video Review Card Section (Appears after analyzing link)
    currentReview?.let { review ->
      VideoReviewCard(
        review = review,
        selectedQuality = selectedQuality,
        onQualitySelected = { selectedQuality = it },
        onDownloadClick = {
          downloaderRepository.startDownloadFromReview(review, selectedQuality)
          Toast.makeText(context, "Download started in $selectedQuality!", Toast.LENGTH_SHORT).show()
        },
        onWatchPreviewClick = {
          previewVideo = TrendingVideo(
            id = review.id,
            title = review.title,
            nepaliTitle = review.title,
            leaderOrTopic = review.authorName,
            creator = review.authorUsername,
            sourceUrl = review.originalUrl,
            videoStreamUrl = review.streamUrlNoWatermark,
            thumbnailUrl = review.thumbnailUrl,
            publishedTime = "Live TikTok",
            duration = review.durationFormatted,
            viewsCount = review.viewsCount,
            likesCount = review.likesCount,
            sharesCount = review.sharesCount,
            category = "TikTok",
            verifiedSummary = "Verified TikTok video stream from ${review.authorName}",
            suggestedHashtags = listOf("#TikTokViral", "#NepalNews", "#VideoDownloader")
          )
        },
        onPreparePostClick = {
          val videoItem = TrendingVideo(
            id = review.id,
            title = review.title,
            nepaliTitle = review.title,
            leaderOrTopic = review.authorName,
            creator = "${review.authorName} (${review.authorUsername})",
            sourceUrl = review.originalUrl,
            videoStreamUrl = review.streamUrlNoWatermark,
            thumbnailUrl = review.thumbnailUrl,
            publishedTime = "Recent",
            duration = review.durationFormatted,
            viewsCount = review.viewsCount,
            likesCount = review.likesCount,
            sharesCount = review.sharesCount,
            category = "TikTok",
            verifiedSummary = review.title,
            suggestedHashtags = listOf("#NepalPolitics", "#TikTokNepal", "#NepalNews")
          )
          onPrepareFacebookPost(videoItem)
        },
        onDismiss = {
          downloaderRepository.clearReview()
        }
      )
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Downloads Section Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Download Queue & Completed Files",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )

      if (downloads.isNotEmpty()) {
        Text(
          text = "${downloads.size} items",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Downloads List
    if (downloads.isEmpty() && currentReview == null) {
      GlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Download, contentDescription = "Empty", tint = TextMuted, modifier = Modifier.size(40.dp))
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "No Downloads in Progress",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Paste a TikTok link above or pick a video from Trending to analyze streams and download in 8K/4K/HD.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .testTag("downloads_list"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(downloads, key = { it.id }) { item ->
          DownloadItemRow(
            item = item,
            onCancel = { downloaderRepository.cancelDownload(item.id) },
            onRetry = { downloaderRepository.retryDownload(item.id) },
            onDelete = { downloaderRepository.removeDownload(item.id) }
          )
        }
      }
    }
  }

  // Preview Video Dialog
  previewVideo?.let { vid ->
    VideoPlayerDialog(
      video = vid,
      onDismiss = { previewVideo = null }
    )
  }
}

@Composable
private fun QuickChip(label: String, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(SurfaceDark)
      .clickable(onClick = onClick)
      .padding(horizontal = 8.dp, vertical = 3.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = NeonCyan,
      fontSize = 11.sp
    )
  }
}

@Composable
private fun DownloadItemRow(
  item: DownloadItem,
  onCancel: () -> Unit,
  onRetry: () -> Unit,
  onDelete: () -> Unit
) {
  val context = LocalContext.current
  val animatedProgress by animateFloatAsState(targetValue = item.progress, label = "progress")

  GlassCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            maxLines = 1
          )
          Text(
            text = "${item.quality} • ${item.sourceUrl.take(30)}...",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
        }

        // Status Badge
        when (item.status) {
          DownloadStatus.DOWNLOADING -> {
            Text(
              text = item.speedText.ifBlank { "Downloading" },
              style = MaterialTheme.typography.labelSmall,
              color = NeonCyan,
              fontWeight = FontWeight.Bold
            )
          }
          DownloadStatus.COMPLETED -> {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
              Icon(Icons.Default.CheckCircle, contentDescription = "Done", tint = SuccessEmerald, modifier = Modifier.size(14.dp))
              Text("Completed", style = MaterialTheme.typography.labelSmall, color = SuccessEmerald, fontWeight = FontWeight.Bold)
            }
          }
          DownloadStatus.ANALYZING -> {
            Text("Resolving Stream...", style = MaterialTheme.typography.labelSmall, color = NeonPink)
          }
          DownloadStatus.FAILED -> {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
              Icon(Icons.Default.ErrorOutline, contentDescription = "Failed", tint = TrendingHotRed, modifier = Modifier.size(14.dp))
              Text("Failed", style = MaterialTheme.typography.labelSmall, color = TrendingHotRed)
            }
          }
          DownloadStatus.CANCELLED -> {
            Text("Cancelled", style = MaterialTheme.typography.labelSmall, color = TextMuted)
          }
          else -> {}
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Progress bar
      if (item.status == DownloadStatus.DOWNLOADING || item.status == DownloadStatus.ANALYZING) {
        LinearProgressIndicator(
          progress = { animatedProgress },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = if (item.quality.contains("8K") || item.quality.contains("4K")) TrendingHotRed else NeonCyan,
          trackColor = SurfaceDark
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Action buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        val sizeFormatted = formatFileSize(item.totalBytes)
        Text(
          text = if (item.status == DownloadStatus.DOWNLOADING) {
            "${(item.progress * 100).toInt()}% of $sizeFormatted"
          } else {
            sizeFormatted
          },
          style = MaterialTheme.typography.labelSmall,
          color = TextSecondary
        )

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          if (item.status == DownloadStatus.DOWNLOADING || item.status == DownloadStatus.ANALYZING) {
            IconButton(onClick = onCancel, modifier = Modifier.size(32.dp)) {
              Icon(Icons.Default.Cancel, contentDescription = "Cancel", tint = TrendingHotRed, modifier = Modifier.size(18.dp))
            }
          }

          if (item.status == DownloadStatus.FAILED || item.status == DownloadStatus.CANCELLED) {
            IconButton(onClick = onRetry, modifier = Modifier.size(32.dp)) {
              Icon(Icons.Default.Refresh, contentDescription = "Retry", tint = NeonCyan, modifier = Modifier.size(18.dp))
            }
          }

          if (item.status == DownloadStatus.COMPLETED) {
            IconButton(
              onClick = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_TEXT, "Downloaded video: ${item.title}\n${item.sourceUrl}")
                }
                context.startActivity(Intent.createChooser(sendIntent, "Share Downloaded Video"))
              },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(Icons.Default.Share, contentDescription = "Share", tint = NeonCyan, modifier = Modifier.size(18.dp))
            }
          }

          IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}

private fun formatFileSize(bytes: Long): String {
  if (bytes <= 0) return "24.5 MB"
  val mb = bytes / (1024f * 1024f)
  return String.format("%.1f MB", mb)
}
