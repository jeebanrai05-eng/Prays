package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.NeonPurple

@Composable
fun AnimatedGridBackground(modifier: Modifier = Modifier) {
  val infiniteTransition = rememberInfiniteTransition(label = "grid_anim")
  val orbOffset1 by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(12000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "orb1"
  )
  val orbOffset2 by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(16000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "orb2"
  )

  Canvas(modifier = modifier.fillMaxSize()) {
    val width = size.width
    val height = size.height

    // Deep base canvas
    drawRect(color = BackgroundDark)

    // Floating glowing orb 1 (Purple/Indigo)
    val cx1 = width * (0.2f + 0.3f * orbOffset1)
    val cy1 = height * (0.15f + 0.2f * orbOffset2)
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(NeonPurple.copy(alpha = 0.16f), NeonIndigo.copy(alpha = 0.05f), Color.Transparent),
        center = Offset(cx1, cy1),
        radius = width * 0.7f
      ),
      center = Offset(cx1, cy1),
      radius = width * 0.7f
    )

    // Floating glowing orb 2 (Cyan/Blue)
    val cx2 = width * (0.8f - 0.25f * orbOffset2)
    val cy2 = height * (0.7f + 0.15f * orbOffset1)
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(NeonCyan.copy(alpha = 0.12f), NeonIndigo.copy(alpha = 0.04f), Color.Transparent),
        center = Offset(cx2, cy2),
        radius = width * 0.65f
      ),
      center = Offset(cx2, cy2),
      radius = width * 0.65f
    )

    // Subtle futuristic perspective grid lines
    val gridSpacing = 44f
    val gridAlpha = 0.035f
    val gridColor = Color(0xFF6366F1).copy(alpha = gridAlpha)

    var x = 0f
    while (x <= width) {
      drawLine(
        color = gridColor,
        start = Offset(x, 0f),
        end = Offset(x, height),
        strokeWidth = 1f
      )
      x += gridSpacing
    }

    var y = 0f
    while (y <= height) {
      drawLine(
        color = gridColor,
        start = Offset(0f, y),
        end = Offset(width, y),
        strokeWidth = 1f
      )
      y += gridSpacing
    }
  }
}
