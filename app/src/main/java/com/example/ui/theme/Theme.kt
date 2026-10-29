package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
  primary = NeonIndigo,
  onPrimary = OnPrimary,
  primaryContainer = SurfaceCard,
  onPrimaryContainer = TextPrimary,
  secondary = NeonCyan,
  onSecondary = OnSecondary,
  secondaryContainer = SurfaceLightNavy,
  onSecondaryContainer = TextPrimary,
  tertiary = NeonPink,
  onTertiary = OnTertiary,
  background = BackgroundDark,
  onBackground = TextPrimary,
  surface = SurfaceDark,
  onSurface = TextPrimary,
  surfaceVariant = SurfaceLightNavy,
  onSurfaceVariant = TextSecondary,
  outline = SurfaceCardBorder,
  error = TrendingHotRed
)

@Composable
fun TokDownloaderTheme(
  darkTheme: Boolean = true, // Default to dark AMOLED/Navy theme as requested
  content: @Composable () -> Unit
) {
  val colorScheme = DarkColorScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = BackgroundDark.toArgb()
      window.navigationBarColor = BackgroundDark.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
      WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
