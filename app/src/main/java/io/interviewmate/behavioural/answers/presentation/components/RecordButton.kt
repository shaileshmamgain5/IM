package io.interviewmate.behavioural.answers.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lens
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.interviewmate.behavioural.R
import io.interviewmate.behavioural.ui.theme.red700
import io.interviewmate.behavioural.ui.theme.red800

@ExperimentalAnimationApi
@Composable
fun RecordButton(modifier: Modifier, isRecording: Boolean, onClickListener: () -> Unit) {
    val gradientBackground = Brush.horizontalGradient(
        colors = listOf(red800, red700),
        startX = 0f,
        endX = 128f
    )
    val componentModifier = modifier.run {
        this
            .size(64.dp)
            .clip(CircleShape)
            .background(brush = gradientBackground)
            .clickable(onClick = onClickListener)
    }

    Surface(color = Color.Transparent, modifier = componentModifier) {
        AnimatedVisibility(
            visible = isRecording,
            enter = fadeIn(initialAlpha = 0.3f),
            exit = fadeOut()

        ) {
            Image(
                colorFilter = ColorFilter.tint(Color.White),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_stop_24),
                alignment = Alignment.Center,
                modifier = Modifier.requiredWidth(42.dp),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
        }
        AnimatedVisibility(
            visible = !isRecording,
            enter = fadeIn(initialAlpha = 0.3f),
            exit = fadeOut()

        ) {
            Image(
                colorFilter = ColorFilter.tint(Color.White),
                imageVector = Icons.Filled.Lens,
                alignment = Alignment.Center,
                modifier = Modifier.requiredWidth(32.dp),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
        }
    }

}