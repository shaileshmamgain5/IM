package io.interviewmate.behavioural.answers.presentation.answers

import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.answers.domain.model.AnswerOrder
import io.interviewmate.behavioural.answers.domain.use_case.AnswerUseCases
import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.model.QuestionOrder
import io.interviewmate.behavioural.questions.domain.use_case.QuestionUseCases
import io.interviewmate.behavioural.questions.presentation.add_edit_question.AudioPlayer
import io.interviewmate.behavioural.questions.presentation.questions.QuestionsEvent
import io.interviewmate.behavioural.questions.presentation.questions.QuestionsState
import io.interviewmate.behavioural.util.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class AnswersViewModel @Inject constructor(
    private val questionUseCases: QuestionUseCases,
    private val answerUseCases: AnswerUseCases,
    private val audioPlayer: AudioPlayer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        var questionId: Int = -1
    }
    val mHandler = Handler(Looper.getMainLooper())
    val middlePlayer = MutableStateFlow(MiddlePlayer())

    private val _state = mutableStateOf(AnswersState())
    val state: State<AnswersState> = _state

    private var recentlyDeletedAnswer: Answer? = null

    private var getAnswersJob: Job? = null
    var questionTitle: String? = null

    init {
        getAnswers(AnswerOrder.Date(OrderType.Descending))
        savedStateHandle.get<String>("questionTitle")?.let { question ->
            questionTitle = question
        }
    }

    fun onEvent(event: AnswersEvent) {
        when (event) {
            is AnswersEvent.Order -> {
                if (state.value.answerOrder::class == event.answerOrder::class &&
                    state.value.answerOrder.orderType == event.answerOrder.orderType
                ) {
                    return
                }
                getAnswers(event.answerOrder)
            }
            is AnswersEvent.DeleteAnswer -> {
                viewModelScope.launch {
                    answerUseCases.deleteAnswer(event.answer)
                    recentlyDeletedAnswer = event.answer
                }
            }
            is AnswersEvent.RestoreAnswer -> {
                viewModelScope.launch {
                    answerUseCases.addAnswer(recentlyDeletedAnswer ?: return@launch)
                    recentlyDeletedAnswer = null
                }
            }
            is AnswersEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getAnswers(answerOrder: AnswerOrder) {
        getAnswersJob?.cancel()
        getAnswersJob = answerUseCases.getAnswersFor(questionId, AnswerOrder.Date(OrderType.Descending))
            .onEach { questions ->
                _state.value = state.value.copy(
                    answers = questions,
                    answerOrder = answerOrder
                )
            }
            .launchIn(viewModelScope)
    }

    // ** Mediaplayer Funs **

    fun initMediaPlayer(uri: Uri) {
        if (middlePlayer.value.isPlaying) {
            pauseMedia()
        } else {
            viewModelScope.launch {
                audioPlayer.prepareMediaPlayer(uri)
                middlePlayer.emit(
                    middlePlayer.value.copy(
                        duration = audioPlayer.mediaPlayer.duration,
                        currentPosition = audioPlayer.mediaPlayer.currentPosition,
                        isPlaying = false
                    )
                )
            }
            mHandler.postDelayed(
                Runnable { playMedia() }
                , 500)

        }
    }

    fun rewindTenSeconds() {
        audioPlayer.mediaPlayer.seekTo(audioPlayer.mediaPlayer.currentPosition - 10000)
        updateCurrentPosition()
    }

    fun forwardTenSeconds() {
        audioPlayer.mediaPlayer.seekTo(
            audioPlayer.mediaPlayer.currentPosition + (min(
                10000,
                audioPlayer.mediaPlayer.duration - audioPlayer.mediaPlayer.currentPosition
            ))
        )
        updateCurrentPosition()
    }

    fun playMedia() {
        viewModelScope.launch {
            audioPlayer.mediaPlayer.start()

            middlePlayer.emit(
                middlePlayer.value.copy(
                    isPlaying = true
                )
            )
        }

        mHandler.postDelayed(object : Runnable {

            override fun run() {
                if (middlePlayer.value.isPlaying) {
                    mHandler.postDelayed(this, 1000)
                    updateCurrentPosition()
                }
            }
        }, 0)
    }

    fun pauseMedia() {
        viewModelScope.launch {
            audioPlayer.mediaPlayer.pause()
            mHandler.removeCallbacksAndMessages(null)
            middlePlayer.emit(
                middlePlayer.value.copy(
                    isPlaying = false
                )
            )
        }

    }

    private fun updateCurrentPosition() {
        viewModelScope.launch {
            middlePlayer.emit(
                middlePlayer.value.copy(
                    currentPosition = audioPlayer.mediaPlayer.currentPosition
                )
            )
        }
    }
}

data class MiddlePlayer(
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val duration: Int = 10
)