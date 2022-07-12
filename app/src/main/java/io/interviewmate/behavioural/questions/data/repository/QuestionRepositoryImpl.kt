package io.interviewmate.behavioural.questions.data.repository

import io.interviewmate.behavioural.questions.data.data_source.QuestionDao
import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow

class QuestionRepositoryImpl(
    private val dao: QuestionDao
) : QuestionRepository {
    override fun getQuestions(): Flow<List<Question>> {
        return dao.getQuestions()
    }

    override suspend fun getQuestionById(id: Int): Question? {
        return dao.getQuestionById(id)
    }

    override suspend fun insertQuestion(question: Question) {
        dao.insertQuestion(question)
    }

    override suspend fun deleteQuestion(question: Question) {
        dao.deleteQuestion(question)
    }

}