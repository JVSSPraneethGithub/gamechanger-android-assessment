package com.gamechanger.assessment.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import kotlin.time.Instant

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Room-Database Entities.
 */
@Entity(
    tableName = "gamechangers_league",
    primaryKeys = ["id"]
)
data class GameChangerAssessmentLeagueEntity(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "sport")
    val sport: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant
)

@Entity(
    tableName = "gamechangers_team",
    primaryKeys = [
        "id",
        "name",
        "league"
    ],
    indices = [
        Index(value = ["league"])
    ]
)
data class GameChangerAssessmentTeamEntity(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "sport")
    val sport: String,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "league")
    val league: String,
    @ColumnInfo(name = "stadium")
    val stadium: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "badge")
    val badge: String,
    @ColumnInfo(name = "banner")
    val banner: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "website")
    val website: String,
    @ColumnInfo(name = "facebook")
    val facebook: String,
    @ColumnInfo(name = "twitter")
    val twitter: String,
    @ColumnInfo(name = "instagram")
    val instagram: String,
    @ColumnInfo(name = "youtube")
    val youtube: String,
    @ColumnInfo(name = "locked")
    val locked: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant
)