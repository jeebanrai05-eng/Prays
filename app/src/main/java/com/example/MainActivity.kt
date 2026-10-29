package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.DownloaderRepository
import com.example.data.LibraryRepository
import com.example.data.SettingsRepository
import com.example.data.TrendingRepository
import com.example.model.TrendingVideo
import com.example.ui.components.AnimatedGridBackground
import com.example.ui.components.FacebookPostPrepScreen
import com.example.ui.screens.DownloaderScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TrendingScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TokDownloaderTheme

enum class MainNavigationTab(
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
) {
  DOWNLOADER("Downloader", Icons.Filled.Download, Icons.Outlined.Download, "nav_downloader"),
  TRENDING("Trending", Icons.Filled.Whatshot, Icons.Outlined.Whatshot, "nav_trending"),
  LIBRARY("Library", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary, "nav_library"),
  SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "nav_settings")
}

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Initialize repositories
    val trendingRepository = TrendingRepository()
    val downloaderRepository = DownloaderRepository(applicationContext)
    val libraryRepository = LibraryRepository()
    val settingsRepository = SettingsRepository(applicationContext)
    val updateManager = com.example.data.UpdateManager(applicationContext)

    setContent {
      TokDownloaderTheme {
        MainAppContent(
          trendingRepository = trendingRepository,
          downloaderRepository = downloaderRepository,
          libraryRepository = libraryRepository,
          settingsRepository = settingsRepository,
          updateManager = updateManager
        )
      }
    }
  }
}

@Composable
fun MainAppContent(
  trendingRepository: TrendingRepository,
  downloaderRepository: DownloaderRepository,
  libraryRepository: LibraryRepository,
  settingsRepository: SettingsRepository,
  updateManager: com.example.data.UpdateManager
) {
  var currentTab by remember { mutableStateOf(MainNavigationTab.TRENDING) }
  var prepVideo by remember { mutableStateOf<TrendingVideo?>(null) }

  // If user is preparing a Facebook post, render the dedicated preparation screen
  prepVideo?.let { video ->
    FacebookPostPrepScreen(
      video = video,
      onBack = { prepVideo = null },
      onDownloadRequested = {
        downloaderRepository.startDownload(
          sourceUrl = video.sourceUrl,
          customTitle = video.nepaliTitle,
          thumbnailUrl = video.thumbnailUrl
        )
      },
      onSaveToLibrary = { post ->
        libraryRepository.savePreparedPost(post)
      }
    )
    return
  }

  // Handle system back gesture
  BackHandler(enabled = currentTab != MainNavigationTab.TRENDING) {
    currentTab = MainNavigationTab.TRENDING
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(BackgroundDark),
    containerColor = Color.Transparent,
    contentWindowInsets = WindowInsets.navigationBars,
    bottomBar = {
      NavigationBar(
        containerColor = SurfaceDark,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.testTag("main_bottom_nav_bar")
      ) {
        MainNavigationTab.values().forEach { tab ->
          val isSelected = currentTab == tab
          NavigationBarItem(
            selected = isSelected,
            onClick = { currentTab = tab },
            icon = {
              Icon(
                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                contentDescription = tab.label
              )
            },
            label = {
              Text(
                text = tab.label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color.White,
              selectedTextColor = NeonCyan,
              indicatorColor = NeonIndigo,
              unselectedIconColor = TextMuted,
              unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag(tab.testTag)
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(BackgroundDark)
    ) {
      // Futuristic animated subtle grid with floating glowing orbs
      AnimatedGridBackground(modifier = Modifier.fillMaxSize())

      // Tab Screen Content with smooth fade transitions
      Box(
        modifier = Modifier
          .fillMaxSize()
          .statusBarsPadding()
          .padding(innerPadding)
      ) {
        Crossfade(targetState = currentTab, label = "tab_crossfade") { tab ->
          when (tab) {
            MainNavigationTab.DOWNLOADER -> {
              DownloaderScreen(
                downloaderRepository = downloaderRepository,
                onPrepareFacebookPost = { selectedVid ->
                  prepVideo = selectedVid
                }
              )
            }
            MainNavigationTab.TRENDING -> {
              TrendingScreen(
                trendingRepository = trendingRepository,
                downloaderRepository = downloaderRepository,
                libraryRepository = libraryRepository,
                onPrepareFacebookPost = { selectedVid ->
                  prepVideo = selectedVid
                },
                onNavigateToDownloader = {
                  currentTab = MainNavigationTab.DOWNLOADER
                }
              )
            }
            MainNavigationTab.LIBRARY -> {
              LibraryScreen(
                downloaderRepository = downloaderRepository,
                libraryRepository = libraryRepository,
                onPrepareFacebookPost = { selectedVid ->
                  prepVideo = selectedVid
                }
              )
            }
            MainNavigationTab.SETTINGS -> {
              SettingsScreen(
                settingsRepository = settingsRepository,
                downloaderRepository = downloaderRepository,
                updateManager = updateManager
              )
            }
          }
        }
      }
    }
  }
}
