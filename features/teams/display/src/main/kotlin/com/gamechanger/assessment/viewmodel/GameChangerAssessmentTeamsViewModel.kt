package com.gamechanger.assessment.viewmodel

import androidx.lifecycle.viewModelScope
import com.gamechanger.assessment.features.teams.R
import com.gamechanger.assessment.parcelables.GameChangerAssessmentTeam
import com.gamechanger.assessment.usecase.GameChangerAssessmentAllTeamsUseCase
import com.gamechanger.assessment.usecase.GameChangerAssessmentTeamDetailsUseCase
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.AllTeams
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.Loading
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.TeamsError
import com.gamechanger.assessment.viewmodels.GameChangerAssessmentBaseViewModel
import com.gamechanger.assessment.viewmodels.GameChangerAssessmentUiState
import com.gamechanger.assessment.viewmodels.IBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Teams feature Jetpack Architecture ViewModel implementation for MVVM.
 */
sealed interface GameChangerAssessmentTeamsUiState : GameChangerAssessmentUiState {
    data object Loading : GameChangerAssessmentTeamsUiState
    data class AllTeams(
        val leagueName: String,
        val teams: List<GameChangerAssessmentTeam>
    ) : GameChangerAssessmentTeamsUiState
    data class TeamDetails(val team: GameChangerAssessmentTeam) :
        GameChangerAssessmentTeamsUiState
    data class TeamsError(val errorId: Int) : GameChangerAssessmentTeamsUiState
}

interface IGameChangerAssessmentTeamsViewModel :
    IBaseViewModel<GameChangerAssessmentTeamsUiState> {
    fun fetchAllTeams(leagueName: String)
    fun fetchTeamDetails(
        teamId: String,
        teamName: String,
        leagueName: String
    )
}

@HiltViewModel
internal class GameChangerAssessmentTeamsViewModel @Inject constructor(
    private val allTeamsUseCase: GameChangerAssessmentAllTeamsUseCase,
    private val teamDetailsUseCase: GameChangerAssessmentTeamDetailsUseCase
): GameChangerAssessmentBaseViewModel<GameChangerAssessmentTeamsUiState>(
    Loading
), IGameChangerAssessmentTeamsViewModel {
    override fun fetchAllTeams(leagueName: String) {
        viewModelScope.launch {
            showProgressIndicator(Loading)
            allTeamsUseCase(leagueName)
                .fold(
                    onSuccess = { teamsList ->
                        _uiState.tryEmit(
                            AllTeams(
                                leagueName,
                                teamsList
                            ).takeIf {
                                teamsList.isNotEmpty()
                            } ?: TeamsError(R.string.no_teams_data)
                        )
                    },
                    onFailure = {
                        _uiState.tryEmit(
                            TeamsError(R.string.no_teams_data)
                        )
                    }
                )
        }
    }

    override fun fetchTeamDetails(
        teamId: String,
        teamName: String,
        leagueName: String
    ) {
        viewModelScope.launch {
            showProgressIndicator(Loading)
            teamDetailsUseCase(
                teamId,
                teamName,
                leagueName
            ).fold(
                onSuccess = {
                    _uiState.tryEmit(
                        GameChangerAssessmentTeamsUiState.TeamDetails(it)
                    )
                },
                onFailure = {
                    _uiState.tryEmit(
                        TeamsError(R.string.no_team_details_data)
                    )
                }
            )
        }
    }
}
