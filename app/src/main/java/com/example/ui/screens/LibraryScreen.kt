package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.DownloaderRepository
import com.example.data.LibraryRepository
import com.example.model.DownloadItem
import com.example.model.DownloadStatus
import com.example.model.PreparedFacebookPost
import com.example.model.TrendingVideo
import com.example.ui.components.GlassCard
import com.example.ui.components.VideoPlayerDialog
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceLightNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TrendingHotRed

@Composable
fun LibraryScreen(
  downloaderRepository: DownloaderRepository,
  libraryRepository: LibraryRepository,
  onPrepareFacebookPost: (TrendingVideo) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Downloaded", "Prepared Posts", "Saved Trending")

  val downloads by downloaderRepository.downloads.collectAsState()
  val savedPosts by libraryRepository.savedPosts.collectAsState()
  val savedVideos by libraryRepository.savedTrendingVideos.collectAsState()

  val completedDownloads = remember(downloads) {
    downloads.filter { it.status == DownloadStatus.COMPLETED }
  }

  // Delete confirmation dialog state
  var itemToDelete by remember { mutableStateOf<Any?>(null) }
  var previewVideo by remember { mutableStateOf<TrendingVideo?>(null) }

  fun copyToClipboard(label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    Toast.makeText(context, "$label copied!", Toast.LENGTH_SHORT).show()
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
          text = "Media Library",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Text(
          text = "Downloaded media, prepared captions & saved posts",
          style = MaterialTheme.typography.labelSmall,
          color = NeonCyan
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Modern TabRow
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = SurfaceDark,
      contentColor = NeonCyan,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = NeonCyan,
          height = 3.dp
        )
      },
      modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(SurfaceDark)
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              style = MaterialTheme.typography.labelMedium,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
              color = if (selectedTab == index) TextPrimary else TextMuted
            )
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Tab Content
    when (selectedTab) {
      0 -> {
        // Downloaded Videos
        if (completedDownloads.isEmpty()) {
          EmptyLibraryCard(
            title = "No Downloaded Videos Yet",
            subtitle = "Download political or TikTok videos from the Downloader or Trending tab."
          )
        } else {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(completedDownloads, key = { it.id }) { item ->
              DownloadedItemCard(
                item = item,
                onPlay = {
                  Toast.makeText(context, "Playing ${item.title}", Toast.LENGTH_SHORT).show()
                },
                onShare = {
                  val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Check out this video: ${item.title}\n${item.sourceUrl}")
                  }
                  context.startActivity(Intent.createChooser(sendIntent, "Share Video"))
                },
                onDelete = { itemToDelete = item }
              )
            }
          }
        }
      }
      1 -> {
        // Prepared Facebook Posts
        if (savedPosts.isEmpty()) {
          EmptyLibraryCard(
            title = "No Prepared Facebook Posts",
            subtitle = "Tap 'Prepare Facebook Post' on any video in Trending to customize titles and hashtags."
          )
        } else {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            items(savedPosts, key = { it.id }) { post ->
              PreparedPostCard(
                post = post,
                onCopyTitle = { copyToClipboard("Title", post.nepaliTitle) },
                onCopyCaption = { copyToClipboard("Caption", post.nepaliCaption) },
                onCopyHashtags = { copyToClipboard("Hashtags", post.hashtags) },
                onCopyAll = {
                  val all = "${post.nepaliTitle}\n\n${post.nepaliCaption}\n\n${post.hashtags}"
                  copyToClipboard("All Content", all)
                },
                onShareToFacebook = {
                  val all = "${post.nepaliTitle}\n\n${post.nepaliCaption}\n\n${post.hashtags}"
                  copyToClipboard("Post Text", all)
                  val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, all)
                  }
                  context.startActivity(Intent.createChooser(shareIntent, "Share to Facebook"))
                },
                onDelete = { itemToDelete = post }
              )
            }
          }
        }
      }
      2 -> {
        // Saved Trending Videos
        if (savedVideos.isEmpty()) {
          EmptyLibraryCard(
            title = "No Saved Trending Videos",
            subtitle = "Tap the bookmark icon on any Nepal political video in Trending to save it here."
          )
        } else {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(savedVideos, key = { it.id }) { video ->
              SavedTrendingCard(
                video = video,
                onWatch = { previewVideo = video },
                onPreparePost = { onPrepareFacebookPost(video) },
                onRemove = { libraryRepository.toggleBookmark(video) }
              )
            }
          }
        }
      }
    }
  }

  // Delete Confirmation Dialog
  itemToDelete?.let { item ->
    AlertDialog(
      onDismissRequest = { itemToDelete = null },
      title = {
        Text("Confirm Deletion", fontWeight = FontWeight.Bold, color = TextPrimary)
      },
      text = {
        Text(
          "Are you sure you want to permanently delete this item from your library?",
          color = TextSecondary
        )
      },
      confirmButton = {
        Button(
          onClick = {
            when (item) {
              is DownloadItem -> downloaderRepository.removeDownload(item.id)
              is PreparedFacebookPost -> libraryRepository.deletePreparedPost(item.id)
            }
            itemToDelete = null
            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = TrendingHotRed)
        ) {
          Text("Delete", color = Color.White)
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { itemToDelete = null }) {
          Text("Cancel", color = TextPrimary)
        }
      },
      containerColor = SurfaceDark
    )
  }

  // Preview Video Dialog
  previewVideo?.let { vid ->
    VideoPlayerDialog(
      video = vid,
      onDismiss = { previewVideo = null }
    )
  }
}

@Composable
private fun DownloadedItemCard(
  item: DownloadItem,
  onPlay: () -> Unit,
  onShare: () -> Unit,
  onDelete: () -> Unit
) {
  GlassCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceLightNavy)
            .clickable(onClick = onPlay),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = NeonCyan, modifier = Modifier.size(32.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${item.quality} • 14.8 MB",
            style = MaterialTheme.typography.labelSmall,
            color = NeonCyan
          )
        }

        Row {
          IconButton(onClick = onShare) {
            Icon(Icons.Default.Share, contentDescription = "Share", tint = TextSecondary, modifier = Modifier.size(20.dp))
          }
          IconButton(onClick = onDelete) {
            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TrendingHotRed, modifier = Modifier.size(20.dp))
          }
        }
      }
    }
  }
}

@Composable
private fun PreparedPostCard(
  post: PreparedFacebookPost,
  onCopyTitle: () -> Unit,
  onCopyCaption: () -> Unit,
  onCopyHashtags: () -> Unit,
  onCopyAll: () -> Unit,
  onShareToFacebook: () -> Unit,
  onDelete: () -> Unit
) {
  GlassCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Facebook Post Ready",
          style = MaterialTheme.typography.labelSmall,
          color = NeonPink,
          fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
          Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(18.dp))
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = post.nepaliTitle,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = post.nepaliCaption,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = post.hashtags,
        style = MaterialTheme.typography.labelSmall,
        color = NeonCyan,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Copy Action buttons row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        OutlinedButton(
          onClick = onCopyCaption,
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.weight(1f)
        ) {
          Text("Copy Caption", style = MaterialTheme.typography.labelSmall, color = NeonCyan)
        }

        OutlinedButton(
          onClick = onCopyAll,
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.weight(1f)
        ) {
          Text("Copy All", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
        }

        Button(
          onClick = onShareToFacebook,
          shape = RoundedCornerShape(6.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
          modifier = Modifier.weight(1f)
        ) {
          Text("Facebook", style = MaterialTheme.typography.labelSmall, color = Color.White)
        }
      }
    }
  }
}

@Composable
private fun SavedTrendingCard(
  video: TrendingVideo,
  onWatch: () -> Unit,
  onPreparePost: () -> Unit,
  onRemove: () -> Unit
) {
  GlassCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(video.thumbnailUrl)
            .crossfade(true)
            .build(),
          contentDescription = video.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(width = 90.dp, height = 65.dp)
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
          Text(
            text = video.nepaliTitle,
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }

        IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
          Icon(Icons.Default.Bookmark, contentDescription = "Remove", tint = NeonCyan, modifier = Modifier.size(18.dp))
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onWatch,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = "Watch", modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Watch", style = MaterialTheme.typography.labelSmall)
        }

        Button(
          onClick = onPreparePost,
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
          modifier = Modifier.weight(1.5f)
        ) {
          Icon(Icons.Default.AutoAwesome, contentDescription = "Post", modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Prepare Post", style = MaterialTheme.typography.labelSmall)
        }
      }
    }
  }
}

@Composable
private fun EmptyLibraryCard(title: String, subtitle: String) {
  GlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 20.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(Icons.Default.VideoLibrary, contentDescription = "Empty", tint = TextMuted, modifier = Modifier.size(44.dp))
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary
      )
    }
  }
}
