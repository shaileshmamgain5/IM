package io.interviewmate.behavioural.answers.data.data_source

import androidx.room.*
import io.interviewmate.behavioural.answers.domain.model.Answer
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerDao {
    @Query("SELECT * FROM answer")
    fun getAnswers(): Flow<List<Answer>>

    @Query("SELECT * FROM answer where questionId = :questionId")
    fun getAnswers(questionId: Int): Flow<List<Answer>>

    @Query("SELECT * FROM answer where id = :id")
    suspend fun getAnswer(id: Int): Answer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: Answer)

    @Delete
    suspend fun deleteAnswer(answer: Answer)
}