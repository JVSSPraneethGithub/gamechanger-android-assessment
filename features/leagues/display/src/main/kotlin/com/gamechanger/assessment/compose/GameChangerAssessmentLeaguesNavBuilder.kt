package com.gamechanger.assessment.compose

import android.net.Uri
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gamechanger.assessment.compose.nav.GameChangerAssessmentNav
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesViewModel

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Feature-Leagues Navigation-Builder.
 */
fun NavGraphBuilder.GameChangerAssessmentLeaguesNavBuilder(
    navController: NavHostController,
    gridState: LazyGridState
) = navigation(
    startDestination = GameChangerAssessmentNav.LEAGUES.asRouteAtIndex(0),
    route = GameChangerAssessmentNav.LEAGUES.name
) {
    composable(
        route = GameChangerAssessmentNav.LEAGUES.asRouteAtIndex(0)
    ) {
        GameChangerAssessmentLeaguesScreen(
            viewModel = hiltViewModel<GameChangerAssessmentLeaguesViewModel>(),
            lazyGridState = gridState
        ) { argValue ->
            navController.navigate(
                GameChangerAssessmentNav.TEAMS.asRouteAtIndex(0).let {
                    var link = it
                    GameChangerAssessmentNav.TEAMS.navArgsAtIndex(0).forEach { arg ->
                        link = link.replace(
                            "{$arg}",
                            Uri.encode(argValue)
                        )
                    }
                    link
                }
            )
        }
    }
}