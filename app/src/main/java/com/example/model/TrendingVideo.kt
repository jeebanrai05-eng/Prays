package com.example.model

data class TrendingVideo(
  val id: String,
  val title: String,
  val nepaliTitle: String,
  val leaderOrTopic: String,
  val creator: String,
  val platform: String = "TikTok",
  val sourceUrl: String,
  val videoStreamUrl: String,
  val thumbnailUrl: String,
  val publishedTime: String,
  val duration: String,
  val viewsCount: Long,
  val likesCount: Long,
  val sharesCount: Long,
  val isViral: Boolean = false,
  val isTrending: Boolean = true,
  val isNew: Boolean = false,
  val category: String,
  val verifiedSummary: String,
  val suggestedHashtags: List<String>,
  val alternativeTitles: List<String> = emptyList(),
  val reelsShortCaption: String = ""
)

enum class TrendingFilter(val label: String, val queryKey: String) {
  ALL("All Nepal Politics", "all"),
  HARKA_SAMPANG("Harka Sampang", "harka"),
  BALEN_SHAH("Balen Shah", "balen"),
  NATIONAL_GOVT("National Government", "govt"),
  POLITICAL_PARTIES("Political Parties", "parties"),
  PARLIAMENT("Parliamentary News", "parliament"),
  SPEECHES("Political Speeches", "speeches"),
  INTERVIEWS("Interviews and Debates", "interviews"),
  BREAKING("Breaking Political News", "breaking")
}

enum class TrendingSort(val label: String) {
  MOST_TRENDING("Most Trending"),
  RECENTLY_PUBLISHED("Recently Published"),
  MOST_ENGAGING("Most Engaging")
}
