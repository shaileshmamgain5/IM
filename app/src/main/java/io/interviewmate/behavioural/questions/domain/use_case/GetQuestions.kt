package io.interviewmate.behavioural.questions.domain.use_case

import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.model.QuestionOrder
import io.interviewmate.behavioural.questions.domain.repository.QuestionRepository
import io.interviewmate.behavioural.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetQuestions(
    private val repository: QuestionRepository
) {
    operator fun invoke(
        questionOrder: QuestionOrder
    ): Flow<List<Question>> {
        return repository.getQuestions().map { questions ->
            when (questionOrder.orderType) {
                is OrderType.Ascending -> {
                    when (questionOrder) {
                        is QuestionOrder.Title -> questions.sortedBy { it.title.lowercase() }
                        is QuestionOrder.Date -> questions.sortedBy { it.timestamp }
                    }
                }
                is OrderType.Descending -> {
                    when (questionOrder) {
                        is QuestionOrder.Title -> questions.sortedByDescending { it.title.lowercase() }
                        is QuestionOrder.Date -> questions.sortedByDescending { it.timestamp }
                    }
                }
            }
        }
    }
}