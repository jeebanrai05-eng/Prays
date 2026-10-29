package com.example.model

data class PreparedFacebookPost(
  val id: String,
  val videoId: String,
  val videoTitle: String,
  val thumbnailUrl: String? = null,
  val nepaliTitle: String,
  val nepaliCaption: String,
  val hashtags: String,
  val createdAt: Long = System.currentTimeMillis(),
  val sourceUrl: String = ""
)
