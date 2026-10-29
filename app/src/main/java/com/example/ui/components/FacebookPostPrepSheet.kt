package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.PreparedFacebookPost
import com.example.model.TrendingVideo
import com.example.service.NepaliContentGenerator
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceLightNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FacebookPostPrepScreen(
  video: TrendingVideo,
  onBack: () -> Unit,
  onDownloadRequested: () -> Unit,
  onSaveToLibrary: (PreparedFacebookPost) -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler(onBack = onBack)

  val context = LocalContext.current
  val generated = remember(video) { NepaliContentGenerator.generatePostContent(video) }

  var titleText by remember { mutableStateOf(generated.title) }
  var captionText by remember { mutableStateOf(generated.caption) }
  var hashtagsText by remember { mutableStateOf(generated.hashtags) }
  var showAiVariations by remember { mutableStateOf(false) }
  var isSaved by remember { mutableStateOf(false) }

  fun copyToClipboard(label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "$label copied to clipboard!", Toast.LENGTH_SHORT).show()
  }

  fun shareToFacebookApp() {
    val combinedText = buildString {
      append(titleText.trim())
      append("\n\n")
      append(captionText.trim())
      append("\n\n")
      append(hashtagsText.trim())
      if (video.sourceUrl.isNotBlank()) {
        append("\n\nVideo Source: ${video.sourceUrl}")
      }
    }

    // Copy to clipboard first because Facebook doesn't allow auto-filling text via standard intent
    copyToClipboard("Facebook Post Text", combinedText)

    // Launch Facebook share or native Android chooser
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, combinedText)
      `package` = "com.facebook.katana"
    }

    try {
      context.startActivity(shareIntent)
    } catch (e: Exception) {
      // Fallback to standard Android share sheet
      val chooser = Intent.createChooser(
        Intent(Intent.ACTION_SEND).apply {
          type = "text/plain"
          putExtra(Intent.EXTRA_TEXT, combinedText)
        },
        "Share Post to Facebook / Social Media"
      )
      context.startActivity(chooser)
    }
  }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundDark)
      .statusBarsPadding()
      .navigationBarsPadding(),
    containerColor = BackgroundDark,
    topBar = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("back_from_prep_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = TextPrimary
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Prepare Facebook Post",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Ready to publish with authentic Nepali copy",
            style = MaterialTheme.typography.labelSmall,
            color = NeonCyan
          )
        }

        // Save to Library button
        IconButton(
          onClick = {
            val post = PreparedFacebookPost(
              id = UUID.randomUUID().toString(),
              videoId = video.id,
              videoTitle = video.title,
              thumbnailUrl = video.thumbnailUrl,
              nepaliTitle = titleText,
              nepaliCaption = captionText,
              hashtags = hashtagsText,
              sourceUrl = video.sourceUrl
            )
            onSaveToLibrary(post)
            isSaved = true
            Toast.makeText(context, "Saved to Library!", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier.testTag("save_prepared_post_button")
        ) {
          Icon(
            imageVector = if (isSaved) Icons.Default.Check else Icons.Default.Save,
            contentDescription = "Save Post",
            tint = if (isSaved) NeonCyan else TextPrimary
          )
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

      // A. VIDEO PREVIEW & ACTIONS
      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            AsyncImage(
              model = ImageRequest.Builder(LocalContext.current)
                .data(video.thumbnailUrl)
                .crossfade(true)
                .build(),
              contentDescription = video.title,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(width = 110.dp, height = 75.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceLightNavy)
            )

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = video.leaderOrTopic,
                style = MaterialTheme.typography.labelSmall,
                color = NeonCyan,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = video.title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                maxLines = 2,
                fontWeight = FontWeight.Medium
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "By ${video.creator}",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = onDownloadRequested,
              modifier = Modifier
                .weight(1f)
                .testTag("prep_download_video_button"),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Download Video", style = MaterialTheme.typography.labelSmall)
            }

            OutlinedButton(
              onClick = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_TEXT, "${video.title}\n${video.sourceUrl}")
                }
                context.startActivity(Intent.createChooser(sendIntent, "Share Video"))
              },
              modifier = Modifier
                .weight(1f)
                .testTag("prep_share_video_button"),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Share Link", style = MaterialTheme.typography.labelSmall)
            }
          }
        }
      }

      // AI Suggestions Switcher / Variations
      GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = NeonPink, modifier = Modifier.size(18.dp))
              Text(
                text = "AI Nepali Content Engine",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            }

            OutlinedButton(
              onClick = { showAiVariations = !showAiVariations },
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                if (showAiVariations) "Hide Variations" else "Show Variations",
                style = MaterialTheme.typography.labelSmall,
                color = NeonCyan
              )
            }
          }

          AnimatedVisibility(visible = showAiVariations) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = "Tap any suggestion to apply immediately:",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
              )

              generated.alternativeTitles.forEach { altTitle ->
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                      titleText = altTitle
                      Toast.makeText(context, "Title applied!", Toast.LENGTH_SHORT).show()
                    },
                  colors = CardDefaults.cardColors(containerColor = SurfaceLightNavy),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = altTitle,
                      style = MaterialTheme.typography.bodySmall,
                      color = TextPrimary,
                      modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Apply", style = MaterialTheme.typography.labelSmall, color = NeonCyan, fontWeight = FontWeight.Bold)
                  }
                }
              }

              if (generated.reelsCaption.isNotBlank()) {
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                      captionText = generated.reelsCaption
                      Toast.makeText(context, "Reels caption applied!", Toast.LENGTH_SHORT).show()
                    },
                  colors = CardDefaults.cardColors(containerColor = SurfaceLightNavy),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                      text = "Short Reels/TikTok Format:",
                      style = MaterialTheme.typography.labelSmall,
                      color = NeonPink,
                      fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = generated.reelsCaption,
                      style = MaterialTheme.typography.bodySmall,
                      color = TextPrimary
                    )
                  }
                }
              }
            }
          }
        }
      }

      // B. FACEBOOK TITLE SECTION
      PostSectionField(
        sectionTag = "B",
        title = "Facebook Title (Nepali)",
        subtitle = "Catchy & factual headline for higher reach",
        value = titleText,
        onValueChange = { titleText = it },
        onCopy = { copyToClipboard("Facebook Title", titleText) },
        copyButtonTag = "copy_title_button"
      )

      // C. FACEBOOK CAPTION SECTION
      PostSectionField(
        sectionTag = "C",
        title = "Facebook Caption",
        subtitle = "Contextual Nepali narrative describing the political event",
        value = captionText,
        onValueChange = { captionText = it },
        onCopy = { copyToClipboard("Facebook Caption", captionText) },
        isMultiline = true,
        copyButtonTag = "copy_caption_button"
      )

      // D. HASHTAGS SECTION
      PostSectionField(
        sectionTag = "D",
        title = "Political Hashtags",
        subtitle = "Targeted hashtags matching the specific leader & topic",
        value = hashtagsText,
        onValueChange = { hashtagsText = it },
        onCopy = { copyToClipboard("Hashtags", hashtagsText) },
        copyButtonTag = "copy_hashtags_button"
      )

      // E. COPY ALL BUTTON
      OutlinedButton(
        onClick = {
          val combined = "$titleText\n\n$captionText\n\n$hashtagsText"
          copyToClipboard("All Post Content", combined)
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("copy_all_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
      ) {
        Icon(Icons.Default.ContentCopy, contentDescription = "Copy All", modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Copy All (Title + Caption + Hashtags)",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold
        )
      }

      // F. SHARE TO FACEBOOK BUTTON
      Button(
        onClick = { shareToFacebookApp() },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("share_to_facebook_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF1877F2), // Facebook Blue
          contentColor = Color.White
        )
      ) {
        Icon(Icons.Default.Share, contentDescription = "Facebook", modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Share to Facebook",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }

      // Compliance note
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(Icons.Default.Info, contentDescription = "Info", tint = TextMuted, modifier = Modifier.size(16.dp))
        Text(
          text = "Note: Tapping 'Share to Facebook' copies your caption to clipboard and opens Facebook so you can paste and review before publishing. We never ask for your Facebook account password.",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted
        )
      }
    }
  }
}

@Composable
private fun PostSectionField(
  sectionTag: String,
  title: String,
  subtitle: String,
  value: String,
  onValueChange: (String) -> Unit,
  onCopy: () -> Unit,
  isMultiline: Boolean = false,
  copyButtonTag: String
) {
  GlassCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "$sectionTag. $title",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
        }

        OutlinedButton(
          onClick = onCopy,
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.testTag(copyButtonTag)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp), tint = NeonCyan)
          Spacer(modifier = Modifier.width(4.dp))
          Text("Copy", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        minLines = if (isMultiline) 4 else 1,
        maxLines = if (isMultiline) 8 else 3,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary,
          focusedBorderColor = NeonIndigo,
          unfocusedBorderColor = SurfaceCardBorder,
          focusedContainerColor = SurfaceDark,
          unfocusedContainerColor = SurfaceDark
        )
      )
    }
  }
}
