package com.gamechanger.assessment.repository

import androidx.room.withTransaction
import com.gamechanger.assessment.api.GameChangerAssessmentApi
import com.gamechanger.assessment.database.GameChangerAssessmentDatabase
import com.gamechanger.assessment.entities.GameChangerAssessmentTeamEntity
import com.gamechanger.assessment.parcelables.GameChangerAssessmentTeam
import kotlin.time.Clock

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Teams Repository.
 */
interface IGameChangerAssessmentTeamsRepository {
    suspend fun fetchAllTeamsForLeague(name: String): Result<List<GameChangerAssessmentTeam>>

    suspend fun fetchTeam(
        id: String,
        teamName: String,
        leagueName: String
    ): Result<GameChangerAssessmentTeam>
}

internal class GameChangerAssessmentTeamsRepository(
    private val api: GameChangerAssessmentApi,
    private val database: GameChangerAssessmentDatabase
) : IGameChangerAssessmentTeamsRepository {

    private fun cleanWebLink(webLink: String) = webLink.takeIf { link ->
        !link.startsWith("https://")
    }?.let { link ->
        "https://${link}"
    } ?: webLink

    override suspend fun fetchAllTeamsForLeague(name: String) = runCatching {
        api.searchAllTeams(name)
    }.onSuccess { response ->
        val createdInstant = Clock.System.now()
        database.withTransaction {
            database.teamsDao().insertAll(
                response.teamsList.map {
                    GameChangerAssessmentTeamEntity(
                        id = it.id,
                        name = it.name,
                        sport = it.sport,
                        gender = it.gender,
                        league = it.league,
                        stadium = it.stadium,
                        location = it.location,
                        country = it.country,
                        badge = it.badge,
                        banner = it.banner,
                        description = it.description,
                        website = it.website,
                        facebook = it.facebook,
                        twitter = it.twitter,
                        instagram = it.instagram,
                        youtube = it.youtube,
                        locked = it.locked,
                        createdAt = createdInstant
                    )
                }
            )
        }
    }.let {
        runCatching {
            database.teamsDao().fetchAllTeamsForLeague(name).map {
                GameChangerAssessmentTeam(
                    id = it.id,
                    name = it.name,
                    sport = it.sport,
                    gender = it.gender,
                    league = it.league,
                    stadium = it.stadium,
                    location = it.location,
                    country = it.country,
                    badge = it.badge,
                    banner = it.banner,
                    description = it.description,
                    website = cleanWebLink(it.website),
                    facebook = cleanWebLink(it.facebook),
                    twitter = cleanWebLink(it.twitter),
                    instagram = cleanWebLink(it.instagram),
                    youtube = cleanWebLink(it.youtube),
                    locked = it.locked
                )
            }
        }
    }

    override suspend fun fetchTeam(
        id: String,
        teamName: String,
        leagueName: String
    ) = runCatching {
        api.searchTeamByName(teamName)
    }.onSuccess { response ->
        response.teamsList.find { item ->
            item.id == id && item.league == leagueName && item.name == teamName
        }?.also {
            database.withTransaction {
                database.teamsDao().insertTeam(
                    GameChangerAssessmentTeamEntity(
                        id = it.id,
                        name = it.name,
                        sport = it.sport,
                        gender = it.gender,
                        league = it.league,
                        stadium = it.stadium,
                        location = it.location,
                        country = it.country,
                        badge = it.badge,
                        banner = it.banner,
                        description = it.description,
                        website = it.website,
                        facebook = it.facebook,
                        twitter = it.twitter,
                        instagram = it.instagram,
                        youtube = it.youtube,
                        locked = it.locked,
                        createdAt = Clock.System.now()
                    )
                )
            }
        }
    }.let {
        runCatching {
            database.teamsDao().fetchTeam(
                id = id,
                name = teamName,
                league = leagueName
            ).let {
                GameChangerAssessmentTeam(
                    id = it.id,
                    name = it.name,
                    sport = it.sport,
                    gender = it.gender,
                    league = it.league,
                    stadium = it.stadium,
                    location = it.location,
                    country = it.country,
                    badge = it.badge,
                    banner = it.banner,
                    description = it.description,
                    website = cleanWebLink(it.website),
                    facebook = cleanWebLink(it.facebook),
                    twitter = cleanWebLink(it.twitter),
                    instagram = cleanWebLink(it.instagram),
                    youtube = cleanWebLink(it.youtube),
                    locked = it.locked
                )
            }
        }
    }
}