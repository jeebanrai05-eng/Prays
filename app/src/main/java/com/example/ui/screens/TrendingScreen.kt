package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.LibraryRepository
import com.example.data.TrendingRepository
import com.example.model.TrendingFilter
import com.example.model.TrendingSort
import com.example.model.TrendingVideo
import com.example.ui.components.GlassCard
import com.example.ui.components.TrendingVideoCard
import com.example.ui.components.VideoPlayerDialog
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceLightNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TrendingHotRed
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
  trendingRepository: TrendingRepository,
  downloaderRepository: DownloaderRepository,
  libraryRepository: LibraryRepository,
  onPrepareFacebookPost: (TrendingVideo) -> Unit,
  onNavigateToDownloader: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val lastRefreshed by trendingRepository.lastRefreshed.collectAsState()
  val isRefreshing by trendingRepository.isRefreshing.collectAsState()
  val savedVideos by libraryRepository.savedTrendingVideos.collectAsState()

  var selectedFilter by remember { mutableStateOf(TrendingFilter.ALL) }
  var selectedSort by remember { mutableStateOf(TrendingSort.MOST_TRENDING) }
  var searchQuery by remember { mutableStateOf("") }
  var videosList by remember { mutableStateOf<List<TrendingVideo>>(emptyList()) }
  var previewVideo by remember { mutableStateOf<TrendingVideo?>(null) }

  // Load videos when filter/sort/search changes
  LaunchedEffect(selectedFilter, selectedSort, searchQuery, isRefreshing) {
    videosList = trendingRepository.getTrendingVideos(
      filter = selectedFilter,
      sort = selectedSort,
      searchQuery = searchQuery
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Top Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Whatshot,
            contentDescription = "Trending",
            tint = TrendingHotRed,
            modifier = Modifier.size(24.dp)
          )
          Text(
            text = "Trending Nepal",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }
        Text(
          text = "Updated: $lastRefreshed • Verified Nepal Politics Feed",
          style = MaterialTheme.typography.labelSmall,
          color = NeonCyan
        )
      }

      IconButton(
        onClick = {
          coroutineScope.launch {
            trendingRepository.refreshFeed()
            Toast.makeText(context, "Feed refreshed with latest Nepal political updates", Toast.LENGTH_SHORT).show()
          }
        },
        modifier = Modifier
          .clip(CircleShape)
          .background(SurfaceCard)
          .testTag("refresh_trending_button")
      ) {
        if (isRefreshing) {
          CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = NeonCyan)
        } else {
          Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = TextPrimary)
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("trending_search_field"),
      placeholder = {
        Text("Search Balen, Harka, Prachanda, Parliament...", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
      },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = "Search", tint = TextMuted)
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
          }
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

    // Horizontal Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      TrendingFilter.values().forEach { filter ->
        val selected = selectedFilter == filter
        FilterChip(
          selected = selected,
          onClick = { selectedFilter = filter },
          label = {
            Text(
              text = filter.label,
              style = MaterialTheme.typography.labelMedium,
              fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = NeonIndigo,
            selectedLabelColor = Color.White,
            containerColor = SurfaceCard,
            labelColor = TextSecondary
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = if (selected) NeonIndigo else SurfaceCardBorder
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Sort Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Icon(Icons.Default.FilterList, contentDescription = "Sort", tint = TextMuted, modifier = Modifier.size(16.dp))
        Text("Sort by:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
      }

      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        TrendingSort.values().forEach { sort ->
          val isCurrent = selectedSort == sort
          Text(
            text = sort.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isCurrent) NeonCyan else TextMuted,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .clickable { selectedSort = sort }
              .padding(horizontal = 4.dp, vertical = 2.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // Content List or Empty State
    if (videosList.isEmpty()) {
      GlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 24.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Info, contentDescription = "Empty", tint = TextMuted, modifier = Modifier.size(36.dp))
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "No Nepal Political Videos Found",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Try clearing your search query or selecting 'All Nepal Politics'.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .testTag("trending_videos_list"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(videosList, key = { it.id }) { video ->
          val isBookmarked = savedVideos.any { it.id == video.id }
          TrendingVideoCard(
            video = video,
            isBookmarked = isBookmarked,
            onWatchClick = {
              previewVideo = video
            },
            onDownloadClick = {
              downloaderRepository.startDownload(
                sourceUrl = video.sourceUrl,
                customTitle = video.nepaliTitle,
                thumbnailUrl = video.thumbnailUrl,
                quality = "4K Ultra HD",
                directStreamUrl = video.videoStreamUrl
              )
              Toast.makeText(context, "Added to Downloader in 4K Ultra HD!", Toast.LENGTH_SHORT).show()
              onNavigateToDownloader()
            },
            onPrepareFacebookPost = {
              onPrepareFacebookPost(video)
            },
            onBookmarkToggle = {
              libraryRepository.toggleBookmark(video)
              val nowSaved = libraryRepository.isBookmarked(video.id)
              Toast.makeText(
                context,
                if (nowSaved) "Saved to Library!" else "Removed from Library",
                Toast.LENGTH_SHORT
              ).show()
            }
          )
        }
      }
    }
  }

  // Video Preview Dialog
  previewVideo?.let { vid ->
    VideoPlayerDialog(
      video = vid,
      onDismiss = { previewVideo = null }
    )
  }
}
