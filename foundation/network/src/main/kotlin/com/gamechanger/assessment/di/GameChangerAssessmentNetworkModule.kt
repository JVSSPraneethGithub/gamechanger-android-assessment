package com.gamechanger.assessment.di

import com.gamechanger.assessment.api.GameChangerAssessmentApi
import com.gamechanger.assessment.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Network-I/O Dependency-framework Singleton-Module.
 */
@Module
@InstallIn(SingletonComponent::class)
class GameChangerAssessmentNetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpClient() = OkHttpClient.Builder().apply {
        addInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
        ).takeIf { BuildConfig.DEBUG }
    }.build()

    @Suppress("JSON_FORMAT_REDUNDANT")
    @Provides
    @Singleton
    fun providesGameChangerAssessmentApi(
        okHttpClient: OkHttpClient
    ) : GameChangerAssessmentApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(
                Json {
                    isLenient = true
                    prettyPrint = true
                    encodeDefaults = true
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                }.asConverterFactory(
                    "application/json; charset=UTF-8".toMediaType()
                )
            )
            .baseUrl("https://www.thesportsdb.com/api/v1/json/123/")
            .build()
            .create<GameChangerAssessmentApi>()
}