package com.example.model

enum class DownloadStatus {
  IDLE,
  ANALYZING,
  DOWNLOADING,
  COMPLETED,
  FAILED,
  CANCELLED
}

data class DownloadItem(
  val id: String,
  val title: String,
  val sourceUrl: String,
  val localFilePath: String? = null,
  val totalBytes: Long = 0L,
  val downloadedBytes: Long = 0L,
  val status: DownloadStatus = DownloadStatus.IDLE,
  val progress: Float = 0f,
  val speedText: String = "",
  val timestamp: Long = System.currentTimeMillis(),
  val thumbnailUrl: String? = null,
  val quality: String = "1080p HD",
  val errorMessage: String? = null
)
