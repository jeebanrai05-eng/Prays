package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.TrendingVideo
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun VideoPlayerDialog(
  video: TrendingVideo,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var isPlaying by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceDark,
      tonalElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("video_player_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = video.leaderOrTopic,
            style = MaterialTheme.typography.titleSmall,
            color = NeonCyan,
            fontWeight = FontWeight.Bold
          )

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Video Viewport / Thumbnail Preview
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9.5f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black),
          contentAlignment = Alignment.Center
        ) {
          AsyncImage(
            model = ImageRequest.Builder(context)
              .data(video.thumbnailUrl)
              .crossfade(true)
              .build(),
            contentDescription = video.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
          )

          // Dark overlay
          Box(
            modifier = Modifier
              .matchParentSize()
              .background(Color.Black.copy(alpha = if (isPlaying) 0.15f else 0.45f))
          )

          // Play Button Icon
          IconButton(
            onClick = { isPlaying = !isPlaying },
            modifier = Modifier
              .size(60.dp)
              .clip(CircleShape)
              .background(NeonIndigo.copy(alpha = 0.85f))
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = "Play/Pause",
              tint = Color.White,
              modifier = Modifier.size(36.dp)
            )
          }

          if (isPlaying) {
            Text(
              text = "▶ Playing Political Video Broadcast...",
              color = NeonCyan,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = video.nepaliTitle,
          style = MaterialTheme.typography.titleSmall,
          color = TextPrimary,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = video.verifiedSummary,
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.sourceUrl))
                context.startActivity(intent)
              } catch (_: Exception) {}
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.OpenInBrowser, contentDescription = "Source", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Open Source", style = MaterialTheme.typography.labelMedium)
          }

          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = NeonIndigo),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text("Done", style = MaterialTheme.typography.labelMedium)
          }
        }
      }
    }
  }
}
