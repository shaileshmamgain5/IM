package io.interviewmate.behavioural.answers.domain.model

import android.media.MediaMetadataRetriever
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Entity
data class Answer (
    val questionId: Int? = null,
    val duration: String,
    val readableDate: String,
    val readableDateTime: String,
    val timestamp: Long,
    val path: String,
    @PrimaryKey val id: Int? = null
        ) {

}

class InvalidAnswerException(message: String) : Exception(message)

fun convertDurationToString(duration: Int): String = String.format(
    "%02d:%02d",
    TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
    TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
)

fun generateRecordingName(path: String?): String {
    return "${path}/InterviewMate-${
        SimpleDateFormat("ddMMyyyy-HHmmss", Locale.getDefault()).format(
            Calendar.getInstance().time
        )
    }.m4a"
}