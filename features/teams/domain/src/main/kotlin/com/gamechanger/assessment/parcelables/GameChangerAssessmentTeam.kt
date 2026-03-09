package com.gamechanger.assessment.parcelables

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Team-item Parcelable.
 */
@Parcelize
data class GameChangerAssessmentTeam(
    val id: String,
    val name: String,
    val sport: String,
    val gender: String,
    val league: String,
    val stadium: String,
    val location: String,
    val country: String,
    val badge: String,
    val banner: String,
    val description: String,
    val website: String,
    val facebook: String,
    val twitter: String,
    val instagram: String,
    val youtube: String,
    val locked: String
) : Parcelable