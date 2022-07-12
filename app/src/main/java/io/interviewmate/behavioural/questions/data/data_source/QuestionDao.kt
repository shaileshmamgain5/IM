package io.interviewmate.behavioural.questions.data.data_source

import androidx.room.*
import io.interviewmate.behavioural.questions.domain.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM question")
    fun getQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM question where id = :id")
    suspend fun getQuestionById(id: Int): Question?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)
}