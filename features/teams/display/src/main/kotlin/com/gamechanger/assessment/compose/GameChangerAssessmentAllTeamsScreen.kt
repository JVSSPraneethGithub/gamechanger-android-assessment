package com.gamechanger.assessment.compose

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.gamechanger.assessment.compose.widgets.GameChangerAssessmentStickyHeader
import com.gamechanger.assessment.compose.widgets.GameChangerAssessmentStickyHeaderPreview
import com.gamechanger.assessment.features.teams.R
import com.gamechanger.assessment.parcelables.GameChangerAssessmentTeam
import com.gamechangers.assessment.compose.theme.Space_16DP
import com.gamechangers.assessment.compose.theme.Space_4DP
import com.gamechangers.assessment.compose.theme.Space_8DP
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.AllTeams
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.TeamsError
import com.gamechanger.assessment.viewmodel.IGameChangerAssessmentTeamsViewModel
import com.gamechangers.assessment.compose.theme.No_Space
import kotlinx.coroutines.launch

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Feature-Teams Screen Composable.
 */
@Composable
internal fun GameChangerAssessmentAllTeamsScreen(
    viewModel: IGameChangerAssessmentTeamsViewModel,
    lazyGridState: LazyGridState,
    onItemClicked: (Array<String>) -> Unit
) {
    var errorId by rememberSaveable { mutableIntStateOf(-1) }
    var leagueName by rememberSaveable { mutableStateOf("") }
    var teams by rememberSaveable {
        mutableStateOf<List<GameChangerAssessmentTeam>?>(null)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState) {
        when(val currState = uiState) {
            is AllTeams -> {
                errorId = -1
                teams = currState.teams
                if (leagueName != currState.leagueName) {
                    lazyGridState.scrollToItem(0)
                }
                leagueName = currState.leagueName
            }
            is TeamsError -> {
                errorId = currState.errorId
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (errorId == -1) {
            teams?.also { data ->
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = lazyGridState,
                    contentPadding = PaddingValues(Space_8DP),
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
                            stringResource(
                                R.string.screen_title_all_teams,
                                leagueName
                            )
                        )
                    }
                    items(
                        count = data.size,
                        key = { data[it].id }
                    ) {index ->
                        val team = data[index]
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
                                    onClick = {
                                        onItemClicked(
                                            arrayOf(
                                                team.id,
                                                team.name,
                                                team.league
                                            )
                                        )
                                    }
                                ),
                            shape = RoundedCornerShape(Space_8DP),
                            elevation = CardDefaults
                                .elevatedCardElevation(Space_4DP),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceBright,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.2f)
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(Space_4DP)
                                            .align(Alignment.Center),
                                        painter = rememberAsyncImagePainter(
                                            model = ImageRequest.Builder(
                                                LocalContext.current
                                            ).data(team.badge).build()
                                        ),
                                        contentDescription = stringResource(
                                            R.string.team_details_badge_talkback,
                                            team.name
                                        ),
                                        contentScale = ContentScale.FillWidth
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(Space_8DP),
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(fontWeight = FontWeight.Bold)
                                            ) {
                                                append(
                                                    stringResource(
                                                        R.string.team_details_name_label
                                                    )
                                                )
                                            }
                                            append(" ${team.name}")
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Start,
                                        overflow = TextOverflow.Ellipsis
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
                                            .fillMaxWidth()
                                            .padding(Space_8DP),
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(fontWeight = FontWeight.Bold)
                                            ) {
                                                append(
                                                    stringResource(
                                                        R.string.team_details_sport_label
                                                    )
                                                )
                                            }
                                            append(" ${team.sport}")
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Start,
                                        overflow = TextOverflow.Ellipsis
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
                                            .fillMaxWidth()
                                            .padding(Space_8DP),
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(fontWeight = FontWeight.Bold)
                                            ) {
                                                append(
                                                    stringResource(
                                                        R.string.team_details_country_label
                                                    )
                                                )
                                            }
                                            append(" ${team.country}")
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Start,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            } ?: CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("GameChangerAssessmentAllTeamsScreenLoading")
            )
        } else {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space_16DP)
                    .align(Alignment.Center),
                text = stringResource(errorId),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun GameChangerAssessmentAllTeamsScreenPreview() {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Space_8DP),
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append(
                                        stringResource(
                                            R.string.team_details_name_label
                                        )
                                    )
                                }
                                append(" Team ${counter + 1}")
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis
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
                                .fillMaxWidth()
                                .padding(Space_8DP),
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append(
                                        stringResource(
                                            R.string.team_details_sport_label
                                        )
                                    )
                                }
                                append(" Sport")
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis
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
                                .fillMaxWidth()
                                .padding(Space_8DP),
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append(
                                        stringResource(
                                            R.string.team_details_country_label
                                        )
                                    )
                                }
                                append(" Country")
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}