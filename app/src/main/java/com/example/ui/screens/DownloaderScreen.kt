package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DownloaderRepository
import com.example.model.DownloadItem
import com.example.model.DownloadStatus
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceLightNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TrendingHotRed

@Composable
fun DownloaderScreen(
  downloaderRepository: DownloaderRepository,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val downloads by downloaderRepository.downloads.collectAsState()

  var inputUrl by remember { mutableStateOf("") }
  var selectedQuality by remember { mutableStateOf("1080p HD") }
  val qualities = listOf("1080p HD", "720p Standard", "Audio MP3")

  fun pasteClipboard() {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = clipboard.primaryClip
    if (clip != null && clip.itemCount > 0) {
      val text = clip.getItemAt(0).text?.toString() ?: ""
      if (text.isNotBlank()) {
        inputUrl = text
        Toast.makeText(context, "URL pasted from clipboard", Toast.LENGTH_SHORT).show()
      }
    } else {
      Toast.makeText(context, "Clipboard is empty", Toast.LENGTH_SHORT).show()
    }
  }

  fun handleDownload() {
    val url = inputUrl.trim()
    if (url.isBlank()) {
      Toast.makeText(context, "Please enter or paste a video URL", Toast.LENGTH_SHORT).show()
      return
    }
    if (!downloaderRepository.isValidTikTokUrl(url)) {
      Toast.makeText(context, "Please enter a valid TikTok or video URL", Toast.LENGTH_SHORT).show()
      return
    }
    downloaderRepository.startDownload(sourceUrl = url, quality = selectedQuality)
    inputUrl = ""
    Toast.makeText(context, "Download started!", Toast.LENGTH_SHORT).show()
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Video Downloader",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Text(
          text = "Fast HD & Audio downloads with progress tracking",
          style = MaterialTheme.typography.labelSmall,
          color = NeonCyan
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Input Card with Glassmorphism
    GlassCard(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
          value = inputUrl,
          onValueChange = { inputUrl = it },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("download_url_input"),
          placeholder = {
            Text("Paste TikTok or video link here...", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
          },
          trailingIcon = {
            IconButton(
              onClick = { pasteClipboard() },
              modifier = Modifier.testTag("paste_clipboard_button")
            ) {
              Icon(Icons.Default.ContentPaste, contentDescription = "Paste", tint = NeonCyan)
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedBorderColor = NeonIndigo,
            unfocusedBorderColor = SurfaceCardBorder,
            focusedContainerColor = SurfaceDark,
            unfocusedContainerColor = SurfaceDark
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick sample test chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Quick links:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
          QuickUrlChip(label = "Balen Speech", onClick = {
            inputUrl = "https://www.tiktok.com/@balen.shah/video/731002345129"
          })
          QuickUrlChip(label = "Harka Shramdan", onClick = {
            inputUrl = "https://www.tiktok.com/@harkasampang/video/731098712345"
          })
          QuickUrlChip(label = "Parliament", onClick = {
            inputUrl = "https://www.tiktok.com/@parliament.nepal/video/73145678901"
          })
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quality selection chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          qualities.forEach { q ->
            val isSelected = selectedQuality == q
            FilterChip(
              selected = isSelected,
              onClick = { selectedQuality = q },
              label = {
                Text(
                  text = q,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NeonIndigo,
                selectedLabelColor = Color.White,
                containerColor = SurfaceDark,
                labelColor = TextSecondary
              ),
              border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = isSelected,
                borderColor = if (isSelected) NeonIndigo else SurfaceCardBorder
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Download Action Button
        Button(
          onClick = { handleDownload() },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("start_download_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = NeonIndigo,
            contentColor = Color.White
          )
        ) {
          Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Download Video",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Downloads Section Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Download Queue & History",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )

      if (downloads.isNotEmpty()) {
        Text(
          text = "${downloads.size} items",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Downloads List
    if (downloads.isEmpty()) {
      GlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Download, contentDescription = "Empty", tint = TextMuted, modifier = Modifier.size(40.dp))
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "No Active Downloads",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Paste a TikTok video link above or pick a video from the Trending section.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .testTag("downloads_list"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(downloads, key = { it.id }) { item ->
          DownloadItemRow(
            item = item,
            onCancel = { downloaderRepository.cancelDownload(item.id) },
            onRetry = { downloaderRepository.retryDownload(item.id) },
            onDelete = { downloaderRepository.removeDownload(item.id) }
          )
        }
      }
    }
  }
}

@Composable
private fun QuickUrlChip(label: String, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(SurfaceDark)
      .clickable(onClick = onClick)
      .padding(horizontal = 8.dp, vertical = 3.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = NeonCyan,
      fontSize = 11.sp
    )
  }
}

@Composable
private fun DownloadItemRow(
  item: DownloadItem,
  onCancel: () -> Unit,
  onRetry: () -> Unit,
  onDelete: () -> Unit
) {
  val context = LocalContext.current
  val animatedProgress by animateFloatAsState(targetValue = item.progress, label = "progress")

  GlassCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            maxLines = 1
          )
          Text(
            text = "${item.quality} • ${item.sourceUrl.take(30)}...",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
        }

        // Status Badge
        when (item.status) {
          DownloadStatus.DOWNLOADING -> {
            Text(
              text = item.speedText.ifBlank { "Downloading" },
              style = MaterialTheme.typography.labelSmall,
              color = NeonCyan,
              fontWeight = FontWeight.Bold
            )
          }
          DownloadStatus.COMPLETED -> {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
              Icon(Icons.Default.CheckCircle, contentDescription = "Done", tint = SuccessEmerald, modifier = Modifier.size(14.dp))
              Text("Completed", style = MaterialTheme.typography.labelSmall, color = SuccessEmerald, fontWeight = FontWeight.Bold)
            }
          }
          DownloadStatus.ANALYZING -> {
            Text("Analyzing...", style = MaterialTheme.typography.labelSmall, color = NeonPink)
          }
          DownloadStatus.FAILED -> {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
              Icon(Icons.Default.ErrorOutline, contentDescription = "Failed", tint = TrendingHotRed, modifier = Modifier.size(14.dp))
              Text("Failed", style = MaterialTheme.typography.labelSmall, color = TrendingHotRed)
            }
          }
          DownloadStatus.CANCELLED -> {
            Text("Cancelled", style = MaterialTheme.typography.labelSmall, color = TextMuted)
          }
          else -> {}
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Progress bar
      if (item.status == DownloadStatus.DOWNLOADING || item.status == DownloadStatus.ANALYZING) {
        LinearProgressIndicator(
          progress = { animatedProgress },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = NeonCyan,
          trackColor = SurfaceDark
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Action buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        val sizeFormatted = formatFileSize(item.totalBytes)
        Text(
          text = if (item.status == DownloadStatus.DOWNLOADING) {
            "${(item.progress * 100).toInt()}% of $sizeFormatted"
          } else {
            sizeFormatted
          },
          style = MaterialTheme.typography.labelSmall,
          color = TextSecondary
        )

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          if (item.status == DownloadStatus.DOWNLOADING || item.status == DownloadStatus.ANALYZING) {
            IconButton(onClick = onCancel, modifier = Modifier.size(32.dp)) {
              Icon(Icons.Default.Cancel, contentDescription = "Cancel", tint = TrendingHotRed, modifier = Modifier.size(18.dp))
            }
          }

          if (item.status == DownloadStatus.FAILED || item.status == DownloadStatus.CANCELLED) {
            IconButton(onClick = onRetry, modifier = Modifier.size(32.dp)) {
              Icon(Icons.Default.Refresh, contentDescription = "Retry", tint = NeonCyan, modifier = Modifier.size(18.dp))
            }
          }

          if (item.status == DownloadStatus.COMPLETED) {
            IconButton(
              onClick = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_TEXT, "Downloaded video: ${item.title}\n${item.sourceUrl}")
                }
                context.startActivity(Intent.createChooser(sendIntent, "Share Downloaded Video"))
              },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(Icons.Default.Share, contentDescription = "Share", tint = NeonCyan, modifier = Modifier.size(18.dp))
            }
          }

          IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}

private fun formatFileSize(bytes: Long): String {
  if (bytes <= 0) return "14.2 MB"
  val mb = bytes / (1024f * 1024f)
  return String.format("%.1f MB", mb)
}
