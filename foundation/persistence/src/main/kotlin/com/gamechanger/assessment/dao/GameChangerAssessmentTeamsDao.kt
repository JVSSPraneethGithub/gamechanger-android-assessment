package com.gamechanger.assessment.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gamechanger.assessment.entities.GameChangerAssessmentTeamEntity

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Room-Database Teams Data-Access component.
 */
@Dao
interface GameChangerAssessmentTeamsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: GameChangerAssessmentTeamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<GameChangerAssessmentTeamEntity>)

    @Query("select * from gamechangers_team " +
            "where id = :id and name = :name and league = :league " +
            "limit 1")
    suspend fun fetchTeam(
        id: String,
        name: String,
        league: String
    ): GameChangerAssessmentTeamEntity

    @Query("select * from gamechangers_team where league = :league " +
            "order by created_at desc")
    suspend fun fetchAllTeamsForLeague(league: String): List<GameChangerAssessmentTeamEntity>
}