package io.interviewmate.behavioural.answers.domain.use_case

import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.answers.domain.model.AnswerOrder
import io.interviewmate.behavioural.answers.domain.repository.AnswerRepository
import io.interviewmate.behavioural.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAnswers(
    private val repository: AnswerRepository
) {
    operator fun invoke(
        answerOrder: AnswerOrder
    ): Flow<List<Answer>> {
        return repository.getAnswers().map { answer ->
            when (answerOrder.orderType) {
                is OrderType.Ascending -> {
                    when (answerOrder) {
                        is AnswerOrder.Duration -> answer.sortedBy { it.duration.lowercase() }
                        is AnswerOrder.Date -> answer.sortedBy { it.timestamp }
                    }
                }
                is OrderType.Descending -> {
                    when (answerOrder) {
                        is AnswerOrder.Duration -> answer.sortedByDescending { it.duration.lowercase() }
                        is AnswerOrder.Date -> answer.sortedByDescending { it.timestamp }
                    }
                }
            }
        }
    }
}