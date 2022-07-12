package io.interviewmate.behavioural.answers.presentation.answers

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.interviewmate.behavioural.answers.domain.model.Answer
import io.interviewmate.behavioural.answers.presentation.components.AnswerItem
import io.interviewmate.behavioural.questions.presentation.questions.components.OrderSection
import io.interviewmate.behavioural.questions.presentation.questions.components.QuestionItem
import io.interviewmate.behavioural.questions.presentation.util.Screen
import kotlinx.coroutines.launch
import java.io.File

@ExperimentalAnimationApi
@Composable
fun AnswersScreen(
    navController: NavController,
    viewModel: AnswersViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val title = viewModel.questionTitle ?: "Answers"

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Spacer(modifier = Modifier.height(18.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.answers) { answer ->
                    AnswerItem(
                        answer = answer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                viewModel.initMediaPlayer(Uri.fromFile(
                                    File(answer.path ?: "")
                                ))
                            },
                        onDeleteClick = {
                            viewModel.onEvent(AnswersEvent.DeleteAnswer(answer))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Answer deleted",
                                    actionLabel = "Undo"
                                )
                                if(result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(AnswersEvent.RestoreAnswer)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}