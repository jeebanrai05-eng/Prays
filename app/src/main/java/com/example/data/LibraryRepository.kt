package com.example.data

import com.example.model.PreparedFacebookPost
import com.example.model.TrendingVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class LibraryRepository {

  private val _savedPosts = MutableStateFlow<List<PreparedFacebookPost>>(
    listOf(
      PreparedFacebookPost(
        id = "post_sample_01",
        videoId = "np_pol_01",
        videoTitle = "काठमाडौँको फुटपाथ विस्तार र टुकुचा खोला संरक्षणबारे बालेन शाहको कडा कदम!",
        thumbnailUrl = "https://images.unsplash.com/photo-1544717302-de2939b7ef71?auto=format&fit=crop&w=600&q=80",
        nepaliTitle = "काठमाडौँको विकास र सुशासनबारे बालेन शाहको कडा सम्बोधन!",
        nepaliCaption = "काठमाडौँ महानगरपालिका प्रमुख बालेन शाहद्वारा सार्वजनिक सडक र सम्पदा अतिक्रमण हटाउने अभियान तीव्र बनाइएको छ। पैदल यात्रु अधिकार र आधुनिक काठमाडौँ निर्माणमा यो कदम प्रशंसनीय छ। प्रतिक्रिया दिनुहोस् र सेयर गरौँ!",
        hashtags = "#NepalPolitics #BalenShah #KathmanduMetro #NepalNews #NepaliPolitics #Sushasan",
        createdAt = System.currentTimeMillis() - 3600000L * 2,
        sourceUrl = "https://www.tiktok.com/@balen.shah/video/731002345129"
      ),
      PreparedFacebookPost(
        id = "post_sample_02",
        videoId = "np_pol_02",
        videoTitle = "धरानमा हर्क साम्पाङको नेतृत्वमा भव्य श्रमदान: खानेपानी योजना!",
        thumbnailUrl = "https://images.unsplash.com/photo-1517048676732-d65bc937f952?auto=format&fit=crop&w=600&q=80",
        nepaliTitle = "हर्क साम्पाङको श्रमदान अभियान: जनताको तागतले धरानमा बग्यो पानी!",
        nepaliCaption = "धरान उपमहानगरपालिकामा हजारौँ नागरिकहरूको प्रत्यक्ष सहभागितामा खानेपानी पाइपलाइन विस्तार कार्य सम्पन्न भएको छ। भाषणभन्दा कर्म ठूलो भन्ने उदाहरण फेरि देखिएको छ!",
        hashtags = "#NepalPolitics #HarkaSampang #Shramdan #DharanWater #Janashram",
        createdAt = System.currentTimeMillis() - 3600000L * 5,
        sourceUrl = "https://www.tiktok.com/@harkasampang/video/731098712345"
      )
    )
  )
  val savedPosts: StateFlow<List<PreparedFacebookPost>> = _savedPosts.asStateFlow()

  private val _savedTrendingVideos = MutableStateFlow<List<TrendingVideo>>(emptyList())
  val savedTrendingVideos: StateFlow<List<TrendingVideo>> = _savedTrendingVideos.asStateFlow()

  fun savePreparedPost(post: PreparedFacebookPost) {
    val existingIndex = _savedPosts.value.indexOfFirst { it.videoId == post.videoId }
    if (existingIndex >= 0) {
      _savedPosts.value = _savedPosts.value.mapIndexed { index, item ->
        if (index == existingIndex) post else item
      }
    } else {
      _savedPosts.value = listOf(post) + _savedPosts.value
    }
  }

  fun deletePreparedPost(id: String) {
    _savedPosts.value = _savedPosts.value.filterNot { it.id == id }
  }

  fun toggleBookmark(video: TrendingVideo) {
    val isSaved = _savedTrendingVideos.value.any { it.id == video.id }
    if (isSaved) {
      _savedTrendingVideos.value = _savedTrendingVideos.value.filterNot { it.id == video.id }
    } else {
      _savedTrendingVideos.value = listOf(video) + _savedTrendingVideos.value
    }
  }

  fun isBookmarked(videoId: String): Boolean {
    return _savedTrendingVideos.value.any { it.id == videoId }
  }
}
