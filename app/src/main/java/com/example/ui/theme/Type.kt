package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val PlusJakartaSans = FontFamily(
  Font(R.font.plus_jakarta_sans, FontWeight.Normal),
  Font(R.font.plus_jakarta_sans, FontWeight.Medium),
  Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
  Font(R.font.plus_jakarta_sans, FontWeight.Bold)
)

val Typography = Typography(
  displayLarge = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 36.sp,
    color = TextPrimary
  ),
  headlineMedium = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    color = TextPrimary
  ),
  titleLarge = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    color = TextPrimary
  ),
  titleMedium = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = FontWeight.SemiBold,
    fontSize = 15.sp,
    lineHeight = 22.sp,
    color = TextPrimary
  ),
  bodyLarge = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    color = TextPrimary
  ),
  bodyMedium = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    color = TextSecondary
  ),
  labelLarge = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = FontWeight.SemiBold,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    color = TextPrimary
  ),
  labelSmall = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 15.sp,
    color = TextSecondary
  )
)
