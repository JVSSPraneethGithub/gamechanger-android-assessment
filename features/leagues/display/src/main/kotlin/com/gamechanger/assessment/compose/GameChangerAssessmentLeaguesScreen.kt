package com.gamechanger.assessment.compose

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.HtmlCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gamechanger.assessment.compose.widgets.GameChangerAssessmentStickyHeader
import com.gamechanger.assessment.compose.widgets.GameChangerAssessmentStickyHeaderPreview
import com.gamechanger.assessment.features.leagues.R
import com.gamechanger.assessment.parcelables.GameChangerAssessmentLeague
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Completed.Failure
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentLeaguesUiState.Completed.Success
import com.gamechanger.assessment.viewmodel.IGameChangerAssessmentLeaguesViewModel
import com.gamechangers.assessment.compose.theme.No_Space
import com.gamechangers.assessment.compose.theme.Space_16DP
import com.gamechangers.assessment.compose.theme.Space_4DP
import com.gamechangers.assessment.compose.theme.Space_8DP

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Feature-Leagues Screen Composable.
 */
@Composable
internal fun GameChangerAssessmentLeaguesScreen(
    viewModel: IGameChangerAssessmentLeaguesViewModel,
    lazyGridState: LazyGridState,
    onItemClicked: (String) -> Unit
) {
    var errorTextId by rememberSaveable { mutableIntStateOf(-1) }
    var leagues by rememberSaveable {
        mutableStateOf<List<GameChangerAssessmentLeague>?>(null)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState) {
        errorTextId = -1

        when(val currState = uiState) {
            is Success -> {
                leagues = currState.items
            }
            is Failure -> {
                errorTextId = currState.errorId
            }
            else -> {
                leagues = null
                errorTextId = -1
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (errorTextId == -1) {
            leagues?.also { data ->
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = lazyGridState,
                    columns = GridCells.Fixed(
                        1.takeIf {
                            LocalConfiguration.current.orientation == ORIENTATION_PORTRAIT
                        } ?: 2
                    ),
                    verticalArrangement = Arrangement.spacedBy(Space_16DP),
                    horizontalArrangement = Arrangement.spacedBy(Space_16DP)
                ) {
                    stickyHeader {
                        GameChangerAssessmentStickyHeader(
                            stringResource(R.string.screen_title)
                        )
                    }
                    items(
                        count = data.size,
                        key = { data[it].id }
                    ) { index ->
                        val leagueItem = data[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = Space_8DP.takeIf {
                                        LocalConfiguration.current.orientation ==
                                                ORIENTATION_PORTRAIT
                                    } ?: No_Space
                                )
                                .clickable(
                                    onClick = { onItemClicked(leagueItem.name) }
                                ),
                            shape = RoundedCornerShape(Space_8DP),
                            elevation = CardDefaults
                                .elevatedCardElevation(Space_4DP),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceBright,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(Space_8DP),
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(fontWeight = FontWeight.Bold)
                                        ) {
                                            append(
                                                stringResource(R.string.league_name_label)
                                            )
                                        }
                                        append(" ${leagueItem.name}")
                                    },
                                    textAlign = TextAlign.Start,
                                    overflow = TextOverflow.Visible
                                )
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = Space_4DP),
                                    thickness = DividerDefaults.Thickness,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(Space_8DP),
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(fontWeight = FontWeight.Bold)
                                        ) {
                                            append(
                                                stringResource(R.string.league_sport_label)
                                            )
                                        }
                                        append(" ${leagueItem.sport}")
                                    },
                                    textAlign = TextAlign.Start,
                                    overflow = TextOverflow.Visible
                                )
                            }
                        }
                    }
                }
            } ?: CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("GameChangerAssessmentLeaguesScreenLoading")
            )
        } else {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space_16DP)
                    .align(Alignment.Center),
                text = stringResource(errorTextId),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun GameChangersAssessmentLeaguesScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Space_16DP)
    ) {
        GameChangerAssessmentStickyHeaderPreview()
        repeat(5) { counter ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Space_8DP.takeIf {
                            LocalConfiguration.current.orientation ==
                                    ORIENTATION_PORTRAIT
                        } ?: No_Space
                    ),
                shape = RoundedCornerShape(Space_8DP),
                elevation = CardDefaults
                    .elevatedCardElevation(Space_4DP),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(Space_8DP),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Bold)
                            ) {
                                append(
                                    stringResource(R.string.league_name_label)
                                )
                            }
                            append(" League ${counter + 1}")
                        },
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Visible
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Space_4DP),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(Space_8DP),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Bold)
                            ) {
                                append(
                                    stringResource(R.string.league_sport_label)
                                )
                            }
                            append(" Sport ${counter + 1}")
                        },
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Visible
                    )
                }
            }
        }
    }
}