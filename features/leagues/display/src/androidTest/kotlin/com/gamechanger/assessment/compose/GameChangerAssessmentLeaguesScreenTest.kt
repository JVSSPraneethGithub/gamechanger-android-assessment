package com.gamechanger.assessment.compose

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.gamechanger.assessment.features.leagues.R
import com.gamechanger.assessment.parcelables.GameChangerAssessmentLeague
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Completed.Failure
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Completed.Success
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Progress
import com.gamechanger.assessment.viewmodel.IGameChangerAssessmentLeaguesViewModel
import com.gamechangers.assessment.compose.theme.GameChangerAssessmentTheme
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Feature-Leagues Ui-Test.
 */
@SmallTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class GameChangerAssessmentLeaguesScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testUiState = MutableStateFlow<GameChangerAssessmentLeaguesUiState>(
        Progress
    )

    private val testSuccessData = List(5) { index ->
        GameChangerAssessmentLeague(
            id = index.toString(),
            name = "League $index",
            sport = "Sport $index"
        )
    }

    @BindValue
    val testViewModel = mockk<IGameChangerAssessmentLeaguesViewModel>(relaxed = true) {
        every { uiState } returns testUiState
        every { showProgressIndicator(any()) } just Runs
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        composeTestRule.activityRule.scenario.close()
    }

    @Test
    fun test_success_portrait() {
        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentLeaguesScreen(
                    viewModel = testViewModel,
                    lazyGridState = rememberLazyGridState()
                ) {}
            }
        }

        composeTestRule
            .onNodeWithTag("GameChangerAssessmentLeaguesScreenLoading")
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = Success(
            items = testSuccessData
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("GameChangerAssessmentLeaguesScreenLoading")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_leagues_data
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.screen_title
                )
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        repeat(testSuccessData.size) { counter ->
            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.league_name_label
                    ) + " ${testSuccessData[counter].name}"
                )
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.league_sport_label
                    ) + " ${testSuccessData[counter].sport}"
                )
                .assertIsDisplayed()
        }
    }

    @Test
    fun test_failure_portrait() {
        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentLeaguesScreen(
                    viewModel = testViewModel,
                    lazyGridState = rememberLazyGridState()
                ) {}
            }
        }

        composeTestRule
            .onNodeWithTag("GameChangerAssessmentLeaguesScreenLoading")
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = Failure(
            errorId = R.string.no_leagues_data
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("GameChangerAssessmentLeaguesScreenLoading")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_leagues_data
                )
            )
            .assertIsDisplayed()

        repeat(testSuccessData.size) { counter ->
            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.league_name_label
                    ) + " ${testSuccessData[counter].name}"
                )
                .assertDoesNotExist()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.league_sport_label
                    ) + " ${testSuccessData[counter].sport}"
                )
                .assertDoesNotExist()
        }
    }

    @Test
    fun test_success_landscape() {
        composeTestRule.activity.requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
        composeTestRule.waitForIdle()

        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentLeaguesScreen(
                    viewModel = testViewModel,
                    lazyGridState = rememberLazyGridState()
                ) {}
            }
        }

        composeTestRule
            .onNodeWithTag("GameChangerAssessmentLeaguesScreenLoading")
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = Success(
            items = testSuccessData
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("GameChangerAssessmentLeaguesScreenLoading")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_leagues_data
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.screen_title
                )
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        repeat(testSuccessData.size) { counter ->
            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.league_name_label
                    ) + " ${testSuccessData[counter].name}"
                )
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.league_sport_label
                    ) + " ${testSuccessData[counter].sport}"
                )
                .assertIsDisplayed()
        }
    }

    @Test
    fun test_failure_landscape() {
        composeTestRule.activity.requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
        composeTestRule.waitForIdle()

        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentLeaguesScreen(
                    viewModel = testViewModel,
                    lazyGridState = rememberLazyGridState()
                ) {}
            }
        }

        composeTestRule
            .onNodeWithTag("GameChangerAssessmentLeaguesScreenLoading")
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = Failure(
            errorId = R.string.no_leagues_data
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("GameChangerAssessmentLeaguesScreenLoading")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_leagues_data
                )
            )
            .assertIsDisplayed()

        repeat(testSuccessData.size) { counter ->
            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.league_name_label
                    ) + " ${testSuccessData[counter].name}"
                )
                .assertDoesNotExist()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.league_sport_label
                    ) + " ${testSuccessData[counter].sport}"
                )
                .assertDoesNotExist()
        }
    }
}