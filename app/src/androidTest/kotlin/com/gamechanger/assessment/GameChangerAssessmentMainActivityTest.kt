package com.gamechanger.assessment

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.gamechanger.assessment.compose.GameChangerAssessmentNavHostScreen
import com.gamechanger.assessment.compose.nav.GameChangerAssessmentNav
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Progress
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.Loading
import com.gamechanger.assessment.viewmodel.IGameChangerAssessmentLeaguesViewModel
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
 * GameChangerAssessmentMainActivity Ui-Test.
 */
@SmallTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class GameChangerAssessmentMainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<GameChangerAssessmentMainActivity>()

    private lateinit var testNavController: NavHostController

    @BindValue
    val testLeaguesViewModel = mockk<IGameChangerAssessmentLeaguesViewModel> {
        every { uiState } returns MutableStateFlow<GameChangerAssessmentLeaguesUiState>(
            Progress
        )
        every { showProgressIndicator(any()) } just Runs
    }

    @BindValue
    val testTeamsViewModel = mockk<IGameChangerAssessmentTeamsViewModel> {
        every { uiState } returns MutableStateFlow<GameChangerAssessmentTeamsUiState>(
            Loading
        )
        every { showProgressIndicator(any()) } just Runs
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this)

        testNavController = TestNavHostController(composeTestRule.activity)
        testNavController.navigatorProvider.addNavigator(
            ComposeNavigator()
        )

        composeTestRule.activity.setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentNavHostScreen(
                    navController = testNavController
                )
            }
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        composeTestRule.activityRule.scenario.close()
    }

    @Test
    fun test_navigation() {
        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.app_name
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    com.gamechanger.assessment.coreui.R.string.back
                )
            )
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    com.gamechanger.assessment.coreui.R.string.back
                )
            )
            .assertDoesNotExist()

        composeTestRule.runOnUiThread {
            testNavController.navigate(GameChangerAssessmentNav.TEAMS.asRouteAtIndex(0))
        }
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.app_name
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    com.gamechanger.assessment.coreui.R.string.back
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    com.gamechanger.assessment.coreui.R.string.back
                )
            )
            .assertIsDisplayed()
            .assert(hasClickAction())

        composeTestRule.runOnUiThread {
            testNavController.navigate(GameChangerAssessmentNav.TEAMS.asRouteAtIndex(1))
        }
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.app_name
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    com.gamechanger.assessment.coreui.R.string.back
                )
            )
            .assertIsDisplayed()


        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    com.gamechanger.assessment.coreui.R.string.back
                )
            )
            .assertIsDisplayed()
            .assert(hasClickAction())
            .performClick()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    com.gamechanger.assessment.coreui.R.string.back
                )
            )
            .assertIsDisplayed()
            .assert(hasClickAction())
            .performClick()

        composeTestRule
            .onNodeWithTag(
                composeTestRule.activity.getString(
                    com.gamechanger.assessment.coreui.R.string.back
                )
            )
            .assertDoesNotExist()
    }
}