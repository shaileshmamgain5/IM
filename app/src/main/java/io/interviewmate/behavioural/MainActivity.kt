package io.interviewmate.behavioural

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import io.interviewmate.behavioural.answers.presentation.answers.AnswersScreen
import io.interviewmate.behavioural.questions.presentation.add_edit_question.AddEditQuestionScreen
import io.interviewmate.behavioural.questions.presentation.add_edit_question.AddEditQuestionViewModel
import io.interviewmate.behavioural.questions.presentation.questions.QuestionsScreen
import io.interviewmate.behavioural.questions.presentation.questions.QuestionsViewModel
import io.interviewmate.behavioural.questions.presentation.util.Screen
import io.interviewmate.behavioural.ui.theme.IMAppTheme
import io.interviewmate.behavioural.util.getSampleQuestions
import kotlinx.coroutines.Dispatchers.IO

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) Log.i("MainActivity", "GRANTED")
    }


    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IMAppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.QuestionsScreen.route
                    ) {
                        composable(route = Screen.QuestionsScreen.route) {
                            QuestionsScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditQuestionScreen.route +
                                    "?questionId={questionId}&questionColor={questionColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "questionId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "questionColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                            )
                        ) {
                            val color = it.arguments?.getInt("questionColor") ?: -1
                            AddEditQuestionScreen(
                                navController = navController
                            )
                        }
                        composable(
                            route = Screen.AnswersScreen.route +
                                    "?questionTitle={questionTitle}",
                            arguments = listOf(
                                navArgument(
                                    name = "questionTitle"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = "Answers"
                                }
                            )
                        ) {
                            AnswersScreen(navController = navController)
                        }
                    }
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    AddEditQuestionViewModel.folderPath = externalCacheDir?.absolutePath
                        ?: throw IllegalStateException("externalCacheDir is null!!")
                }
            }
        }

        checkAndAddText()
    }

    fun checkAndAddText() {
        val sharedPreference =  getSharedPreferences("IMPREF", Context.MODE_PRIVATE)
        if (sharedPreference.getBoolean("Initialized", false)) {
            //do nothing
        } else {
            QuestionsViewModel.questionJson = getSampleQuestions(this)
            var editor = sharedPreference.edit()
            editor.putBoolean("Initialized",true)
            editor.commit()
        }
    }
}
