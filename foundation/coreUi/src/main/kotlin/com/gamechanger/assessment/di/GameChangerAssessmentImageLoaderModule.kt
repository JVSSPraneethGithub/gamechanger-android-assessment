package com.gamechanger.assessment.di

import android.content.Context
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.network.CacheStrategy
import coil3.network.ConnectivityChecker
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Coil-3 ImageLoader Dependency-framework.
 */
@Module
@InstallIn(SingletonComponent::class)
class GameChangerAssessmentImageLoaderModule {
    @OptIn(ExperimentalCoilApi::class)
    @Provides
    @Singleton
    fun providesImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ) = ImageLoader.Builder(context)
        .diskCache(
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("gamechanger_assessment"))
                .maxSizePercent(0.02)
                .build()
        )
        .components {
            add(
                OkHttpNetworkFetcherFactory(
                    callFactory = { okHttpClient },
                    cacheStrategy = { CacheStrategy.DEFAULT },
                    connectivityChecker = { ctx -> ConnectivityChecker(ctx) }
                )
            )
        }
        .build()
}
