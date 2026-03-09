package com.gamechanger.assessment.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gamechanger.assessment.entities.GameChangerAssessmentLeagueEntity

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Room-Database Leagues Data-Access component.
 */
@Dao
interface GameChangerAssessmentLeaguesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GameChangerAssessmentLeagueEntity>)

    @Query("select * from gamechangers_league " +
            "order by created_at desc")
    suspend fun getAll(): List<GameChangerAssessmentLeagueEntity>
}