package io.interviewmate.behavioural.questions.presentation.questions

import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.model.QuestionOrder


sealed class QuestionsEvent {
    data class Order(val questionOrder: QuestionOrder): QuestionsEvent()
    data class DeleteQuestion(val question: Question): QuestionsEvent()
    object RestoreQuestion: QuestionsEvent()
    object ToggleOrderSection: QuestionsEvent()
}
