package io.interviewmate.behavioural.questions.presentation.add_edit_question

data class QuestionTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)
