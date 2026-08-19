package com.yaobing.module_middleware.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun GreetingCardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // 自定义符合Material3要求的Typography，你可以在这里自定义各类文本样式
    val AppTypography = Typography(
        bodyLarge = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        // 其他文本样式可以根据需要自行配置，不配置会使用Material3默认值
    )
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}