package io.interviewmate.behavioural.questions.presentation.questions

import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.model.QuestionOrder
import io.interviewmate.behavioural.util.OrderType

data class QuestionsState(
    val questions: List<Question> = emptyList(),
    val questionOrder: QuestionOrder = QuestionOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
