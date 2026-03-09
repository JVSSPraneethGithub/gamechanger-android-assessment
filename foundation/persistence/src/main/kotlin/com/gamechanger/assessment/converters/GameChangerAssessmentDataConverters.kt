package com.gamechanger.assessment.converters

import androidx.room.TypeConverter
import kotlin.time.Instant

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Room-Database Time-stamp Converters.
 */
class GameChangerAssessmentDataConverters {
    @TypeConverter
    fun fromEpochMilliseconds(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun toEpochMilliseconds(value: Instant?): Long? {
        return value?.toEpochMilliseconds()
    }
}