package com.gamechanger.assessment.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING
import com.gamechanger.assessment.database.GameChangerAssessmentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Room-Database Dependency-framework Singleton-Module.
 */
@Module
@InstallIn(SingletonComponent::class)
class GameChangerAssessmentPersistenceModule {
    @Provides
    @Singleton
    fun providesGameChangerAssessmentDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
      context,
        GameChangerAssessmentDatabase::class.java,
        "gamechanger_assessment_database.db"
    ).createFromAsset("db/gamechanger_assessment_database.db")
        .setJournalMode(WRITE_AHEAD_LOGGING)
        .build()
}