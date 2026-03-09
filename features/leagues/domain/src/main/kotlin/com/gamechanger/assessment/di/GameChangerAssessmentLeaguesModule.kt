package com.gamechanger.assessment.di

import com.gamechanger.assessment.api.GameChangerAssessmentApi
import com.gamechanger.assessment.database.GameChangerAssessmentDatabase
import com.gamechanger.assessment.repository.GameChangerAssessmentLeaguesRepository
import com.gamechanger.assessment.repository.IGameChangerAssessmentLeaguesRepository
import com.gamechanger.assessment.usecase.GameChangerAssessmentLeaguesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Leagues-module Dependency-framework.
 */
@Module
@InstallIn(ViewModelComponent::class)
class GameChangerAssessmentLeaguesModule {
    @Provides
    internal fun provideLeaguesRepository(
        api: GameChangerAssessmentApi,
        database: GameChangerAssessmentDatabase
    ): IGameChangerAssessmentLeaguesRepository =
        GameChangerAssessmentLeaguesRepository(api, database)

    @Provides
    fun provideLeaguesUseCase(
        repository: IGameChangerAssessmentLeaguesRepository
    ) = GameChangerAssessmentLeaguesUseCase(repository)
}