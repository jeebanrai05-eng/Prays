package com.example.model

data class AppSettings(
  val language: String = "ne", // "ne" for Nepali, "en" for English
  val downloadQuality: String = "1080p HD",
  val storageLocation: String = "App Internal Storage",
  val wifiOnly: Boolean = false,
  val autoRefreshTrending: Boolean = true,
  val refreshIntervalMinutes: Int = 30
)
