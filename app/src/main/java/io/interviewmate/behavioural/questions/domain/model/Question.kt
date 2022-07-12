package io.interviewmate.behavioural.questions.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    val title: String,
    val description: String,
    val hint: String,
    val timestamp: Long,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        //remove if not required
    }
}

class InvalidQuestionException(message: String): Exception(message)