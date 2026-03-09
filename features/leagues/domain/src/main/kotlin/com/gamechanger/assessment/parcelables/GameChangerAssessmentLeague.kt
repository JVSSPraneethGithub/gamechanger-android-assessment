package com.gamechanger.assessment.parcelables

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * League-item Parcelable.
 */
@Parcelize
data class GameChangerAssessmentLeague(
    val id: String,
    val name: String,
    val sport: String
): Parcelable