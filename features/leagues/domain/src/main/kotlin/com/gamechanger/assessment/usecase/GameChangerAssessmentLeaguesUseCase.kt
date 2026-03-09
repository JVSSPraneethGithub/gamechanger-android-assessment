package com.gamechanger.assessment.usecase

import com.gamechanger.assessment.repository.IGameChangerAssessmentLeaguesRepository

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Leagues Use-case.
 */
class GameChangerAssessmentLeaguesUseCase internal constructor(
    private val repository: IGameChangerAssessmentLeaguesRepository
) {
    suspend operator fun invoke() = repository.fetchAllLeagues()
}