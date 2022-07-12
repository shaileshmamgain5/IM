package io.interviewmate.behavioural.questions.presentation.add_edit_question

import androidx.compose.ui.focus.FocusState

sealed class AddEditQuestionEvent{
    data class EnteredTitle(val value: String): AddEditQuestionEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditQuestionEvent()
    data class EnteredContent(val value: String): AddEditQuestionEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditQuestionEvent()
    object SaveQuestion: AddEditQuestionEvent()
    object ShowHint: AddEditQuestionEvent()
}

