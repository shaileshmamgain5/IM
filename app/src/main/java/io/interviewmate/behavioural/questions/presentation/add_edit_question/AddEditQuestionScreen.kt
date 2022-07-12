package io.interviewmate.behavioural.questions.presentation.add_edit_question

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.interviewmate.behavioural.answers.presentation.answers.AnswersViewModel
import io.interviewmate.behavioural.answers.presentation.components.RecordButton
import io.interviewmate.behavioural.questions.presentation.add_edit_question.components.TransparentHintTextField
import io.interviewmate.behavioural.questions.presentation.util.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AddEditQuestionScreen(
    navController: NavController,
    viewModel: AddEditQuestionViewModel = hiltViewModel()
) {
    val titleState = viewModel.questionTitle.value
    val contentState = viewModel.questionContent.value
    val recordingState = viewModel.recordingState.value
    val isNewQuestion = viewModel.currentQuestionId == null
    val showHint = viewModel.shouldShowAnswerHint.value
    val answerHint = viewModel.answerHint

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditQuestionViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditQuestionViewModel.UiEvent.SaveQuestion -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if (isNewQuestion) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(AddEditQuestionEvent.SaveQuestion)
                    },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Save question")
                }
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(if (isNewQuestion) 0.dp else 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .align(Alignment.CenterHorizontally),
                elevation = 10.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .align(Alignment.Center)
                    ) {
                        TransparentHintTextField(
                            text = titleState.text,
                            hint = titleState.hint,
                            onValueChange = {
                                viewModel.onEvent(AddEditQuestionEvent.EnteredTitle(it))
                            },
                            onFocusChange = {
                                viewModel.onEvent(AddEditQuestionEvent.ChangeTitleFocus(it))
                            },
                            isHintVisible = titleState.isHintVisible,
                            singleLine = false,
                            textStyle = MaterialTheme.typography.h6
                        )
                        if (isNewQuestion || showHint) {
                            Spacer(modifier = Modifier.height(16.dp))
                            TransparentHintTextField(
                                text = contentState.text,
                                hint = contentState.hint,
                                onValueChange = {
                                    viewModel.onEvent(AddEditQuestionEvent.EnteredContent(it))
                                },
                                onFocusChange = {
                                    viewModel.onEvent(AddEditQuestionEvent.ChangeContentFocus(it))
                                },
                                isHintVisible = contentState.isHintVisible,
                                textStyle = MaterialTheme.typography.body1
                            )
                        }
                    }
                    if (!isNewQuestion) {
                        when (recordingState) {
                            true -> {
                                RecordButton(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.BottomCenter),
                                    isRecording = true,
                                    onClickListener = onRecordPressed(
                                        viewModel = viewModel
                                    )
                                )
                            }
                            else -> {
                                RecordButton(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.BottomCenter),
                                    isRecording = false,
                                    onClickListener = onRecordPressed(
                                        viewModel = viewModel
                                    )
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowRight,
                            contentDescription = "See Answers",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(16.dp)
                                .align(Alignment.BottomEnd)
                                .clickable(onClick = showAnswers(navController, viewModel))
                        )

                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Shuffle",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(16.dp)
                                .align(Alignment.BottomStart)
                                .clickable(onClick = shuffle(navController))
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(if (isNewQuestion) .0001f else .2f)
                    .align(Alignment.CenterHorizontally),
            ) {
                if (!isNewQuestion) {
                    if (showHint) {
                        Text(text = answerHint, textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
                    } else {
                        Button(
                            onClick = showHint(viewModel),
                            modifier = Modifier
                                .height(40.dp)
                                .wrapContentWidth()
                                .align(Alignment.Center),  //avoid the oval shape
                            shape = RoundedCornerShape(15.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                            contentPadding = PaddingValues(0.dp),  //avoid the little icon
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.secondary)
                        ) {
                            Text(text = "See Answer", Modifier.padding(start = 24.dp, end = 24.dp))
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
private fun onRecordPressed(
    viewModel: AddEditQuestionViewModel
): () -> Unit {
    return {
        val isRecording = viewModel.recordingState.value

        // haptic feedback
        if (isRecording) {
            viewModel.stopRecording()
        } else {
            viewModel.startRecording()
        }
    }
}

private fun showAnswers(
    navController: NavController,
    viewModel: AddEditQuestionViewModel
): () -> Unit {
    return {
        AnswersViewModel.questionId = viewModel.currentQuestionId ?: -1
        val title: String = viewModel.questionTitle.value.text
        navController.navigate(Screen.AnswersScreen.route + "?questionTitle=${title}")
    }
}

private fun showHint(
    viewModel: AddEditQuestionViewModel
): () -> Unit {
    return {
        viewModel.onEvent(AddEditQuestionEvent.ShowHint)
    }
}

private fun shuffle(
    navController: NavController
): () -> Unit {
    return {
        navController.popBackStack()
    }
}


/*
*  Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Question.questionColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.questionColor.value == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    questionBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditQuestionEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
*/