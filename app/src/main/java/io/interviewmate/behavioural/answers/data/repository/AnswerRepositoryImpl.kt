package io.interviewmate.behavioural.answers.data.repository

import io.interviewmate.behavioural.answers.data.data_source.AnswerDao
import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.answers.domain.repository.AnswerRepository
import kotlinx.coroutines.flow.Flow

class AnswerRepositoryImpl(
    private val dao: AnswerDao
) : AnswerRepository {
    override fun getAnswers(): Flow<List<Answer>> {
        return dao.getAnswers()
    }

    override fun getAnswers(questionId: Int): Flow<List<Answer>> {
        return dao.getAnswers(questionId)
    }

    override suspend fun getAnswer(id: Int): Answer? {
        return dao.getAnswer(id)
    }

    override suspend fun insertAnswer(answer: Answer) {
        dao.insertAnswer(answer)
    }

    override suspend fun deleteAnswer(answer: Answer) {
        dao.deleteAnswer(answer)
    }
}