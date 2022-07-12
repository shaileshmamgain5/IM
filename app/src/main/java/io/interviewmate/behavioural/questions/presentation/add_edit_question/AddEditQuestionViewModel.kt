package io.interviewmate.behavioural.questions.presentation.add_edit_question

import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.answers.domain.model.generateRecordingName
import io.interviewmate.behavioural.answers.domain.use_case.AnswerUseCases
import io.interviewmate.behavioural.questions.domain.model.InvalidQuestionException
import io.interviewmate.behavioural.questions.domain.model.Question
import io.interviewmate.behavioural.questions.domain.use_case.QuestionUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditQuestionViewModel @Inject constructor(
    private val questionUseCases: QuestionUseCases,
    private val answerUseCases: AnswerUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        var folderPath: String = ""
    }

    private val _questionTitle = mutableStateOf(QuestionTextFieldState(
        hint = "Enter question..."
    ))
    val questionTitle: State<QuestionTextFieldState> = _questionTitle

    private val _questionContent = mutableStateOf(QuestionTextFieldState(
        hint = "Add details"
    ))
    val questionContent: State<QuestionTextFieldState> = _questionContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentQuestionId: Int? = null
    var answerHint: String = ""
    var shouldShowAnswerHint: MutableState<Boolean> = mutableStateOf(false)

    //record answer
    private var recorder: MediaRecorder? = null

    var recordingState: MutableState<Boolean> = mutableStateOf(false)

    init {
        savedStateHandle.get<Int>("questionId")?.let { questionId ->
            if(questionId != -1) {
                viewModelScope.launch {
                    questionUseCases.getQuestion(questionId)?.also { question ->
                        currentQuestionId = question.id
                        answerHint = question.hint
                        _questionTitle.value = questionTitle.value.copy(
                            text = question.title,
                            isHintVisible = false
                        )
                        _questionContent.value = _questionContent.value.copy(
                            text = question.description,
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditQuestionEvent) {
        when(event) {
            is AddEditQuestionEvent.EnteredTitle -> {
                _questionTitle.value = questionTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditQuestionEvent.ChangeTitleFocus -> {
                _questionTitle.value = questionTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            questionTitle.value.text.isBlank()
                )
            }
            is AddEditQuestionEvent.EnteredContent -> {
                _questionContent.value = _questionContent.value.copy(
                    text = event.value
                )
            }
            is AddEditQuestionEvent.ChangeContentFocus -> {
                _questionContent.value = _questionContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _questionContent.value.text.isBlank()
                )
            }

            is AddEditQuestionEvent.SaveQuestion -> {
                viewModelScope.launch {
                    try {
                        questionUseCases.addQuestion(
                            Question(
                                title = questionTitle.value.text,
                                description = questionContent.value.text,
                                hint= "",
                                timestamp = System.currentTimeMillis(),
                                id = currentQuestionId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveQuestion)
                    } catch(e: InvalidQuestionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save question"
                            )
                        )
                    }
                }
            }

            is AddEditQuestionEvent.ShowHint -> {
                shouldShowAnswerHint.value = true
            }
        }


    }


    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveQuestion: UiEvent()
    }

    var fileName: String = ""
    fun startRecording() {
        fileName = generateRecordingName(folderPath)
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setAudioEncodingBitRate(16 * 44100)
            setAudioSamplingRate(44100)
        }

        try {
            recorder?.prepare()
            recorder?.start()
            viewModelScope.launch {
                recordingState.value = true
            }
        } catch (e: IOException) {
            viewModelScope.launch {
                recordingState.value = false
            }
        }
    }

    fun stopRecording() {
        recorder?.apply {
            stop()
            reset()
            release()
        }
        recorder = null

        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.time = Date(System.currentTimeMillis())
            try {
                answerUseCases.addAnswer(
                    Answer(
                        questionId = currentQuestionId,
                        timestamp = System.currentTimeMillis(),
                        readableDate = "${calendar.get(Calendar.DAY_OF_MONTH)} ${
                            SimpleDateFormat(
                                "MMM",
                                Locale.getDefault()
                            ).format(calendar.time)
                        }",
                        readableDateTime = "${
                            SimpleDateFormat(
                                "EEEE",
                                Locale.getDefault()
                            ).format(calendar.time)
                        } at " + String.format(
                            "%02d:%02d",
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE)
                        ),
                        path= fileName,
                        duration = "40"
                    )
                )
            } catch(e: InvalidQuestionException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        message = e.message ?: "Couldn't save question"
                    )
                )
            }
        }
        viewModelScope.launch {
            recordingState.value = false
        }
    }
}