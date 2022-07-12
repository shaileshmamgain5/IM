package io.interviewmate.behavioural.questions.presentation.questions

import android.os.Handler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.interviewmate.behavioural.questions.domain.model.InvalidQuestionException
import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.model.QuestionOrder
import io.interviewmate.behavioural.questions.domain.use_case.QuestionUseCases
import io.interviewmate.behavioural.questions.presentation.add_edit_question.AddEditQuestionViewModel
import io.interviewmate.behavioural.questions.presentation.questions.QuestionsEvent
import io.interviewmate.behavioural.questions.presentation.questions.QuestionsState
import io.interviewmate.behavioural.util.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val questionUseCases: QuestionUseCases
) : ViewModel() {
    companion object {
        var questionJson : JSONArray? = null
    }

    private val _state = mutableStateOf(QuestionsState())
    val state: State<QuestionsState> = _state

    private var recentlyDeletedQuestion: Question? = null

    private var getQuestionsJob: Job? = null


    init {
        getQuestions(QuestionOrder.Date(OrderType.Descending))
        if (questionJson != null) {
            for (i in 0 until questionJson!!.length()) {
                viewModelScope.launch {
                    try {
                        var hint = questionJson!!.getJSONObject(i).getString("Ans1") ?: "Not Found";
                        questionJson!!.getJSONObject(i).getString("Ans2")?.let {
                            hint = hint.plus("\n $it")
                        }
                        questionUseCases.addQuestion(
                            Question(
                                title = questionJson!!.getJSONObject(i).getString("Title") ?: "NotFound",
                                description = questionJson!!.getJSONObject(i).getString("Principle") ?: "NotFound",
                                hint = hint,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                    } catch(e: InvalidQuestionException) {
                    }
                }
            }
        }
    }

    fun onEvent(event: QuestionsEvent) {
        when (event) {
            is QuestionsEvent.Order -> {
                if (state.value.questionOrder::class == event.questionOrder::class &&
                    state.value.questionOrder.orderType == event.questionOrder.orderType
                ) {
                    return
                }
                getQuestions(event.questionOrder)
            }
            is QuestionsEvent.DeleteQuestion -> {
                viewModelScope.launch {
                    questionUseCases.deleteQuestion(event.question)
                    recentlyDeletedQuestion = event.question
                }
            }
            is QuestionsEvent.RestoreQuestion -> {
                viewModelScope.launch {
                    questionUseCases.addQuestion(recentlyDeletedQuestion ?: return@launch)
                    recentlyDeletedQuestion = null
                }
            }
            is QuestionsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getQuestions(questionOrder: QuestionOrder) {
        getQuestionsJob?.cancel()
        getQuestionsJob = questionUseCases.getQuestions(questionOrder)
            .onEach { questions ->
                _state.value = state.value.copy(
                    questions = questions,
                    questionOrder = questionOrder
                )
            }
            .launchIn(viewModelScope)
    }
    var isRunningRandom = false
    fun showRandom() {
        if (!isRunningRandom) {
            Handler().postDelayed({

            }, 1000)
        }
    }
}