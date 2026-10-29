package com.example.model

data class AppUpdateInfo(
  val latestVersion: String,
  val currentVersion: String,
  val releaseNotes: String,
  val apkDownloadUrl: String,
  val apkSizeFormatted: String,
  val publishedAt: String,
  val htmlUrl: String,
  val isUpdateAvailable: Boolean
)

enum class UpdateDownloadState {
  IDLE,
  DOWNLOADING,
  READY_TO_INSTALL,
  ERROR
}
