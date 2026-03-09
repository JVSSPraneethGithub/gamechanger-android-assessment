package com.gamechanger.assessment.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.createGraph
import com.gamechanger.assessment.R
import com.gamechanger.assessment.compose.nav.GameChangerAssessmentNav

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Main Screen Composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GameChangerAssessmentNavHostScreen(
    navController: NavHostController
) {
    var currentNav by rememberSaveable { mutableStateOf<String?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryFlow
        .collectAsStateWithLifecycle(
            navController.currentBackStackEntry
        )
    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.destination?.route?.also {
            currentNav = it
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                title = {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = stringResource(R.string.app_name)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    if(currentNav?.contains("/") == true) {
                        IconButton(
                            modifier = Modifier
                                .wrapContentSize()
                                .testTag(
                                    stringResource(
                                        com.gamechanger.assessment.coreui.R.string.back
                                    )
                                ),
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(
                                    com.gamechanger.assessment.coreui.R.string.back
                                )
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            navController = navController,
            graph = navController.createGraph(
                startDestination = GameChangerAssessmentNav.LEAGUES.name,
            ) {
                GameChangerAssessmentLeaguesNavBuilder(
                    navController,
                    rememberLazyGridState()
                )
                GameChangerAssessmentTeamsNavBuilder(
                    navController,
                    rememberLazyGridState()
                )
            }
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    GameChangersAssessmentLeaguesScreenPreview()
}