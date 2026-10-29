package com.example

import com.example.data.TrendingRepository
import com.example.model.TrendingFilter
import com.example.model.TrendingSort
import com.example.model.TrendingVideo
import com.example.service.NepaliContentGenerator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testNepaliContentGenerator_BalenShah() {
    val sampleVideo = TrendingVideo(
      id = "test_01",
      title = "Balen Shah inspecting road construction",
      nepaliTitle = "काठमाडौँको फुटपाथ विस्तारबारे बालेन शाहको सम्बोधन",
      leaderOrTopic = "Balen Shah",
      creator = "Metro News",
      sourceUrl = "https://www.tiktok.com/@balen/video/123",
      videoStreamUrl = "https://example.com/video.mp4",
      thumbnailUrl = "https://example.com/thumb.jpg",
      publishedTime = "1h ago",
      duration = "1:00",
      viewsCount = 100000L,
      likesCount = 5000L,
      sharesCount = 500L,
      category = "Balen Shah",
      verifiedSummary = "काठमाडौँ सडक विस्तार सम्बन्धी जानकारी।",
      suggestedHashtags = listOf("#NepalPolitics", "#BalenShah")
    )

    val generated = NepaliContentGenerator.generatePostContent(sampleVideo)
    assertTrue(generated.hashtags.contains("#BalenShah"))
    assertTrue(generated.hashtags.contains("#NepalPolitics"))
    assertTrue(generated.title.isNotEmpty())
    assertTrue(generated.caption.isNotEmpty())
  }

  @Test
  fun testNepaliContentGenerator_HarkaSampang() {
    val sampleVideo = TrendingVideo(
      id = "test_02",
      title = "Harka Sampang Dharan Shramdan water pipeline",
      nepaliTitle = "धरानमा हर्क साम्पाङको नेतृत्वमा भव्य श्रमदान",
      leaderOrTopic = "Harka Sampang",
      creator = "Dharan News",
      sourceUrl = "https://www.tiktok.com/@harka/video/456",
      videoStreamUrl = "https://example.com/video.mp4",
      thumbnailUrl = "https://example.com/thumb.jpg",
      publishedTime = "2h ago",
      duration = "2:00",
      viewsCount = 200000L,
      likesCount = 15000L,
      sharesCount = 1200L,
      category = "Harka Sampang",
      verifiedSummary = "धरान खानेपानी आयोजनामा जनश्रमदान।",
      suggestedHashtags = listOf("#HarkaSampang", "#Shramdan")
    )

    val generated = NepaliContentGenerator.generatePostContent(sampleVideo)
    assertTrue(generated.hashtags.contains("#HarkaSampang"))
    assertTrue(generated.hashtags.contains("#Shramdan"))
  }

  @Test
  fun testTrendingRepository_FilteringAndSorting() = runBlocking {
    val repo = TrendingRepository()

    // Test Balen filter
    val balenVideos = repo.getTrendingVideos(filter = TrendingFilter.BALEN_SHAH)
    assertTrue(balenVideos.isNotEmpty())
    assertTrue(balenVideos.all { it.leaderOrTopic.contains("Balen", ignoreCase = true) })

    // Test Harka filter
    val harkaVideos = repo.getTrendingVideos(filter = TrendingFilter.HARKA_SAMPANG)
    assertTrue(harkaVideos.isNotEmpty())
    assertTrue(harkaVideos.all { it.leaderOrTopic.contains("Harka", ignoreCase = true) })

    // Test Sorting
    val trendingSorted = repo.getTrendingVideos(sort = TrendingSort.MOST_TRENDING)
    assertTrue(trendingSorted.size >= 2)
  }

  @Test
  fun testAppUpdateInfo_State() {
    val update = com.example.model.AppUpdateInfo(
      latestVersion = "v1.1.0",
      currentVersion = "v1.0.0",
      releaseNotes = "New features",
      apkDownloadUrl = "https://example.com/app.apk",
      apkSizeFormatted = "18.4 MB",
      publishedAt = "2026-10-29",
      htmlUrl = "https://github.com/test/repo",
      isUpdateAvailable = true
    )
    assertTrue(update.isUpdateAvailable)
    assertEquals("v1.1.0", update.latestVersion)
  }
}
