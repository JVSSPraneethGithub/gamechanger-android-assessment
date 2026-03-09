package com.gamechanger.assessment.compose

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.graphics.drawable.ColorDrawable
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.core.graphics.drawable.toDrawable
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.asImage
import coil3.test.FakeImageLoaderEngine
import com.gamechanger.assessment.features.teams.R
import com.gamechanger.assessment.parcelables.GameChangerAssessmentTeam
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.Loading
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.TeamDetails
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
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Team-Details Ui-Test.
 */
@OptIn(DelicateCoilApi::class)
@SmallTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class GameChangerAssessmentTeamDetailsScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testUiState = MutableStateFlow<GameChangerAssessmentTeamsUiState>(
        Loading
    )

    private val testSuccessData = GameChangerAssessmentTeam(
        id = "id",
        name = "Team",
        sport = "Sport",
        gender = "Gender",
        league = "League",
        stadium = "Stadium",
        location = "Location",
        country = "Country",
        banner = "https://www.banner.com/",
        badge = "https://www.badge.com/",
        description = "Description",
        website = "https://www.google.com",
        facebook = "https://www.google.com",
        twitter = "https://www.google.com",
        instagram = "https://www.google.com",
        youtube = "https://www.google.com",
        locked = "false"
    )

    @BindValue
    val testViewModel = mockk<IGameChangerAssessmentTeamsViewModel>(relaxed = true) {
        every { uiState } returns testUiState
        every { showProgressIndicator(any()) } just Runs
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this)
        composeTestRule.waitForIdle()
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
                GameChangerAssessmentTeamDetailsScreen(
                    viewModel = testViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentTeamDetailsScreenLoading"
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = TeamDetails(
            testSuccessData
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentTeamDetailsScreenLoading"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_team_details_data
                )
            )
            .assertDoesNotExist()

        assert_screen_elements_displayed()
    }

    @Test
    fun test_success_landscape() {
        composeTestRule.activity.requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
        composeTestRule.waitForIdle()

        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentTeamDetailsScreen(
                    viewModel = testViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentTeamDetailsScreenLoading"
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = TeamDetails(
            testSuccessData
        )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentTeamDetailsScreenLoading"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_team_details_data
                )
            )
            .assertDoesNotExist()

        assert_screen_elements_displayed()
    }

    @Test
    fun test_failure_portrait() {
        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentTeamDetailsScreen(
                    viewModel = testViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentTeamDetailsScreenLoading"
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = TeamsError(R.string.no_team_details_data)
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentTeamDetailsScreenLoading"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_team_details_data
                )
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        assert_screen_elements_absent()
    }

    @Test
    fun test_failure_landscape() {
        composeTestRule.activity.requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
        composeTestRule.waitForIdle()

        composeTestRule.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentTeamDetailsScreen(
                    viewModel = testViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentTeamDetailsScreenLoading"
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        testUiState.value = TeamsError(R.string.no_team_details_data)
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(
                "GameChangerAssessmentTeamDetailsScreenLoading"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.no_team_details_data
                )
            )
            .assertIsDisplayed()
            .assert(hasNoClickAction())

        assert_screen_elements_absent()
    }

    private fun assert_screen_elements_displayed() {
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.team_details_facebook_talkback,
                    testSuccessData.name
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    R.string.team_details_facebook_talkback,
                    testSuccessData.name
                )
            )
            .assert(hasClickAction())

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.team_details_instagram_talkback,
                    testSuccessData.name
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    R.string.team_details_instagram_talkback,
                    testSuccessData.name
                )
            )
            .assert(hasClickAction())

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.team_details_twitter_talkback,
                    testSuccessData.name
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    R.string.team_details_twitter_talkback,
                    testSuccessData.name
                )
            )
            .assert(hasClickAction())

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.team_details_youtube_talkback,
                    testSuccessData.name
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    R.string.team_details_youtube_talkback,
                    testSuccessData.name
                )
            )
            .assert(hasClickAction())

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_name_label
                ) + " ${testSuccessData.name}"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_weblink,
                    testSuccessData.name
                )
            )
            .assertIsDisplayed()
            .assert(hasClickAction())

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_league_label
                ) + " ${testSuccessData.league}"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_sport_label
                ) + " ${testSuccessData.sport}"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_gender_label
                ) + " ${testSuccessData.gender}"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_stadium_label
                ) + " ${testSuccessData.stadium}"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_location_label
                ) + " ${testSuccessData.location}"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_country_label
                ) + " ${testSuccessData.country}"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(testSuccessData.description)
            .assertIsDisplayed()
    }

    private fun assert_screen_elements_absent() {
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.team_details_facebook_talkback,
                    testSuccessData.name
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    R.string.team_details_facebook_talkback,
                    testSuccessData.name
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.team_details_instagram_talkback,
                    testSuccessData.name
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    R.string.team_details_instagram_talkback,
                    testSuccessData.name
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.team_details_twitter_talkback,
                    testSuccessData.name
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    R.string.team_details_twitter_talkback,
                    testSuccessData.name
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.team_details_youtube_talkback,
                    testSuccessData.name
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    R.string.team_details_youtube_talkback,
                    testSuccessData.name
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_name_label
                ) + " ${testSuccessData.name}"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_weblink,
                    testSuccessData.name
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_league_label
                ) + " ${testSuccessData.league}"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_sport_label
                ) + " ${testSuccessData.sport}"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_gender_label
                ) + " ${testSuccessData.gender}"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_stadium_label
                ) + " ${testSuccessData.stadium}"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_location_label
                ) + " ${testSuccessData.location}"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.team_details_country_label
                ) + " ${testSuccessData.country}"
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(testSuccessData.description)
            .assertDoesNotExist()
    }
}