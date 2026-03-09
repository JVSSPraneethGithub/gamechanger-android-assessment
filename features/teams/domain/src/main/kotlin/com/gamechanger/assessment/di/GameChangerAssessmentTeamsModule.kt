package com.gamechanger.assessment.di

import com.gamechanger.assessment.api.GameChangerAssessmentApi
import com.gamechanger.assessment.database.GameChangerAssessmentDatabase
import com.gamechanger.assessment.repository.GameChangerAssessmentTeamsRepository
import com.gamechanger.assessment.repository.IGameChangerAssessmentTeamsRepository
import com.gamechanger.assessment.usecase.GameChangerAssessmentAllTeamsUseCase
import com.gamechanger.assessment.usecase.GameChangerAssessmentTeamDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Teams-module Dependency-framework.
 */
@Module
@InstallIn(ViewModelComponent::class)
class GameChangerAssessmentTeamsModule {
    @Provides
    internal fun providesTeamsRepository(
        api: GameChangerAssessmentApi,
        database: GameChangerAssessmentDatabase
    ): IGameChangerAssessmentTeamsRepository =
        GameChangerAssessmentTeamsRepository(api, database)

    @Provides
    fun providesTeamsUseCase(
        repository: IGameChangerAssessmentTeamsRepository
    ) = GameChangerAssessmentAllTeamsUseCase(repository)

    @Provides
    fun providesTeamDetailsUseCase(
        repository: IGameChangerAssessmentTeamsRepository
    ) = GameChangerAssessmentTeamDetailsUseCase(repository)
}