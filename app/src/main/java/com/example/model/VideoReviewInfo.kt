package com.example.model

data class VideoReviewInfo(
  val id: String,
  val originalUrl: String,
  val title: String,
  val authorName: String,
  val authorUsername: String,
  val authorAvatarUrl: String? = null,
  val thumbnailUrl: String,
  val durationSeconds: Int,
  val durationFormatted: String,
  val viewsCount: Long,
  val likesCount: Long,
  val sharesCount: Long,
  val commentsCount: Long = 0L,
  val streamUrlNoWatermark: String,
  val streamUrlHd: String,
  val audioUrl: String? = null,
  val availableQualities: List<String> = listOf(
    "8K HDR 120fps",
    "4K Ultra HD",
    "1080p Full HD",
    "720p Standard",
    "Audio MP3 (320kbps)"
  )
)
