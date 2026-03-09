package com.gamechanger.assessment.usecase

import com.gamechanger.assessment.repository.IGameChangerAssessmentTeamsRepository

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Team-Details Use-case.
 */
class GameChangerAssessmentTeamDetailsUseCase internal constructor(
    private val repository: IGameChangerAssessmentTeamsRepository
) {
    suspend operator fun invoke(
        id: String,
        teamName: String,
        leagueName: String
    ) = repository.fetchTeam(id, teamName, leagueName)
}