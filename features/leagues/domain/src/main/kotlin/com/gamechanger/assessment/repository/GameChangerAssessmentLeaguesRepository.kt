package com.gamechanger.assessment.repository

import androidx.room.withTransaction
import com.gamechanger.assessment.api.GameChangerAssessmentApi
import com.gamechanger.assessment.database.GameChangerAssessmentDatabase
import com.gamechanger.assessment.entities.GameChangerAssessmentLeagueEntity
import com.gamechanger.assessment.parcelables.GameChangerAssessmentLeague
import kotlin.time.Clock

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Leagues Repository.
 */
interface IGameChangerAssessmentLeaguesRepository {
    suspend fun fetchAllLeagues(): Result<List<GameChangerAssessmentLeague>>
}

internal class GameChangerAssessmentLeaguesRepository(
    private val api: GameChangerAssessmentApi,
    private val database: GameChangerAssessmentDatabase
) : IGameChangerAssessmentLeaguesRepository {
    override suspend fun fetchAllLeagues() = runCatching {
        api.getAllLeagues()
    }.onSuccess { response ->
        val createdInstant = Clock.System.now()
        database.withTransaction {
            database.leaguesDao().insertAll(
                response.leaguesList.map {
                    GameChangerAssessmentLeagueEntity(
                        id = it.id,
                        name = it.name,
                        sport = it.sport,
                        createdAt = createdInstant
                    )
                }
            )
        }
    }.let {
        runCatching {
            database.leaguesDao().getAll().map {
                GameChangerAssessmentLeague(
                    id = it.id,
                    name = it.name,
                    sport = it.sport
                )
            }
        }
    }
}