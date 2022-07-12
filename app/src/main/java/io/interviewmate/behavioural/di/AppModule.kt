package io.interviewmate.behavioural.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.interviewmate.behavioural.IMDatabase
import io.interviewmate.behavioural.answers.data.repository.AnswerRepositoryImpl
import io.interviewmate.behavioural.answers.domain.repository.AnswerRepository
import io.interviewmate.behavioural.answers.domain.use_case.*
import io.interviewmate.behavioural.questions.data.repository.QuestionRepositoryImpl
import io.interviewmate.behavioural.questions.domain.repository.QuestionRepository
import io.interviewmate.behavioural.questions.domain.use_case.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): IMDatabase {
        return Room.databaseBuilder(
            app,
            IMDatabase::class.java,
            IMDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(db: IMDatabase): QuestionRepository {
        return QuestionRepositoryImpl(db.questionDao)
    }

    @Provides
    @Singleton
    fun provideQuestionUseCases(repository: QuestionRepository): QuestionUseCases {
        return QuestionUseCases(
            getQuestions = GetQuestions(repository),
            getQuestion = GetQuestion(repository),
            deleteQuestion = DeleteQuestion(repository),
            addQuestion = AddQuestion(repository)
        )
    }

    @Provides
    @Singleton
    fun provideAnswerRepository(db: IMDatabase): AnswerRepository {
        return AnswerRepositoryImpl(db.answerDao)
    }

    @Provides
    @Singleton
    fun provideAnswerUseCases(repository: AnswerRepository): AnswerUseCases {
        return AnswerUseCases(
            getAnswer = GetAnswer(repository),
            getAnswers = GetAnswers(repository),
            getAnswersFor = GetAnswersFor(repository),
            deleteAnswer = DeleteAnswer(repository),
            addAnswer = AddAnswer(repository)
        )
    }

    @Provides
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }
}