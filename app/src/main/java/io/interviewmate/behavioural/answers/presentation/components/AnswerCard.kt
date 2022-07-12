package io.interviewmate.behavioural.answers.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.ui.theme.blue200
import io.interviewmate.behavioural.ui.theme.blue700
import io.interviewmate.behavioural.ui.theme.grey700
import io.interviewmate.behavioural.R

@Composable
fun RecordingCard(
    answer: Answer,
    onClickListener: () -> Unit
) {

    IMSurface(
        onClick = onClickListener,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .weight(8f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = answer.readableDateTime,
                        style = MaterialTheme.typography.h6,
                    )
                    Text(
                        text = answer.readableDate,
                        style = MaterialTheme.typography.body1,
                        color = grey700
                    )
                }
                Text(
                    text = answer.duration,
                    style = MaterialTheme.typography.body1,
                    color = blue700
                )

            }
            Image(
                colorFilter = ColorFilter.tint(blue200),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_play_arrow_24),
                alignment = Alignment.Center,
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .requiredWidth(36.dp)
                    .weight(1f)
                    .align(Alignment.Bottom),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
        }
    }
}