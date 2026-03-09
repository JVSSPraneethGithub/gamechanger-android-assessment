package com.gamechanger.assessment.usecase

import com.gamechanger.assessment.repository.IGameChangerAssessmentTeamsRepository

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Teams Use-case.
 */
class GameChangerAssessmentAllTeamsUseCase internal constructor(
    private val repository: IGameChangerAssessmentTeamsRepository
) {
    suspend operator fun invoke(name: String) = repository.fetchAllTeamsForLeague(name)
}