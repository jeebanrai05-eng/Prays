package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.model.VideoReviewInfo
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceLightNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TrendingHotRed

@Composable
fun VideoReviewCard(
  review: VideoReviewInfo,
  selectedQuality: String,
  onQualitySelected: (String) -> Unit,
  onDownloadClick: () -> Unit,
  onWatchPreviewClick: () -> Unit,
  onPreparePostClick: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  GlassCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("video_review_card")
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Header: Video Analyzed Review & Close
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = "Verified",
            tint = NeonCyan,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Video Stream Verified & Ready",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = NeonCyan
          )
        }

        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Media details row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Thumbnail with Duration
        Box(
          modifier = Modifier
            .size(width = 115.dp, height = 80.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceLightNavy)
        ) {
          AsyncImage(
            model = ImageRequest.Builder(context)
              .data(review.thumbnailUrl)
              .crossfade(true)
              .build(),
            contentDescription = review.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
          )

          // Duration Badge
          Text(
            text = review.durationFormatted,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(4.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(Color.Black.copy(alpha = 0.75f))
              .padding(horizontal = 4.dp, vertical = 2.dp)
          )
        }

        // Creator and Caption
        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            if (!review.authorAvatarUrl.isNullOrBlank()) {
              AsyncImage(
                model = ImageRequest.Builder(context).data(review.authorAvatarUrl).build(),
                contentDescription = review.authorName,
                modifier = Modifier
                  .size(20.dp)
                  .clip(CircleShape)
              )
            }
            Text(
              text = review.authorName,
              style = MaterialTheme.typography.labelMedium,
              color = TextPrimary,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = review.authorUsername,
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = review.title,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Verified Live Stats Pill
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF0C101C))
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        StatItem(icon = Icons.Default.Visibility, value = formatStats(review.viewsCount), label = "Views")
        StatItem(icon = Icons.Default.Favorite, value = formatStats(review.likesCount), label = "Likes", iconTint = NeonPink)
        StatItem(icon = Icons.Default.Share, value = formatStats(review.sharesCount), label = "Shares", iconTint = NeonCyan)
        StatItem(icon = Icons.Default.ChatBubble, value = formatStats(review.commentsCount), label = "Comments", iconTint = TextMuted)
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Quality Selector
      Text(
        text = "Select Video Quality & Bitrate:",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        review.availableQualities.forEach { quality ->
          val isSelected = selectedQuality == quality
          FilterChip(
            selected = isSelected,
            onClick = { onQualitySelected(quality) },
            label = {
              Text(
                text = quality,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = if (quality.contains("8K") || quality.contains("4K")) TrendingHotRed else NeonIndigo,
              selectedLabelColor = Color.White,
              containerColor = SurfaceDark,
              labelColor = TextSecondary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = isSelected,
              borderColor = if (isSelected) NeonCyan else SurfaceCardBorder
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Watch Preview button
        OutlinedButton(
          onClick = onWatchPreviewClick,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = "Watch", modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Preview", style = MaterialTheme.typography.labelSmall)
        }

        // Prepare Facebook Post button
        Button(
          onClick = onPreparePostClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
          modifier = Modifier.weight(1.3f)
        ) {
          Icon(Icons.Default.AutoAwesome, contentDescription = "FB Post", modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Prepare Post", style = MaterialTheme.typography.labelSmall)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Primary Download Button
      Button(
        onClick = onDownloadClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = NeonIndigo),
        modifier = Modifier
          .fillMaxWidth()
          .height(46.dp)
          .testTag("review_download_button")
      ) {
        Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Download in $selectedQuality",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
private fun StatItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  value: String,
  label: String,
  iconTint: Color = TextSecondary
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(3.dp)
  ) {
    Icon(icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(12.dp))
    Text(text = value, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 11.sp)
  }
}

private fun formatStats(count: Long): String {
  return when {
    count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000f)
    count >= 1_000 -> String.format("%.1fK", count / 1_000f)
    else -> count.toString()
  }
}
