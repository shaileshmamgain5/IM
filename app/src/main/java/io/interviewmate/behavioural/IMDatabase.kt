package io.interviewmate.behavioural

import androidx.room.Database
import androidx.room.RoomDatabase
import io.interviewmate.behavioural.answers.data.data_source.AnswerDao
import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.questions.data.data_source.QuestionDao
import io.interviewmate.behavioural.questions.domain.model.Question

@Database (
    entities = [Question::class, Answer::class],
    version = 1
        )

abstract class IMDatabase: RoomDatabase() {
    abstract val questionDao: QuestionDao
    abstract val answerDao: AnswerDao

    companion object {
        const val DATABASE_NAME = "im_db"
    }
}