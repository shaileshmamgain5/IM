package io.interviewmate.behavioural.answers.presentation.answers

import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.answers.domain.model.AnswerOrder
import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.model.QuestionOrder


sealed class AnswersEvent {
    data class Order(val answerOrder: AnswerOrder): AnswersEvent()
    data class DeleteAnswer(val answer: Answer): AnswersEvent()
    object RestoreAnswer: AnswersEvent()
    object ToggleOrderSection: AnswersEvent()
}
