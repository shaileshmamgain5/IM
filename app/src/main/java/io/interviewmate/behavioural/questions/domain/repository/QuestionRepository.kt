package io.interviewmate.behavioural.questions.domain.repository

import io.interviewmate.behavioural.questions.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun getQuestions(): Flow<List<Question>>

    suspend fun getQuestionById(id: Int): Question?

    suspend fun insertQuestion(question: Question)

    suspend fun deleteQuestion(question: Question)
}