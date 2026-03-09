package com.gamechanger.assessment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gamechanger.assessment.converters.GameChangerAssessmentDataConverters
import com.gamechanger.assessment.dao.GameChangerAssessmentLeaguesDao
import com.gamechanger.assessment.dao.GameChangerAssessmentTeamsDao
import com.gamechanger.assessment.entities.GameChangerAssessmentLeagueEntity
import com.gamechanger.assessment.entities.GameChangerAssessmentTeamEntity

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Room-Database.
 */
@Database(
    version = 1,
    exportSchema = false,
    entities = [
        GameChangerAssessmentLeagueEntity::class,
        GameChangerAssessmentTeamEntity::class
    ]
)
@TypeConverters(GameChangerAssessmentDataConverters::class)
abstract class GameChangerAssessmentDatabase : RoomDatabase() {
    abstract fun leaguesDao(): GameChangerAssessmentLeaguesDao
    abstract fun teamsDao(): GameChangerAssessmentTeamsDao
}