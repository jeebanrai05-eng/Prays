package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.DownloaderRepository
import com.example.data.SettingsRepository
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TrendingHotRed

import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.CircularProgressIndicator
import com.example.data.UpdateManager
import com.example.model.AppUpdateInfo
import com.example.ui.components.UpdateDialog
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun SettingsScreen(
  settingsRepository: SettingsRepository,
  downloaderRepository: DownloaderRepository,
  updateManager: UpdateManager,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val settings by settingsRepository.settings.collectAsState()
  val updateInfo by updateManager.updateInfo.collectAsState()
  val isCheckingUpdate by updateManager.isChecking.collectAsState()
  val downloadState by updateManager.downloadState.collectAsState()
  val downloadProgress by updateManager.downloadProgress.collectAsState()
  val updateErrorMessage by updateManager.errorMessage.collectAsState()

  var showUpdateDialog by remember { mutableStateOf(false) }

  var showClearHistoryDialog by remember { mutableStateOf(false) }
  var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
  var showAboutDialog by remember { mutableStateOf(false) }
  var showQualityDialog by remember { mutableStateOf(false) }
  var showLanguageDialog by remember { mutableStateOf(false) }
  var showStorageDialog by remember { mutableStateOf(false) }

  var cacheSizeText by remember { mutableStateOf("18.4 MB") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp)
      .padding(bottom = 90.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Header
    Column {
      Text(
        text = "Settings & Preferences",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )
      Text(
        text = "Configure app language, download limits, storage & cache",
        style = MaterialTheme.typography.labelSmall,
        color = NeonCyan
      )
    }

    Spacer(modifier = Modifier.height(6.dp))

    // 1. General & Language
    SettingsSection(title = "Language & Display") {
      SettingsItemRow(
        icon = Icons.Default.Language,
        title = "Preferred Language",
        subtitle = if (settings.language == "ne") "नेपाली (Nepali)" else "English",
        onClick = { showLanguageDialog = true }
      )
    }

    // 2. Download Options
    SettingsSection(title = "Download Preferences") {
      SettingsItemRow(
        icon = Icons.Default.HighQuality,
        title = "Default Video Quality",
        subtitle = settings.downloadQuality,
        onClick = { showQualityDialog = true }
      )

      SettingsItemRow(
        icon = Icons.Default.Folder,
        title = "Download Storage Location",
        subtitle = settings.storageLocation,
        onClick = { showStorageDialog = true }
      )

      SettingsSwitchRow(
        icon = Icons.Default.Wifi,
        title = "Download on Wi-Fi Only",
        subtitle = "Prevent mobile data consumption during large video downloads",
        checked = settings.wifiOnly,
        onCheckedChange = { settingsRepository.updateWifiOnly(it) }
      )
    }

    // 3. Trending Feed Options
    SettingsSection(title = "Trending Feed & Sync") {
      SettingsSwitchRow(
        icon = Icons.Default.Refresh,
        title = "Automatic Trending Refresh",
        subtitle = "Periodically sync Nepal political viral updates in background",
        checked = settings.autoRefreshTrending,
        onCheckedChange = { settingsRepository.updateAutoRefresh(it) }
      )
    }

    // 4. Cache & History Management
    SettingsSection(title = "Storage & Cache Management") {
      SettingsItemRow(
        icon = Icons.Default.CleaningServices,
        title = "Clear Cached Media",
        subtitle = "App Cache Size: $cacheSizeText",
        onClick = {
          try {
            context.cacheDir.deleteRecursively()
            cacheSizeText = "0 KB"
            Toast.makeText(context, "Cache successfully cleared!", Toast.LENGTH_SHORT).show()
          } catch (_: Exception) {}
        }
      )

      SettingsItemRow(
        icon = Icons.Default.DeleteForever,
        title = "Clear Download History",
        subtitle = "Remove all completed download records and local files",
        iconTint = TrendingHotRed,
        onClick = { showClearHistoryDialog = true }
      )
    }

    // 5. Self Update & GitHub Releases
    SettingsSection(title = "Self Update & GitHub Releases") {
      SettingsItemRow(
        icon = Icons.Default.SystemUpdate,
        title = if (isCheckingUpdate) "Checking for updates..." else "Check for Updates",
        subtitle = if (isCheckingUpdate) "Connecting to GitHub Releases API..." else "Current: v1.0 • Tap to check latest APK release",
        onClick = {
          coroutineScope.launch {
            val result = updateManager.checkForUpdates()
            if (result != null) {
              showUpdateDialog = true
            } else {
              Toast.makeText(context, "You are using the latest version (v1.0)", Toast.LENGTH_SHORT).show()
            }
          }
        }
      )

      SettingsItemRow(
        icon = Icons.Default.Code,
        title = "GitHub Repository",
        subtitle = "https://github.com/jeebannraiii/TokDownloader",
        onClick = {
          try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jeebannraiii/TokDownloader/releases"))
            context.startActivity(intent)
          } catch (_: Exception) {}
        }
      )
    }

    // 6. Legal & About
    SettingsSection(title = "Legal & About") {
      SettingsItemRow(
        icon = Icons.Default.Policy,
        title = "Privacy Policy & Platform Terms",
        subtitle = "User data policy, DRM compliance & fair use declaration",
        onClick = { showPrivacyPolicyDialog = true }
      )

      SettingsItemRow(
        icon = Icons.Default.Info,
        title = "About TokDownloader",
        subtitle = "Version 1.0 • Exclusive Nepal Politics Hub & AI Post Prep",
        onClick = { showAboutDialog = true }
      )
    }
  }

  // Self Update Dialog
  if (showUpdateDialog && updateInfo != null) {
    UpdateDialog(
      updateInfo = updateInfo!!,
      downloadState = downloadState,
      downloadProgress = downloadProgress,
      errorMessage = updateErrorMessage,
      onStartDownload = {
        coroutineScope.launch {
          updateManager.downloadUpdate(updateInfo!!.apkDownloadUrl)
        }
      },
      onInstall = {
        val success = updateManager.installUpdate()
        if (success) {
          showUpdateDialog = false
        }
      },
      onDismiss = { showUpdateDialog = false }
    )
  }

  // Language Dialog
  if (showLanguageDialog) {
    OptionSelectDialog(
      title = "Select Language",
      options = listOf("ne" to "नेपाली (Nepali)", "en" to "English"),
      selectedKey = settings.language,
      onSelect = {
        settingsRepository.updateLanguage(it)
        showLanguageDialog = false
      },
      onDismiss = { showLanguageDialog = false }
    )
  }

  // Quality Dialog
  if (showQualityDialog) {
    OptionSelectDialog(
      title = "Download Video Quality",
      options = listOf("1080p HD" to "1080p Full HD", "720p Standard" to "720p Standard", "Audio MP3" to "Audio MP3 (Soundtrack)"),
      selectedKey = settings.downloadQuality,
      onSelect = {
        settingsRepository.updateQuality(it)
        showQualityDialog = false
      },
      onDismiss = { showQualityDialog = false }
    )
  }

  // Storage Location Dialog
  if (showStorageDialog) {
    OptionSelectDialog(
      title = "Download Storage Location",
      options = listOf("App Internal Storage" to "App Internal Storage (Private & Safe)", "Public Downloads" to "Public Downloads (Device Movies)"),
      selectedKey = settings.storageLocation,
      onSelect = {
        settingsRepository.updateStorageLocation(it)
        showStorageDialog = false
      },
      onDismiss = { showStorageDialog = false }
    )
  }

  // Clear History Confirmation
  if (showClearHistoryDialog) {
    AlertDialog(
      onDismissRequest = { showClearHistoryDialog = false },
      title = { Text("Clear All Download History?", fontWeight = FontWeight.Bold, color = TextPrimary) },
      text = { Text("This will permanently remove all downloaded files and records. This action cannot be undone.", color = TextSecondary) },
      confirmButton = {
        Button(
          onClick = {
            downloaderRepository.clearAllHistory()
            showClearHistoryDialog = false
            Toast.makeText(context, "Download history cleared!", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = TrendingHotRed)
        ) {
          Text("Clear All", color = Color.White)
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showClearHistoryDialog = false }) {
          Text("Cancel", color = TextPrimary)
        }
      },
      containerColor = SurfaceDark
    )
  }

  // Privacy Policy Dialog
  if (showPrivacyPolicyDialog) {
    AlertDialog(
      onDismissRequest = { showPrivacyPolicyDialog = false },
      title = { Text("Privacy Policy & Terms", fontWeight = FontWeight.Bold, color = TextPrimary) },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          Text(
            text = "TokDownloader respects creator copyright, platform terms of service, and user privacy.\n\n" +
                "• All Nepal Political content is curated strictly from public news broadcasts, verified civic releases, and authorized social media posts.\n" +
                "• We never store or transmit your social account credentials. No Facebook or TikTok password is ever requested.\n" +
                "• Videos and generated post captions are saved locally on your device storage.\n" +
                "• Users are responsible for adhering to platform sharing guidelines and copyright licenses when reposting content.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }
      },
      confirmButton = {
        Button(onClick = { showPrivacyPolicyDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = NeonIndigo)) {
          Text("Close", color = Color.White)
        }
      },
      containerColor = SurfaceDark
    )
  }

  // About Dialog
  if (showAboutDialog) {
    AlertDialog(
      onDismissRequest = { showAboutDialog = false },
      title = { Text("About TokDownloader", fontWeight = FontWeight.Bold, color = TextPrimary) },
      text = {
        Column {
          Text(
            text = "TokDownloader v1.0\n\n" +
                "A modern video downloader with a dedicated Nepal Politics Trending Hub.\n\n" +
                "• Downloader: Fast TikTok & video link downloading with progress tracking.\n" +
                "• Trending: Exclusive, verified Nepal political updates (Harka Sampang, Balen Shah, Parliament, national leaders).\n" +
                "• Facebook Post Prep: AI-assisted authentic Nepali titles, captions, and hashtags.\n" +
                "• Library: Organized storage of downloaded videos and prepared social posts.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }
      },
      confirmButton = {
        Button(onClick = { showAboutDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = NeonIndigo)) {
          Text("Got It", color = Color.White)
        }
      },
      containerColor = SurfaceDark
    )
  }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = title,
      style = MaterialTheme.typography.labelMedium,
      color = NeonCyan,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
    )
    GlassCard(modifier = Modifier.fillMaxWidth()) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        content()
      }
    }
  }
}

@Composable
private fun SettingsItemRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  iconTint: Color = NeonCyan,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(22.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
      Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
  }
}

@Composable
private fun SettingsSwitchRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Icon(icon, contentDescription = title, tint = NeonCyan, modifier = Modifier.size(22.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
      Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = NeonIndigo,
        uncheckedTrackColor = SurfaceDark
      )
    )
  }
}

@Composable
private fun OptionSelectDialog(
  title: String,
  options: List<Pair<String, String>>,
  selectedKey: String,
  onSelect: (String) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(title, fontWeight = FontWeight.Bold, color = TextPrimary) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { (key, label) ->
          val isSelected = key == selectedKey
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelect(key) }
              .padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = label,
              style = MaterialTheme.typography.bodyMedium,
              color = if (isSelected) NeonCyan else TextPrimary,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            if (isSelected) {
              Text("✓", color = NeonCyan, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    },
    confirmButton = {
      Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = NeonIndigo)) {
        Text("Cancel", color = Color.White)
      }
    },
    containerColor = SurfaceDark
  )
}
