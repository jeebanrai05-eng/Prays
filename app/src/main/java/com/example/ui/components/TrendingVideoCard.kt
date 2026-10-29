package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.TrendingVideo
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SurfaceLightNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TrendingHotRed
import com.example.ui.theme.ViralAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrendingVideoCard(
  video: TrendingVideo,
  isBookmarked: Boolean,
  onWatchClick: () -> Unit,
  onDownloadClick: () -> Unit,
  onPrepareFacebookPost: () -> Unit,
  onBookmarkToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  GlassCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("trending_video_card_${video.id}")
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {

      // Thumbnail with Duration & Badges overlay
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9.5f)
          .clip(RoundedCornerShape(12.dp))
          .background(SurfaceLightNavy)
      ) {
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(video.thumbnailUrl)
            .crossfade(true)
            .build(),
          contentDescription = video.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.matchParentSize()
        )

        // Gradient scrim at bottom
        Box(
          modifier = Modifier
            .matchParentSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.2f),
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.85f)
                )
              )
            )
        )

        // Top Badges Row (Trending / New / Potential Viral & Bookmark button)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (video.isViral) {
              StatusBadge(text = "VIRAL 🔥", backgroundColor = TrendingHotRed)
            } else if (video.isTrending) {
              StatusBadge(text = "TRENDING 📈", backgroundColor = NeonIndigo)
            }
            if (video.isNew) {
              StatusBadge(text = "NEW ⚡", backgroundColor = NeonCyan, textColor = Color.Black)
            }
          }

          // Bookmark icon button
          IconButton(
            onClick = onBookmarkToggle,
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.55f))
          ) {
            Icon(
              imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "Bookmark",
              tint = if (isBookmarked) NeonCyan else Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        // Bottom duration & platform badge on thumbnail
        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = video.platform,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.85f),
            fontWeight = FontWeight.Medium,
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(Color.Black.copy(alpha = 0.6f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          )

          Text(
            text = video.duration,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(Color.Black.copy(alpha = 0.7f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Political Person Tag / Category Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(NeonPurple.copy(alpha = 0.18f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = video.leaderOrTopic,
            color = NeonCyan,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
          )
        }

        Text(
          text = video.publishedTime,
          color = TextMuted,
          style = MaterialTheme.typography.labelSmall
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Nepali Title (Bold & Prominent)
      Text(
        text = video.nepaliTitle,
        style = MaterialTheme.typography.titleMedium,
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      // Verified summary / Context
      Text(
        text = video.verifiedSummary,
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Original Source and Creator
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text(
          text = "Source: ",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted
        )
        Text(
          text = video.creator,
          style = MaterialTheme.typography.labelSmall,
          color = TextSecondary,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Verified Engagement Statistics
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF0C101C))
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        EngagementStat(icon = Icons.Default.Visibility, value = formatCount(video.viewsCount), label = "Views")
        EngagementStat(icon = Icons.Default.Favorite, value = formatCount(video.likesCount), label = "Likes", iconTint = NeonPink)
        EngagementStat(icon = Icons.Default.Share, value = formatCount(video.sharesCount), label = "Shares", iconTint = NeonCyan)
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Actions: Watch Video, Download, Prepare Facebook Post
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Watch Video button
          OutlinedButton(
            onClick = onWatchClick,
            modifier = Modifier
              .weight(1f)
              .testTag("watch_video_button_${video.id}"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Watch", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Watch Video", style = MaterialTheme.typography.labelLarge)
          }

          // Download button
          FilledTonalButton(
            onClick = onDownloadClick,
            modifier = Modifier
              .weight(1f)
              .testTag("download_video_button_${video.id}"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
              containerColor = SurfaceLightNavy,
              contentColor = NeonCyan
            )
          ) {
            Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Download", style = MaterialTheme.typography.labelLarge)
          }
        }

        // Prepare Facebook Post button (Prominent Gradient/Highlight)
        ElevatedButton(
          onClick = onPrepareFacebookPost,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("prepare_facebook_post_button_${video.id}"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF1877F2), // Facebook Blue / Electric Blue
            contentColor = Color.White
          )
        ) {
          Icon(Icons.Default.AutoAwesome, contentDescription = "Post Prep", modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Prepare Facebook Post",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
fun StatusBadge(text: String, backgroundColor: Color, textColor: Color = Color.White) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(backgroundColor)
      .padding(horizontal = 8.dp, vertical = 3.dp)
  ) {
    Text(
      text = text,
      color = textColor,
      fontSize = 10.sp,
      fontWeight = FontWeight.ExtraBold
    )
  }
}

@Composable
private fun EngagementStat(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  value: String,
  label: String,
  iconTint: Color = TextSecondary
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Icon(icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(14.dp))
    Text(text = value, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
    Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
  }
}

private fun formatCount(count: Long): String {
  return when {
    count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000f)
    count >= 1_000 -> String.format("%.1fK", count / 1_000f)
    else -> count.toString()
  }
}
