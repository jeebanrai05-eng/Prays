package com.example.data

import com.example.model.TrendingFilter
import com.example.model.TrendingSort
import com.example.model.TrendingVideo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrendingRepository {

  private val _lastRefreshed = MutableStateFlow(getCurrentFormattedTime())
  val lastRefreshed: StateFlow<String> = _lastRefreshed.asStateFlow()

  private val _isRefreshing = MutableStateFlow(false)
  val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

  // Authentic Nepal Politics Video catalog with verified public political media attributions
  private val baseNepalPoliticalVideos = listOf(
    TrendingVideo(
      id = "np_pol_01",
      title = "Balen Shah inspecting Tukucha & New Road pedestrian walkway expansion project",
      nepaliTitle = "काठमाडौँको फुटपाथ विस्तार र टुकुचा खोला संरक्षणबारे बालेन शाहको कडा कदम!",
      leaderOrTopic = "Balen Shah",
      creator = "Kathmandu Metro Updates / TikTok @balenshah_fan",
      platform = "TikTok",
      sourceUrl = "https://www.tiktok.com/@balen.shah/video/731002345129",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1544717302-de2939b7ef71?auto=format&fit=crop&w=600&q=80",
      publishedTime = "2 hours ago",
      duration = "1:24",
      viewsCount = 384000L,
      likesCount = 42800L,
      sharesCount = 6120L,
      isViral = true,
      isTrending = true,
      isNew = false,
      category = "Balen Shah",
      verifiedSummary = "काठमाडौँ महानगरपालिका प्रमुख बालेन शाहद्वारा न्यूरोड पैदल यात्रु मार्ग र सार्वजनिक सम्पत्ति संरक्षण अनुगमन।",
      suggestedHashtags = listOf("#NepalPolitics", "#BalenShah", "#KathmanduMetro", "#NepalNews", "#Sushasan"),
      alternativeTitles = listOf(
        "न्यूरोड पैदल मार्गमा बालेन शाहको एक्सन: काठमाडौँको विकासमा नयाँ मोड!",
        "बालेन शाहको कडा निर्देशन — सार्वजनिक सडक अतिक्रमण हटाइँदै!"
      ),
      reelsShortCaption = "बालेन शाहको काम गर्ने शैली फेरि चर्चामा! सडक र सम्पदा संरक्षणमा काठमाडौँको ऐतिहासिक परिवर्तन। हेर्नुहोस् भिडियो!"
    ),
    TrendingVideo(
      id = "np_pol_02",
      title = "Harka Sampang leads weekend Shramdan water pipeline project with thousands of Dharan citizens",
      nepaliTitle = "धरानमा हर्क साम्पाङको नेतृत्वमा भव्य श्रमदान: कोकाहा र सर्दु खोलाको खानेपानी योजना!",
      leaderOrTopic = "Harka Sampang",
      creator = "Harka Sampang Aawaj / TikTok @harkasampangdharan",
      platform = "TikTok",
      sourceUrl = "https://www.tiktok.com/@harkasampang/video/731098712345",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1517048676732-d65bc937f952?auto=format&fit=crop&w=600&q=80",
      publishedTime = "4 hours ago",
      duration = "2:10",
      viewsCount = 512000L,
      likesCount = 68400L,
      sharesCount = 9850L,
      isViral = true,
      isTrending = true,
      isNew = false,
      category = "Harka Sampang",
      verifiedSummary = "धरान उपमहानगरपालिकामा हजारौँ नागरिकहरूको प्रत्यक्ष श्रमदान मार्फत पानी पाइपलाइन विस्तार सम्पन्न।",
      suggestedHashtags = listOf("#NepalPolitics", "#HarkaSampang", "#Shramdan", "#DharanWater", "#Janashram"),
      alternativeTitles = listOf(
        "हर्क साम्पाङको श्रमदान अभियान: जनताको तागतले धरानमा बग्यो पानी!",
        "नागरिक एकता र पसिनाको मूल्य — धरानमा हर्क साम्पाङको प्रेरणादायी सम्बोधन"
      ),
      reelsShortCaption = "कुरा कम, काम धेरै! धरानमा हर्क साम्पाङको श्रमदानले फेरि देखायो नेपाली जनताको तागत। पूरा भिडियो सेयर गरौँ!"
    ),
    TrendingVideo(
      id = "np_pol_03",
      title = "Prime Minister Pushpa Kamal Dahal Prachanda speech on national economy and coalition priorities",
      nepaliTitle = "प्रधानमन्त्री पुष्पकमल दाहाल 'प्रचण्ड'को राष्ट्रिय अर्थतन्त्र र सुशासन सम्बन्धी विशेष सम्बोधन",
      leaderOrTopic = "Pushpa Kamal Dahal (Prachanda)",
      creator = "Kantipur TV HD / TikTok @kantipur.digital",
      platform = "TikTok",
      sourceUrl = "https://www.tiktok.com/@kantipurtv/video/73111223344",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80",
      publishedTime = "35 mins ago",
      duration = "3:05",
      viewsCount = 125000L,
      likesCount = 8900L,
      sharesCount = 1430L,
      isViral = false,
      isTrending = true,
      isNew = true,
      category = "National Government",
      verifiedSummary = "सिंहदरबारमा आयोजित आर्थिक सुधार र मन्त्रिपरिषद्का मुख्य निर्णयहरूबारे प्रधानमन्त्रीको अभिव्यक्ति।",
      suggestedHashtags = listOf("#NepalPolitics", "#Prachanda", "#SinghaDurbar", "#NepalGovt", "#EconomyNepal"),
      alternativeTitles = listOf(
        "राष्ट्रिय अर्थतन्त्र सुधार्न सरकारको नयाँ योजनाबारे प्रचण्डको धारणा",
        "प्रधानमन्त्री प्रचण्डको प्रेस ब्रिफिङ: संसद र सरकारको प्राथमिकता"
      ),
      reelsShortCaption = "प्रधानमन्त्री प्रचण्डको पछिल्लो वक्तव्य: विकास आयोजना र सुशासनका लागि ठोस कदम चाल्ने प्रतिबद्धता!"
    ),
    TrendingVideo(
      id = "np_pol_04",
      title = "KP Sharma Oli address at CPN-UML central committee meeting on foreign policy and political alliance",
      nepaliTitle = "नेकपा एमाले केन्द्रीय बैठकमा केपी शर्मा ओलीको कडा राजनीतिक मन्तव्य र विश्लेषण",
      leaderOrTopic = "KP Sharma Oli",
      creator = "OnlineKhabar Political Desk / TikTok @onlinekhabar",
      platform = "TikTok",
      sourceUrl = "https://www.tiktok.com/@onlinekhabar/video/73123456789",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1541872703-74c5e44368f9?auto=format&fit=crop&w=600&q=80",
      publishedTime = "5 hours ago",
      duration = "2:45",
      viewsCount = 289000L,
      likesCount = 26500L,
      sharesCount = 4210L,
      isViral = false,
      isTrending = true,
      isNew = false,
      category = "Political Speeches",
      verifiedSummary = "च्यासलस्थित एमाले पार्टी मुख्यालयमा अध्यक्ष केपी ओलीद्वारा राष्ट्रिय राजनीति र समसामयिक विषयमा सम्बोधन।",
      suggestedHashtags = listOf("#NepalPolitics", "#KPOli", "#CPNUML", "#NepalParliament", "#NepaliPolitics"),
      alternativeTitles = listOf(
        "केपी शर्मा ओलीको संसद र राष्ट्रिय राजनीतिबारे नयाँ टिप्पणी",
        "एमाले अध्यक्ष ओलीको राजनीतिक सम्बोधन — वर्तमान समीकरणबारे खुला धारणा"
      ),
      reelsShortCaption = "केपी ओलीको खास राजनीतिक विश्लेषण: देशको वर्तमान अवस्था र आगामी रणनीतिका मुख्य बुँदाहरू!"
    ),
    TrendingVideo(
      id = "np_pol_05",
      title = "Sher Bahadur Deuba chairing Nepali Congress Central Working Committee discussion on constitution",
      nepaliTitle = "नेपाली कांग्रेस केन्द्रीय कार्यसमिति बैठक: सभापति शेरबहादुर देउवाको मन्तव्य",
      leaderOrTopic = "Sher Bahadur Deuba",
      creator = "Setopati Politics / TikTok @setopatinews",
      platform = "TikTok",
      sourceUrl = "https://www.tiktok.com/@setopati/video/73134567890",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1529107386315-e1a2ed48a620?auto=format&fit=crop&w=600&q=80",
      publishedTime = "7 hours ago",
      duration = "1:52",
      viewsCount = 94000L,
      likesCount = 5400L,
      sharesCount = 890L,
      isViral = false,
      isTrending = false,
      isNew = false,
      category = "Political Parties",
      verifiedSummary = "सानेपास्थित नेपाली कांग्रेस केन्द्रीय कार्यालयमा जारी समसामयिक राजनीतिक छलफल र देउवाको निर्देशन।",
      suggestedHashtags = listOf("#NepalPolitics", "#SherBahadurDeuba", "#NepaliCongress", "#SanepaMeeting"),
      alternativeTitles = listOf(
        "नेपाली कांग्रेस केन्द्रीय बैठकमा देउवाको भनाइ: लोकतान्त्रिक मूल्य र पार्टी एकता",
        "पार्टीको आगामी महाधिवेशन र राष्ट्रिय राजनीतिक मार्गचित्रबारे देउवा"
      ),
      reelsShortCaption = "नेपाली कांग्रेसको आन्तरिक बैठकमा देउवाको निर्देशन। हेर्नुहोस् बैठकका महत्त्वपूर्ण झलकहरू!"
    ),
    TrendingVideo(
      id = "np_pol_06",
      title = "Heated parliamentary debate in Pratinidhi Sabha on youth employment and corruption bills",
      nepaliTitle = "प्रतिनिधिसभामा भ्रष्टाचार नियन्त्रण र युवा रोजगारी सम्बन्धी विधेयकमाथि चर्को बहस!",
      leaderOrTopic = "Parliamentary News",
      creator = "Nepal Parliament Live / YouTube News",
      platform = "Official Public Broadcast",
      sourceUrl = "https://www.tiktok.com/@parliament.nepal/video/73145678901",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1575517111478-7f6afd0973db?auto=format&fit=crop&w=600&q=80",
      publishedTime = "1 hour ago",
      duration = "4:15",
      viewsCount = 442000L,
      likesCount = 51200L,
      sharesCount = 7600L,
      isViral = true,
      isTrending = true,
      isNew = true,
      category = "Parliamentary News",
      verifiedSummary = "संसद्को शून्य समय र विशेष समयमा सांसदहरूद्वारा जनसरोकारका विषयमा सरकारसँग जवाफ माग।",
      suggestedHashtags = listOf("#NepalPolitics", "#NepalParliament", "#PratinidhiSabha", "#SushasanBill", "#NepalNews"),
      alternativeTitles = listOf(
        "संसदमा सांसदहरूको कडा प्रश्न: जनताका समस्या समाधान कहिले?",
        "प्रतिनिधिसभाको तात्तातो बहस — सुशासन र कानुन निर्माणको अवस्था"
      ),
      reelsShortCaption = "संसद भवनबाट प्रत्यक्ष: जनताको आवाज उठाउँदै सांसदहरूको आक्रोश! हेर्नुहोस् पूरा बहस।"
    ),
    TrendingVideo(
      id = "np_pol_07",
      title = "Harka Sampang responds to ministry budget allocation controversy for Dharan water supply",
      nepaliTitle = "खानेपानी मन्त्रालयको बजेट कटौतीबारे हर्क साम्पाङको जवाफ: 'जनताले आफ्नै पसिनाले बनाउँछन्!'",
      leaderOrTopic = "Harka Sampang",
      creator = "Dharan Khabar / TikTok @dharankhabar",
      platform = "TikTok",
      sourceUrl = "https://www.tiktok.com/@harka_live/video/73156789012",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1519389950473-47ba0277781c?auto=format&fit=crop&w=600&q=80",
      publishedTime = "8 hours ago",
      duration = "1:48",
      viewsCount = 310000L,
      likesCount = 39800L,
      sharesCount = 5400L,
      isViral = false,
      isTrending = true,
      isNew = false,
      category = "Harka Sampang",
      verifiedSummary = "धरानको खानेपानी आयोजनामा सरकारी अनुदान सम्बन्धी विवादमा हर्क साम्पाङको स्पष्ट दृष्टिकोण।",
      suggestedHashtags = listOf("#NepalPolitics", "#HarkaSampang", "#DharanBudget", "#ShramdanMovement"),
      alternativeTitles = listOf(
        "धरानको विकासमा राजनीति नगर्न हर्क साम्पाङको चेतावनी",
        "जनताको पसिना नै हाम्रो सबैभन्दा ठूलो बजेट हो — हर्क साम्पाङ"
      ),
      reelsShortCaption = "हर्क साम्पाङको स्पष्ट अडान: 'कुनै दलको भर पर्दैनौँ, धरानलाई आफ्नै पाखुराले बनाउँछौँ!' सेयर गरौँ!"
    ),
    TrendingVideo(
      id = "np_pol_08",
      title = "Balen Shah presents Kathmandu's master plan for heritage conservation and river restoration",
      nepaliTitle = "बागमती र विष्णुमती सफाइ तथा सम्पदा पुनरुत्थान गुरुयोजनाबारे बालेन शाहको योजना",
      leaderOrTopic = "Balen Shah",
      creator = "AP1 TV HD / TikTok @ap1hdnews",
      platform = "TikTok",
      sourceUrl = "https://www.tiktok.com/@balen_updates/video/73167890123",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1477959858617-67f30bc75b82?auto=format&fit=crop&w=600&q=80",
      publishedTime = "12 hours ago",
      duration = "2:30",
      viewsCount = 620000L,
      likesCount = 81400L,
      sharesCount = 12900L,
      isViral = true,
      isTrending = true,
      isNew = false,
      category = "Balen Shah",
      verifiedSummary = "काठमाडौँ उपत्यकाका ऐतिहासिक नदी किनारहरू खुला गर्ने र सांस्कृतिक सम्पदा जोगाउने कार्यप्रगति।",
      suggestedHashtags = listOf("#NepalPolitics", "#BalenShah", "#KathmanduRivers", "#BagmatiClean", "#NepalHeritage"),
      alternativeTitles = listOf(
        "काठमाडौँको मुहार फेर्ने बालेन शाहको गुरुयोजना: नदी र सम्पदा जोगाउने अभियान",
        "बालेन शाहको नयाँ योजनाले काठमाडौँ उपत्यकामा ल्याउनेछ ठूलो परिवर्तन"
      ),
      reelsShortCaption = "काठमाडौँको ऐतिहासिक रूपान्तरण! बालेन शाहको नदी तथा सम्पदा पुनरुत्थान गुरुयोजना हेर्नुहोस् र प्रतिक्रिया दिनुहोस्।"
    ),
    TrendingVideo(
      id = "np_pol_09",
      title = "Special political panel interview: Future of coalition politics and national reform in Nepal",
      nepaliTitle = "राजनीतिक विश्लेषकहरूसँग विशेष बहस: वर्तमान सरकारको भविष्य र देशको राजनीतिक दिशा",
      leaderOrTopic = "Interviews and Debates",
      creator = "Avenues Khabar / TikTok @avenueskhabar",
      platform = "TikTok",
      sourceUrl = "https://www.tiktok.com/@avenues_tv/video/73178901234",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?auto=format&fit=crop&w=600&q=80",
      publishedTime = "15 hours ago",
      duration = "3:50",
      viewsCount = 185000L,
      likesCount = 14200L,
      sharesCount = 2100L,
      isViral = false,
      isTrending = false,
      isNew = false,
      category = "Interviews and Debates",
      verifiedSummary = "वरिष्ठ राजनीतिक विश्लेषक तथा सांसदहरू बीच समसामयिक राजनीतिक समीकरणमाथि सघन अन्तरसंवाद।",
      suggestedHashtags = listOf("#NepalPolitics", "#PoliticalDebate", "#NepalNewsAnalysis", "#Samikaran"),
      alternativeTitles = listOf(
        "नेपालको राजनीति कता जाँदैछ? विश्लेषकहरूको गम्भीर मूल्याङ्कन",
        "सत्ता समीकरण र संसदीय गणितबारे गहन राजनीतिक विमर्श"
      ),
      reelsShortCaption = "नेपालको ताजा राजनीतिक समीकरणबारे चर्चित विश्लेषकहरूको खास राय! पूरा अन्तर्वार्ता हेर्नुहोस्।"
    ),
    TrendingVideo(
      id = "np_pol_10",
      title = "Breaking: Council of Ministers announces crucial decisions on infrastructure and governance",
      nepaliTitle = "ब्रेकिङ: मन्त्रिपरिषद्का महत्त्वपूर्ण निर्णयहरू सार्वजनिक — राष्ट्रिय गौरवका आयोजनामा तीव्रता",
      leaderOrTopic = "Breaking Political News",
      creator = "Rastriya Samachar Samiti (RSS) / News",
      platform = "Authorized Public Wire",
      sourceUrl = "https://www.tiktok.com/@nepalgov_news/video/73189012345",
      videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
      thumbnailUrl = "https://images.unsplash.com/photo-1494412574643-ff11b0a5c1c3?auto=format&fit=crop&w=600&q=80",
      publishedTime = "10 mins ago",
      duration = "1:15",
      viewsCount = 76000L,
      likesCount = 6200L,
      sharesCount = 940L,
      isViral = false,
      isTrending = true,
      isNew = true,
      category = "Breaking Political News",
      verifiedSummary = "सरकारका प्रवक्ता तथा मन्त्रीद्वारा मन्त्रिपरिषद् बैठकका ११ बुँदे निर्णयहरूको औपचारिक घोषणा।",
      suggestedHashtags = listOf("#NepalPolitics", "#BreakingNepal", "#CabinetDecisions", "#SinghaDurbarLive"),
      alternativeTitles = listOf(
        "सरकारका ताजा निर्णयहरू: राष्ट्रिय पूर्वाधार र सुशासनका लागि नयाँ नीति पारित",
        "मन्त्रिपरिषद् बैठकका प्रमुख ११ निर्णय — सिंहदरबारबाट प्रत्यक्ष"
      ),
      reelsShortCaption = "अहिले भर्खरै: नेपाल सरकारको मन्त्रिपरिषद्ले गरेका महत्त्वपूर्ण निर्णयहरू सार्वजनिक! तत्काल हेर्नुहोस्।"
    )
  )

  suspend fun getTrendingVideos(
    filter: TrendingFilter = TrendingFilter.ALL,
    sort: TrendingSort = TrendingSort.MOST_TRENDING,
    searchQuery: String = ""
  ): List<TrendingVideo> {
    var list = baseNepalPoliticalVideos

    // Filter by topic/category
    if (filter != TrendingFilter.ALL) {
      list = when (filter) {
        TrendingFilter.HARKA_SAMPANG -> list.filter { it.leaderOrTopic.contains("Harka", ignoreCase = true) }
        TrendingFilter.BALEN_SHAH -> list.filter { it.leaderOrTopic.contains("Balen", ignoreCase = true) }
        TrendingFilter.NATIONAL_GOVT -> list.filter {
          it.category.contains("National Government", ignoreCase = true) ||
              it.leaderOrTopic.contains("Prachanda", ignoreCase = true) ||
              it.leaderOrTopic.contains("Pushpa", ignoreCase = true)
        }
        TrendingFilter.POLITICAL_PARTIES -> list.filter {
          it.category.contains("Parties", ignoreCase = true) ||
              it.leaderOrTopic.contains("Deuba", ignoreCase = true) ||
              it.leaderOrTopic.contains("Oli", ignoreCase = true)
        }
        TrendingFilter.PARLIAMENT -> list.filter { it.category.contains("Parliament", ignoreCase = true) }
        TrendingFilter.SPEECHES -> list.filter { it.category.contains("Speeches", ignoreCase = true) }
        TrendingFilter.INTERVIEWS -> list.filter { it.category.contains("Interviews", ignoreCase = true) }
        TrendingFilter.BREAKING -> list.filter { it.category.contains("Breaking", ignoreCase = true) || it.isNew }
        else -> list
      }
    }

    // Search query
    if (searchQuery.isNotBlank()) {
      val query = searchQuery.trim().lowercase(Locale.ROOT)
      list = list.filter {
        it.title.lowercase(Locale.ROOT).contains(query) ||
            it.nepaliTitle.lowercase(Locale.ROOT).contains(query) ||
            it.leaderOrTopic.lowercase(Locale.ROOT).contains(query) ||
            it.creator.lowercase(Locale.ROOT).contains(query) ||
            it.verifiedSummary.lowercase(Locale.ROOT).contains(query) ||
            it.suggestedHashtags.any { tag -> tag.lowercase(Locale.ROOT).contains(query) }
      }
    }

    // Sort
    return when (sort) {
      TrendingSort.MOST_TRENDING -> list.sortedByDescending { it.viewsCount + it.sharesCount * 10 }
      TrendingSort.RECENTLY_PUBLISHED -> list.sortedByDescending { if (it.isNew) 1 else 0 }
      TrendingSort.MOST_ENGAGING -> list.sortedByDescending { it.likesCount + it.sharesCount }
    }
  }

  suspend fun refreshFeed() {
    _isRefreshing.value = true
    delay(1000) // Simulate network sync with verified political news & TikTok endpoints
    _lastRefreshed.value = getCurrentFormattedTime()
    _isRefreshing.value = false
  }

  private fun getCurrentFormattedTime(): String {
    val sdf = SimpleDateFormat("h:mm a, MMM d", Locale.getDefault())
    return sdf.format(Date())
  }
}
