package com.gamechanger.assessment.viewmodel

import com.gamechanger.assessment.parcelables.GameChangerAssessmentTeam
import com.gamechanger.assessment.usecase.GameChangerAssessmentAllTeamsUseCase
import com.gamechanger.assessment.usecase.GameChangerAssessmentTeamDetailsUseCase
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.AllTeams
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.Loading
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.TeamDetails
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.TeamsError
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
 * Teams feature ViewModel Unit-Test.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GameChangerAssessmentTeamsViewModelTest {

    private lateinit var testViewModel: IGameChangerAssessmentTeamsViewModel
    private val uiStateList = mutableListOf<GameChangerAssessmentTeamsUiState>()
    private lateinit var job: Job

    private var testSuccessResponse = List(10) { index ->
            GameChangerAssessmentTeam(
                id = index.toString(),
                name = "Team $index",
                sport = "Sport",
                gender = "Gender",
                league = "League",
                stadium = "Stadium-$index",
                location = "Location: $index",
                country = "Country",
                badge = "",
                banner = "",
                description = "Description $index",
                website = "weblink-$index",
                facebook = "meta/facebook-$index",
                twitter = "twitter.com/$index",
                instagram = "",
                youtube = "youtube.com/$index--",
                locked = ""
            )
        }

    @MockK
    private lateinit var allTeamsUseCase: GameChangerAssessmentAllTeamsUseCase

    @MockK
    private lateinit var teamDetailsUseCase: GameChangerAssessmentTeamDetailsUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this)
        uiStateList.clear()

        testViewModel = GameChangerAssessmentTeamsViewModel(
            allTeamsUseCase,
            teamDetailsUseCase
        )

        job = TestScope().launch {
            testViewModel.uiState.collect {
                uiStateList.add(it)
            }
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
        uiStateList.clear()
    }

    @Test
    fun `test initialization`() = runTest {
        advanceUntilIdle()

        assertTrue(uiStateList.isNotEmpty())
        assert(uiStateList.first() is Loading)
        job.cancelAndJoin()
    }

    @Test
    fun `test allTeamsUseCase success`() = runTest {
        advanceUntilIdle()

        coEvery {
            allTeamsUseCase(any())
        } returns Result.success(
            testSuccessResponse
        )

        testViewModel.fetchAllTeams("League")
        advanceUntilIdle()

        coVerify(exactly = 1) { allTeamsUseCase("League") }
        assertTrue(uiStateList.isNotEmpty())
        assert(uiStateList.last() is AllTeams)
        job.cancelAndJoin()
    }

    @Test
    fun `test allTeamsUseCase failure`() = runTest {
        advanceUntilIdle()

        coEvery {
            allTeamsUseCase(any())
        } returns Result.failure(
            RuntimeException("Error")
        )

        testViewModel.fetchAllTeams("League")
        advanceUntilIdle()

        coVerify(exactly = 1) { allTeamsUseCase("League") }
        assertTrue(uiStateList.isNotEmpty())
        assert(uiStateList.last() is TeamsError)
        job.cancelAndJoin()
    }

    @Test
    fun `test teamDetailsUseCase success`() = runTest {
        advanceUntilIdle()

        coEvery {
            teamDetailsUseCase(any(), any(), any())
        } returns Result.success(
            testSuccessResponse.first()
        )

        testViewModel.fetchTeamDetails(
            "1",
            "Team 1",
            "League"
        )
        advanceUntilIdle()

        coVerify(exactly = 1) {
            teamDetailsUseCase("1", "Team 1", "League")
        }
        assertTrue(uiStateList.isNotEmpty())
        assert(uiStateList.last() is TeamDetails)
        job.cancelAndJoin()
    }

    @Test
    fun `test teamDetailsUseCase failure`() = runTest {
        advanceUntilIdle()

        coEvery {
            teamDetailsUseCase(any(), any(), any())
        } returns Result.failure(
            RuntimeException("Error")
        )

        testViewModel.fetchTeamDetails(
            "1",
            "Team 1",
            "League"
        )
        advanceUntilIdle()

        coVerify(exactly = 1) {
            teamDetailsUseCase("1", "Team 1", "League")
        }
        assertTrue(uiStateList.isNotEmpty())
        assert(uiStateList.last() is TeamsError)
        job.cancelAndJoin()
    }
}