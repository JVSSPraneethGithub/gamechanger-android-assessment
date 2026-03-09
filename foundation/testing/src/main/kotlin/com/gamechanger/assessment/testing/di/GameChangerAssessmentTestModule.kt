package com.gamechanger.assessment.testing.di

import com.gamechanger.assessment.api.GameChangerAssessmentApi
import com.gamechanger.assessment.database.GameChangerAssessmentDatabase
import com.gamechanger.assessment.di.GameChangerAssessmentNetworkModule
import com.gamechanger.assessment.di.GameChangerAssessmentPersistenceModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Ui-Testing mockk Dependency-framework.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [GameChangerAssessmentNetworkModule::class,
        GameChangerAssessmentPersistenceModule::class]
)
class GameChangerAssessmentTestModule {

    @Provides
    @Singleton
    fun providesOkHttpClient() = mockk<OkHttpClient>(relaxed = true)

    @Provides
    @Singleton
    fun providesGameChangerAssessmentApi() =
        mockk<GameChangerAssessmentApi>(relaxed = true)

    @Provides
    @Singleton
    fun providesGameChangerAssessmentDatabase() =
        mockk<GameChangerAssessmentDatabase>(relaxed = true)
}