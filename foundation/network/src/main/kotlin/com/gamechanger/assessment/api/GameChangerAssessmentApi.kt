package com.gamechanger.assessment.api

import com.gamechanger.assessment.serializables.GameChangerLeagueResponse
import com.gamechanger.assessment.serializables.GameChangerTeamsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Retrofit Network-I/O.
 */
interface GameChangerAssessmentApi {

    @GET("all_leagues.php")
    suspend fun getAllLeagues(): GameChangerLeagueResponse

    @GET("search_all_teams.php")
    suspend fun searchAllTeams(
        @Query("l") league: String
    ): GameChangerTeamsResponse

    @GET("searchteams.php")
    suspend fun searchTeamByName(
        @Query("t") name: String
    ): GameChangerTeamsResponse
}