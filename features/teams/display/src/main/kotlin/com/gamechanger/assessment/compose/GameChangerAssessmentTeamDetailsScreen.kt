package com.gamechanger.assessment.compose

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.gamechanger.assessment.features.teams.R
import com.gamechanger.assessment.parcelables.GameChangerAssessmentTeam
import com.gamechanger.assessment.utils.launchWebLink
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.TeamDetails
import com.gamechanger.assessment.viewmodel.GameChangerAssessmentTeamsUiState.TeamsError
import com.gamechanger.assessment.viewmodel.IGameChangerAssessmentTeamsViewModel
import com.gamechangers.assessment.compose.theme.Space_12DP
import com.gamechangers.assessment.compose.theme.Space_16DP
import com.gamechangers.assessment.compose.theme.Space_24DP
import com.gamechangers.assessment.compose.theme.Space_40DP
import com.gamechangers.assessment.compose.theme.Space_8DP
import compose.icons.SimpleIcons
import compose.icons.simpleicons.Facebook
import compose.icons.simpleicons.Instagram
import compose.icons.simpleicons.Twitter
import compose.icons.simpleicons.Youtube
import kotlinx.coroutines.launch

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Team-details Screen Composable.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun GameChangerAssessmentTeamDetailsScreen(
    viewModel: IGameChangerAssessmentTeamsViewModel
) {
    var errorId by rememberSaveable { mutableIntStateOf(-1) }
    var team by rememberSaveable {
        mutableStateOf<GameChangerAssessmentTeam?>(null)
    }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState) {
        when(val currState = uiState) {
            is TeamsError -> {
                errorId = currState.errorId
            }
            is TeamDetails -> {
                errorId = -1
                team = currState.team
                coroutineScope.launch {
                    listState.scrollToItem(0)
                }
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (errorId == -1) {
            team?.also { data ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.surface
                                )
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth(
                                        1f.takeIf {
                                            LocalConfiguration.current.orientation ==
                                                    ORIENTATION_PORTRAIT
                                        } ?: 0.4f
                                    )
                                    .align(Alignment.TopCenter),
                                painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(team?.banner).build(),
                                    onSuccess = {
                                        coroutineScope.launch {
                                            listState.scrollToItem(0)
                                        }
                                    }
                                ),
                                contentDescription = stringResource(
                                    R.string.team_details_banner_talkback,
                                    data.name
                                ),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(
                                    1f.takeIf {
                                        LocalConfiguration.current.orientation ==
                                                ORIENTATION_PORTRAIT
                                    } ?: 0.6f
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(Space_8DP)
                                    .align(Alignment.CenterEnd),
                                horizontalArrangement = Arrangement.spacedBy(Space_8DP),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GameChangersAssessmentTeamSocialsButton(
                                    imageVector = SimpleIcons.Facebook,
                                    accessibilityTalkback = R.string.team_details_facebook_talkback,
                                    weblink = data.facebook,
                                    teamName = data.name
                                )
                                GameChangersAssessmentTeamSocialsButton(
                                    imageVector = SimpleIcons.Instagram,
                                    accessibilityTalkback = R.string.team_details_instagram_talkback,
                                    weblink = data.instagram,
                                    teamName = data.name
                                )
                                GameChangersAssessmentTeamSocialsButton(
                                    imageVector = SimpleIcons.Twitter,
                                    accessibilityTalkback = R.string.team_details_twitter_talkback,
                                    weblink = data.twitter,
                                    teamName = data.name
                                )
                                GameChangersAssessmentTeamSocialsButton(
                                    imageVector = SimpleIcons.Youtube,
                                    accessibilityTalkback = R.string.team_details_youtube_talkback,
                                    weblink = data.youtube,
                                    teamName = data.name
                                )
                            }
                        }
                    }
                    item {
                        GameChangersAssessmentTeamDetails(data)
                    }
                }
            }?: CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("GameChangerAssessmentTeamDetailsScreenLoading")
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

@Composable
private fun GameChangersAssessmentTeamSocialsButton(
    imageVector : ImageVector,
    @StringRes accessibilityTalkback: Int,
    weblink: String,
    teamName: String
) {
    val context = LocalContext.current
    IconButton(
        modifier = Modifier
            .wrapContentSize()
            .testTag(
                stringResource(
                    accessibilityTalkback,
                    teamName
                )
            ),
        onClick = {
            launchWebLink(
                context,
                weblink
            )
        }
    ) {
        Icon(
            modifier = Modifier
                .size(Space_24DP),
            imageVector = imageVector,
            contentDescription = stringResource(
                accessibilityTalkback,
                teamName
            )
        )
    }
}

@Composable
private fun GameChangersAssessmentTeamDetails(
    data: GameChangerAssessmentTeam
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(
                1f.takeIf {
                    LocalConfiguration.current.orientation == ORIENTATION_PORTRAIT
                } ?: 0.6f
            )
            .padding(horizontal = Space_8DP),
        verticalArrangement = Arrangement.spacedBy(Space_12DP)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) {
                    append(
                        stringResource(
                            R.string.team_details_name_label
                        )
                    )
                    append(" ${data.name}")
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start
        )
        val context = LocalContext.current
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        launchWebLink(
                            context,
                            data.website
                        )
                    }
                ),
            text = stringResource(R.string.team_details_weblink, data.name),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            ),
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) {
                    append(
                        stringResource(
                            R.string.team_details_league_label
                        )
                    )
                }
                append(" ${data.league}")
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
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
                append(" ${data.sport}")
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) {
                    append(
                        stringResource(
                            R.string.team_details_gender_label
                        )
                    )
                }
                append(" ${data.gender}")
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) {
                    append(
                        stringResource(
                            R.string.team_details_stadium_label
                        )
                    )
                }
                append(" ${data.stadium}")
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) {
                    append(
                        stringResource(
                            R.string.team_details_location_label
                        )
                    )
                }
                append(" ${data.location}")
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
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
                append(" ${data.country}")
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = data.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Visible
        )
    }
}

@Preview
@Composable
fun GameChangerAssessmentTeamDetailsScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = Space_40DP,
                    max = Space_40DP
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(Space_8DP)
                    .align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(Space_8DP),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(
                    SimpleIcons.Facebook,
                    SimpleIcons.Instagram,
                    SimpleIcons.Twitter,
                    SimpleIcons.Youtube
                ).forEachIndexed { index, item ->
                    IconButton(
                        modifier = Modifier
                            .wrapContentSize(),
                        onClick = {}
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(Space_24DP),
                            imageVector = item,
                            contentDescription = "Icon ${index + 1}"
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Space_8DP),
            verticalArrangement = Arrangement.spacedBy(Space_12DP)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(
                            stringResource(
                                R.string.team_details_name_label
                            )
                        )
                        append(" Team name")
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.team_details_weblink, "Team's"),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(
                            stringResource(
                                R.string.team_details_league_label
                            )
                        )
                    }
                    append(" League")
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
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
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(
                            stringResource(
                                R.string.team_details_gender_label
                            )
                        )
                    }
                    append(" Gender")
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(
                            stringResource(
                                R.string.team_details_stadium_label
                            )
                        )
                    }
                    append(" Stadium")
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(
                            stringResource(
                                R.string.team_details_location_label
                            )
                        )
                    }
                    append(" Location")
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
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
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Long multi-line description",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Visible
            )
        }
    }
}