package com.example.service

import com.example.model.TrendingVideo

data class GeneratedPostContent(
  val title: String,
  val caption: String,
  val hashtags: String,
  val alternativeTitles: List<String>,
  val reelsCaption: String
)

object NepaliContentGenerator {

  fun generatePostContent(video: TrendingVideo): GeneratedPostContent {
    val topic = video.leaderOrTopic
    val leaderTag = cleanHashtag(topic)

    // Accurate context-based authentic Nepali generation
    val (title, caption, altTitles, reelsText) = when {
      topic.contains("Balen", ignoreCase = true) -> {
        val t = "काठमाडौँ महानगरको विकास र सुशासनबारे बालेन शाहको सम्बोधन"
        val c = "काठमाडौँ महानगरपालिका प्रमुख बालेन शाहद्वारा सार्वजनिक सडक, नदी किनारा र सम्पदा संरक्षण अभियान अन्तर्गत भइरहेका कार्यहरूबारे जानकारी। नगरवासीको हकहित र दीर्घकालीन विकासका लागि यो कदम महत्त्वपूर्ण मानिएको छ। पूरा भिडियो हेरी आफ्नो विचार कमेन्ट गर्नुहोस्।"
        val alts = listOf(
          "काठमाडौँको ऐतिहासिक रूपान्तरण: बालेन शाहको नयाँ योजना र प्रगति!",
          "फुटपाथ र सम्पदा संरक्षणमा बालेन शाहको निरन्तर पहल — विशेष भिडियो"
        )
        val r = "काठमाडौँको मुहार फेर्ने बालेन शाहको नयाँ पहल! पूरा भिडियो हेर्नुहोस् र प्रतिक्रिया दिनुहोस्।"
        FourTuple(t, c, alts, r)
      }
      topic.contains("Harka", ignoreCase = true) -> {
        val t = "धरानमा हर्क साम्पाङको नेतृत्वमा श्रमदान: नागरिक एकताको नयाँ उदाहरण!"
        val c = "धरान उपमहानगरपालिकामा हजारौँ नागरिकहरूको प्रत्यक्ष सहभागितामा खानेपानी तथा पूर्वाधार निर्माणका लागि श्रमदान अभियान जारी छ। आफ्नै पसिना र सामूहिक प्रयासबाट गाउँ-सहर निर्माण गर्ने यो उदाहरणीय अभियानबारे विशेष रिपोर्ट। हेर्नुहोस् र सेयर गरौँ!"
        val alts = listOf(
          "हर्क साम्पाङको श्रमदान अभियान: जनताको तागतले धरानमा बग्यो पानी!",
          "कुरा कम, काम धेरै! धरानमा हर्क साम्पाङको जनश्रमदान अभियान तीव्र"
        )
        val r = "धरानमा श्रमदानको अभूतपूर्व लहर! जनताको पसिनाले लेखिँदैछ विकासको नयाँ इतिहास।"
        FourTuple(t, c, alts, r)
      }
      topic.contains("Prachanda", ignoreCase = true) || topic.contains("Pushpa", ignoreCase = true) -> {
        val t = "राष्ट्रिय राजनीति र अर्थतन्त्र सुधारबारे प्रधानमन्त्री प्रचण्डको अभिव्यक्ति"
        val c = "प्रधानमन्त्री पुष्पकमल दाहाल 'प्रचण्ड'द्वारा सरकारका प्राथमिकता, मन्त्रिपरिषद्का निर्णयहरू तथा सुशासन अभिवृद्धिबारे सार्वजनिक मन्तव्य। देशको आर्थिक स्थिति र आगामी योजनाबारे प्रधानमन्त्रीको आधिकारिक धारणा।"
        val alts = listOf(
          "प्रधानमन्त्री प्रचण्डको पछिल्लो वक्तव्य: विकास आयोजना र सुशासनका प्राथमिकता",
          "मन्त्रिपरिषद् निर्णय र राष्ट्रिय राजनीतिका समसामयिक विषयमा प्रधानमन्त्री"
        )
        val r = "प्रधानमन्त्री प्रचण्डको विशेष सम्बोधन: राष्ट्रिय विकास र गठबन्धनका मुख्य निर्णयहरू।"
        FourTuple(t, c, alts, r)
      }
      topic.contains("Oli", ignoreCase = true) -> {
        val t = "नेकपा एमाले अध्यक्ष केपी शर्मा ओलीको राजनीतिक विश्लेषण र मन्तव्य"
        val c = "एमाले पार्टी मुख्यालयमा आयोजित बैठकमा अध्यक्ष केपी शर्मा ओलीद्वारा समसामयिक राष्ट्रिय राजनीति, संसदीय समीकरण र आगामी कार्यदिशाबारे धारणा सार्वजनिक। हेर्नुहोस् मुख्य अंशहरू।"
        val alts = listOf(
          "केपी शर्मा ओलीको कडा राजनीतिक मन्तव्य — देशको वर्तमान अवस्थाबारे टिप्पणी",
          "संसद र सरकारको कामकारबाहीबारे एमाले अध्यक्ष ओलीको आधिकारिक धारणा"
        )
        val r = "केपी ओलीको खास राजनीतिक विश्लेषण: देशको वर्तमान राजनीतिक दिशा कता?"
        FourTuple(t, c, alts, r)
      }
      topic.contains("Deuba", ignoreCase = true) -> {
        val t = "नेपाली कांग्रेस केन्द्रीय बैठकमा शेरबहादुर देउवाको निर्देशन"
        val c = "नेपाली कांग्रेसका सभापति शेरबहादुर देउवाद्वारा पार्टीको आन्तरिक रणनीति, लोकतान्त्रिक मूल्य र राष्ट्रिय राजनीतिका विषयमा धारणा व्यक्त। पार्टी संगठन र समसामयिक राजनीतिबारे सम्बोधन।"
        val alts = listOf(
          "नेपाली कांग्रेस केन्द्रीय कार्यसमितिमा देउवाको भनाइ: पार्टी एकता र सुशासन",
          "समसामयिक राजनीतिक मार्गचित्रबारे सभापति देउवाको निर्देशन"
        )
        val r = "नेपाली कांग्रेस बैठकबाट सभापति देउवाको सम्बोधन। हेर्नुहोस् विशेष झलक।"
        FourTuple(t, c, alts, r)
      }
      topic.contains("Parliament", ignoreCase = true) -> {
        val t = "प्रतिनिधिसभामा जनसरोकार र सुशासन विधेयकमाथि तात्तातो बहस!"
        val c = "संसद्को शून्य र विशेष समयमा माननीय सांसदहरूद्वारा भ्रष्टाचार नियन्त्रण, युवा रोजगारी र विकास आयोजनाका ढिलाइबारे सरकारसँग सशक्त प्रश्न। जनप्रतिनिधिहरूको आवाज सुन्नुहोस्।"
        val alts = listOf(
          "संसदमा सांसदहरूको कडा खबरदारी: जनताका समस्या समाधान कहिले?",
          "प्रतिनिधिसभा बैठकका मुख्य झलक — कानुन निर्माण र जनसरोकार"
        )
        val r = "संसद भवनबाट प्रत्यक्ष बहस: जनताको आवाज उठाउँदै सांसदहरू! हेर्नुहोस् पूरा भिडियो।"
        FourTuple(t, c, alts, r)
      }
      else -> {
        val t = if (video.nepaliTitle.isNotBlank()) video.nepaliTitle else video.title
        val c = "${video.verifiedSummary} यस भिडियोबारे आफ्नो धारणा राख्नुहोस् र नागरिक बहसमा सहभागी हुनुहोस्।"
        val alts = listOf(
          "नेपालको ताजा राजनीतिक विकासक्रम: हेर्नुहोस् महत्त्वपूर्ण भिडियो",
          "समसामयिक राजनीतिक घटनाक्रम र जनप्रतिक्रिया — विशेष रिपोर्ट"
        )
        val r = "नेपालको ताजा राजनीतिक अपडेट! तुरुन्त हेर्नुहोस् र प्रतिक्रिया दिनुहोस्।"
        FourTuple(t, c, alts, r)
      }
    }

    // Build focused, relevant hashtags
    val baseTags = mutableListOf("#NepalPolitics", "#NepalNews", "#NepaliPolitics")
    if (leaderTag.isNotBlank() && leaderTag != "All") {
      baseTags.add("#$leaderTag")
    }
    if (topic.contains("Harka", ignoreCase = true)) {
      baseTags.add("#Shramdan")
      baseTags.add("#Dharan")
    }
    if (topic.contains("Balen", ignoreCase = true)) {
      baseTags.add("#KathmanduMetro")
      baseTags.add("#Sushasan")
    }
    if (topic.contains("Parliament", ignoreCase = true)) {
      baseTags.add("#PratinidhiSabha")
      baseTags.add("#NepalParliament")
    }

    val finalHashtags = baseTags.distinct().joinToString(" ")

    return GeneratedPostContent(
      title = title,
      caption = caption,
      hashtags = finalHashtags,
      alternativeTitles = altTitles,
      reelsCaption = reelsText
    )
  }

  private fun cleanHashtag(name: String): String {
    return name.replace(Regex("[^a-zA-Z0-9]"), "")
  }

  private data class FourTuple(
    val title: String,
    val caption: String,
    val altTitles: List<String>,
    val reelsText: String
  )
}
