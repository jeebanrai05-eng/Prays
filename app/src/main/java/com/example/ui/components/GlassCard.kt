package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonIndigo
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun GlassCard(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(16.dp),
  backgroundColor: Color = SurfaceCard,
  borderColor: Color = SurfaceCardBorder,
  borderWidth: Dp = 1.dp,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  val border = BorderStroke(
    borderWidth,
    Brush.linearGradient(
      colors = listOf(
        borderColor.copy(alpha = 0.45f),
        NeonIndigo.copy(alpha = 0.20f),
        borderColor.copy(alpha = 0.10f)
      )
    )
  )

  Surface(
    modifier = modifier
      .clip(shape)
      .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    shape = shape,
    color = backgroundColor,
    border = border,
    tonalElevation = 2.dp
  ) {
    Box(modifier = Modifier.padding(16.dp)) {
      content()
    }
  }
}
