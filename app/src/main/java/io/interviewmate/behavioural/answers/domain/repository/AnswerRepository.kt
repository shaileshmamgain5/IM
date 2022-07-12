package io.interviewmate.behavioural.answers.domain.repository
import io.interviewmate.behavioural.answers.domain.model.Answer
import kotlinx.coroutines.flow.Flow

interface AnswerRepository {
    fun getAnswers(): Flow<List<Answer>>
    fun getAnswers(questionId: Int): Flow<List<Answer>>
    suspend fun getAnswer(id: Int): Answer?
    suspend fun insertAnswer(answer: Answer)
    suspend fun deleteAnswer(answer: Answer)
}