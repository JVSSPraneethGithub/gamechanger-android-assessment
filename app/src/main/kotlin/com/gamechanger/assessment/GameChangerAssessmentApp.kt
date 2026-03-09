package com.gamechanger.assessment

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Main App.
 */
@HiltAndroidApp
internal class GameChangerAssessmentApp : Application(),
        SingletonImageLoader.Factory {

            @Inject
            lateinit var imageLoader: ImageLoader

            override fun newImageLoader(context: Context) = imageLoader
}