package com.gamechanger.assessment.viewmodel

import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.Companion.PRIVATE
import androidx.lifecycle.viewModelScope
import com.gamechanger.assessment.features.leagues.R
import com.gamechanger.assessment.parcelables.GameChangerAssessmentLeague
import com.gamechanger.assessment.usecase.GameChangerAssessmentLeaguesUseCase
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Completed.Failure
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Completed.Success
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Progress
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
 * Feature-Leagues Jetpack Architecture ViewModel implementation for MVVM.
 */
sealed interface GameChangerAssessmentLeaguesUiState : GameChangerAssessmentUiState {
    data object Progress : GameChangerAssessmentLeaguesUiState

    sealed interface Completed : GameChangerAssessmentLeaguesUiState {
        data class Success(
            val items: List<GameChangerAssessmentLeague>
        ): Completed

        data class Failure(
            @param:StringRes val errorId: Int
        ) : Completed
    }
}

interface IGameChangerAssessmentLeaguesViewModel :
    IBaseViewModel<GameChangerAssessmentLeaguesUiState>

@HiltViewModel
internal class GameChangerAssessmentLeaguesViewModel @Inject constructor(
    private val leaguesUseCase: GameChangerAssessmentLeaguesUseCase
) : GameChangerAssessmentBaseViewModel<GameChangerAssessmentLeaguesUiState>(
    Progress
), IGameChangerAssessmentLeaguesViewModel {
    init {
        fetchAllLeagues()
    }

    private fun fetchAllLeagues() {
        viewModelScope.launch {
            showProgressIndicator(Progress)
            leaguesUseCase()
                .fold(
                    onSuccess = { leagueList ->
                        _uiState.tryEmit(
                            Success(leagueList)
                                .takeIf { leagueList.isNotEmpty() }
                                ?: Failure(
                                    R.string.no_leagues_data
                                )
                        )
                    },
                    onFailure = {
                        _uiState.tryEmit(
                            Failure(
                                R.string.no_leagues_data
                            )
                        )
                    }
                )
        }
    }
}