package io.interviewmate.behavioural.answers.presentation.answers

import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.answers.domain.model.AnswerOrder
import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.model.QuestionOrder
import io.interviewmate.behavioural.util.OrderType

data class AnswersState(
    val answers: List<Answer> = emptyList(),
    val answerOrder: AnswerOrder = AnswerOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
