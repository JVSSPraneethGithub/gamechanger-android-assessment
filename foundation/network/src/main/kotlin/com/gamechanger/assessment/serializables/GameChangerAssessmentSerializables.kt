@file:OptIn(InternalSerializationApi::class)

package com.gamechanger.assessment.serializables

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Network-I/O Kotlinx Serializable Entities.
 */
@Serializable
data class GameChangerLeagueResponse(
    @SerialName("leagues")
    val leaguesList: List<GameChangerLeagueSerializableEntity>
)
@Serializable
data class GameChangerLeagueSerializableEntity(
    @SerialName("idLeague")
    val id: String,
    @SerialName("strLeague")
    val name: String,
    @SerialName("strSport")
    val sport: String
)

@Serializable
data class GameChangerTeamsResponse(
    @SerialName("teams")
    val teamsList: List<GameChangerTeamSerializableEntity>
)

@Serializable
data class GameChangerTeamSerializableEntity(
    @SerialName("idTeam")
    val id: String,
    @SerialName("strTeam")
    val name: String,
    @SerialName("strSport")
    val sport: String,
    @SerialName("strGender")
    val gender: String,
    @SerialName("strLeague")
    val league: String,
    @SerialName("strStadium")
    val stadium: String,
    @SerialName("strLocation")
    val location: String,
    @SerialName("strCountry")
    val country: String,
    @SerialName("strBadge")
    val badge: String,
    @SerialName("strBanner")
    val banner: String,
    @SerialName("strDescriptionEN")
    val description: String,
    @SerialName("strWebsite")
    val website: String,
    @SerialName("strFacebook")
    val facebook: String,
    @SerialName("strTwitter")
    val twitter: String,
    @SerialName("strInstagram")
    val instagram: String,
    @SerialName("strYoutube")
    val youtube: String,
    @SerialName("strLocked")
    val locked: String
)
