package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.UpdateManager
import com.example.model.AppUpdateInfo
import com.example.model.UpdateDownloadState
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceLightNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TrendingHotRed
import kotlinx.coroutines.launch

@Composable
fun UpdateDialog(
  updateInfo: AppUpdateInfo,
  downloadState: UpdateDownloadState,
  downloadProgress: Float,
  errorMessage: String?,
  onStartDownload: () -> Unit,
  onInstall: () -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = SurfaceDark,
      tonalElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("update_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.SystemUpdate, contentDescription = "Update", tint = NeonCyan, modifier = Modifier.size(24.dp))
            Column {
              Text(
                text = "New Update Available!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "${updateInfo.latestVersion} (Current: ${updateInfo.currentVersion})",
                style = MaterialTheme.typography.labelSmall,
                color = NeonCyan
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Release Info Banner
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceCard)
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "APK Size: ${updateInfo.apkSizeFormatted}", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
            Text(text = "Released: ${updateInfo.publishedAt}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "What's New in this Release:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Changelog Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceLightNavy)
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
        ) {
          Text(
            text = updateInfo.releaseNotes,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Download progress bar
        if (downloadState == UpdateDownloadState.DOWNLOADING) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Downloading APK...", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
              Text("${(downloadProgress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = NeonCyan, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
              progress = { downloadProgress },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = NeonCyan,
              trackColor = SurfaceLightNavy
            )
          }
          Spacer(modifier = Modifier.height(14.dp))
        }

        // Error message if any
        if (errorMessage != null) {
          Text(
            text = errorMessage,
            style = MaterialTheme.typography.labelSmall,
            color = TrendingHotRed,
            modifier = Modifier.padding(bottom = 8.dp)
          )
        }

        // Actions
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          when (downloadState) {
            UpdateDownloadState.READY_TO_INSTALL -> {
              Button(
                onClick = onInstall,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("install_update_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonIndigo)
              ) {
                Icon(Icons.Default.SystemUpdate, contentDescription = "Install", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Install Update Now", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
              }
            }
            UpdateDownloadState.DOWNLOADING -> {
              Button(
                onClick = {},
                enabled = false,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp),
                shape = RoundedCornerShape(10.dp)
              ) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Downloading APK...", style = MaterialTheme.typography.labelLarge)
              }
            }
            else -> {
              Button(
                onClick = onStartDownload,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("download_update_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonIndigo)
              ) {
                Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Download & Install Update", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
              }
            }
          }

          OutlinedButton(
            onClick = {
              try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateInfo.htmlUrl))
                context.startActivity(intent)
              } catch (_: Exception) {}
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.OpenInBrowser, contentDescription = "GitHub", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("View Release on GitHub", style = MaterialTheme.typography.labelMedium)
          }
        }
      }
    }
  }
}
