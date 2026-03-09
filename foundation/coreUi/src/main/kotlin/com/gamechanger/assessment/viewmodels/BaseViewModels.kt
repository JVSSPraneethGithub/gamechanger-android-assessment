package com.gamechanger.assessment.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Core Jetpack Architecture ViewModel implementation for MVVM.
 */
interface GameChangerAssessmentUiState

interface IBaseViewModel<T: GameChangerAssessmentUiState> {
    val uiState: StateFlow<T>

    fun showProgressIndicator(progressState: T)
}

abstract class GameChangerAssessmentBaseViewModel<T: GameChangerAssessmentUiState>(
    initialState: T
) : ViewModel(), IBaseViewModel<T> {
    protected val _uiState = MutableStateFlow(initialState)
    override val uiState: StateFlow<T> = _uiState.asStateFlow()

    override fun showProgressIndicator(progressState: T) {
        _uiState.tryEmit(progressState)
    }
}

