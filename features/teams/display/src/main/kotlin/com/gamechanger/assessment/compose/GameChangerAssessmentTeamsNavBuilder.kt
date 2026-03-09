package com.gamechanger.assessment.compose

import android.net.Uri
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.gamechanger.assessment.compose.nav.GameChangerAssessmentNav
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsViewModel
import kotlinx.coroutines.launch

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Feature-Teams Navigation-Builder.
 */
fun NavGraphBuilder.GameChangerAssessmentTeamsNavBuilder(
    navController: NavHostController,
    gridState: LazyGridState
) = navigation(
    startDestination = GameChangerAssessmentNav.TEAMS.asRouteAtIndex(0),
    route = GameChangerAssessmentNav.TEAMS.name
) {
    composable(
        route = GameChangerAssessmentNav.TEAMS.asRouteAtIndex(0),
        arguments = GameChangerAssessmentNav.TEAMS.navArgsAtIndex(0).map {
            navArgument(it) {
                type = NavType.StringType
                nullable = false
            }
        }
    ) {entry ->
        GameChangerAssessmentAllTeamsScreen(
            hiltViewModel<GameChangerAssessmentTeamsViewModel>().also { vm ->
                vm.fetchAllTeams(
                    entry.arguments?.getString(
                        GameChangerAssessmentNav.TEAMS.navArgsAtIndex(0).first()
                    )?.let {
                        Uri.decode(it)
                    } ?: ""
                )
            },
            gridState
        ) { vars ->
            navController.navigate(
                GameChangerAssessmentNav.TEAMS.asRouteAtIndex(1).let {
                    var link = it
                    GameChangerAssessmentNav.TEAMS.navArgsAtIndex(1).forEachIndexed { index, arg ->
                        link = link.replace(
                            "{$arg}",
                            Uri.encode(vars[index])
                        )
                    }
                    link
                }
            )
        }
    }
    composable(
        route = GameChangerAssessmentNav.TEAMS.asRouteAtIndex(1),
        arguments = GameChangerAssessmentNav.TEAMS.navArgsAtIndex(1).map {
            navArgument(it) {
                type = NavType.StringType
                nullable = false
            }
        }
    ) {entry ->
        GameChangerAssessmentTeamDetailsScreen(
            hiltViewModel<GameChangerAssessmentTeamsViewModel>().also { vm ->
                vm.fetchTeamDetails(
                    teamId = entry.arguments?.getString(
                        GameChangerAssessmentNav.TEAMS.navArgsAtIndex(1).first()
                    )?.let {
                        Uri.decode(it)
                    } ?: "",
                    teamName = entry.arguments?.getString(
                        GameChangerAssessmentNav.TEAMS.navArgsAtIndex(1)[1]
                    )?.let {
                        Uri.decode(it)
                    } ?: "",
                    leagueName = entry.arguments?.getString(
                        GameChangerAssessmentNav.TEAMS.navArgsAtIndex(1).last()
                    )?.let {
                        Uri.decode(it)
                    } ?: ""
                )
            }
        )
    }
}