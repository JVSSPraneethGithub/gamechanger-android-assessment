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
import coil3.annotation.DelicateCoilApi
import com.gamechanger.assessment.features.teams.R
import com.gamechanger.assessment.parcelables.GameChangerAssessmentTeam
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.AllTeams
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.Loading
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.TeamsError
import com.gamechanger.assessment.viewmodel.IGameChangerAssessmentTeamsViewModel
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
 * Feature-Teams Ui-Test.
 */
@OptIn(DelicateCoilApi::class)
@SmallTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class GameChangerAssessmentAllTeamsScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testUiState = MutableStateFlow<GameChangerAssessmentTeamsUiState>(
        Loading
    )

    private val testSuccessData = List(5){ index ->
        GameChangerAssessmentTeam(
            id = index.toString(),
            name = "Team $index",
            sport = "Sport-$index",
            gender = "Gender",
            league = "League",
            stadium = "Stadium $index",
            location = "Location $index",
            country = "Country $index",
            banner = "https://www.banner.com/$index",
            badge = "https://www.badge.com/$index",
            description = "Description $index",
            website = "https://www.google.com",
            facebook = "https://www.google.com",
            twitter = "https://www.google.com",
            instagram = "https://www.google.com",
            youtube = "https://www.google.com",
            locked = "false"
        )
    }

    @BindValue
    val testViewModel = mockk<IGameChangerAssessmentTeamsViewModel>(relaxed = true) {
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
                GameChangerAssessmentAllTeamsScreen(
                    viewModel = testViewModel,
                    lazyGridState = rememberLazyGridState()
                ) { }
            }
        }

        composeTestRule
            .onNodeWithTag(
        "GameChangerAssessmentAllTeamsScreenLoading"
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = AllTeams(
            leagueName = "League",
            teams = testSuccessData
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentAllTeamsScreenLoading"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_teams_data
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.screen_title_all_teams,
                    "League"
                )
            )
            .assertIsDisplayed()

        repeat(testSuccessData.size) { counter ->
            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_name_label
                    ) + " ${testSuccessData[counter].name}"
                )
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_sport_label
                    ) + " ${testSuccessData[counter].sport}"
                )
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_country_label
                    ) + " ${testSuccessData[counter].country}"
                )
                .assertIsDisplayed()
        }
    }

    @Test
    fun test_success_landscape() {
        composeTestRule.activity.requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
        composeTestRule.waitForIdle()

        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentAllTeamsScreen(
                    viewModel = testViewModel,
                    lazyGridState = rememberLazyGridState()
                ) { }
            }
        }

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentAllTeamsScreenLoading"
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = AllTeams(
            leagueName = "League",
            teams = testSuccessData
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentAllTeamsScreenLoading"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_teams_data
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.screen_title_all_teams,
                    "League"
                )
            )
            .assertIsDisplayed()

        repeat(testSuccessData.size) { counter ->
            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_name_label
                    ) + " ${testSuccessData[counter].name}"
                )
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_sport_label
                    ) + " ${testSuccessData[counter].sport}"
                )
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_country_label
                    ) + " ${testSuccessData[counter].country}"
                )
                .assertIsDisplayed()
        }
    }

    @Test
    fun test_failure_portrait() {
        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentAllTeamsScreen(
                    viewModel = testViewModel,
                    lazyGridState = rememberLazyGridState()
                ) { }
            }
        }

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentAllTeamsScreenLoading"
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = TeamsError(
            R.string.no_teams_data
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentAllTeamsScreenLoading"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_teams_data
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.screen_title_all_teams,
                    "League"
                )
            )
            .assertDoesNotExist()

        repeat(testSuccessData.size) { counter ->
            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_name_label
                    ) + " ${testSuccessData[counter].name}"
                )
                .assertDoesNotExist()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_sport_label
                    ) + " ${testSuccessData[counter].sport}"
                )
                .assertDoesNotExist()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_country_label
                    ) + " ${testSuccessData[counter].country}"
                )
                .assertDoesNotExist()
        }
    }

    @Test
    fun test_failure_landscape() {
        composeTestRule.activity.requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
        composeTestRule.waitForIdle()

        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentAllTeamsScreen(
                    viewModel = testViewModel,
                    lazyGridState = rememberLazyGridState()
                ) { }
            }
        }

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentAllTeamsScreenLoading"
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = TeamsError(
            R.string.no_teams_data
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentAllTeamsScreenLoading"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_teams_data
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.screen_title_all_teams,
                    "League"
                )
            )
            .assertDoesNotExist()

        repeat(testSuccessData.size) { counter ->
            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_name_label
                    ) + " ${testSuccessData[counter].name}"
                )
                .assertDoesNotExist()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_sport_label
                    ) + " ${testSuccessData[counter].sport}"
                )
                .assertDoesNotExist()

            composeTestRule
                .onNodeWithText(
                    composeTestRule.activity.getString(
                        R.string.team_details_country_label
                    ) + " ${testSuccessData[counter].country}"
                )
                .assertDoesNotExist()
        }
    }
}