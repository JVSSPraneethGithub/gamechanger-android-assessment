package com.gamechanger.assessment.viewmodel

import com.gamechanger.assessment.parcelables.GameChangerAssessmentLeague
import com.gamechanger.assessment.usecase.GameChangerAssessmentLeaguesUseCase
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Completed.Failure
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Completed.Success
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Feature-Leagues ViewModel Unit-Test.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class GameChangerAssessmentLeaguesViewModelTest {

    private lateinit var testViewModel: IGameChangerAssessmentLeaguesViewModel
    private val uiStateList = mutableListOf<GameChangerAssessmentLeaguesUiState>()
    private lateinit var job: Job
    private val testSuccessResponse = List(5) {index ->
        GameChangerAssessmentLeague(
            id = index.toString(),
            name = "League $index",
            sport = "Sport"
        )
    }

    @MockK
    private lateinit var leaguesUseCase: GameChangerAssessmentLeaguesUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this)
        uiStateList.clear()
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
        uiStateList.clear()
    }

    @Test
    fun `test viewModel initialization`() = runTest {
        coEvery {
            leaguesUseCase()
        } returns Result.success(
            testSuccessResponse
        )

        testViewModel = GameChangerAssessmentLeaguesViewModel(
            leaguesUseCase
        )

        job = TestScope().launch {
            testViewModel.uiState.collect {
                uiStateList.add(it)
            }
        }
        advanceUntilIdle()

        coVerify(exactly = 1) { leaguesUseCase() }
        assertTrue(uiStateList.isNotEmpty())
        assert(uiStateList.last() is Success)
        job.cancelAndJoin()
    }

    @Test
    fun `test leagues useCase failure`() = runTest {
        coEvery {
            leaguesUseCase()
        } returns Result.failure(
            RuntimeException("Error")
        )

        testViewModel = GameChangerAssessmentLeaguesViewModel(
            leaguesUseCase
        )

        job = TestScope().launch {
            testViewModel.uiState.collect {
                uiStateList.add(it)
            }
        }
        advanceUntilIdle()

        coVerify(exactly = 1) { leaguesUseCase() }
        assertTrue(uiStateList.isNotEmpty())
        assert(uiStateList.last() is Failure)
        job.cancelAndJoin()
    }
}