package io.interviewmate.behavioural.answers.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.interviewmate.behavioural.ui.theme.surfaceWhite

@Composable
fun IMSurface(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val color = if (MaterialTheme.colors.isLight) {
        surfaceWhite
    } else {
        MaterialTheme.colors.surface
    }

    Surface(
    elevation = 2.dp,
    color = color,
    modifier = modifier.clip(shapes.medium)
    .let { if (onClick != null) it.clickable(onClick = onClick) else it }
    ) {
        content()
    }
}