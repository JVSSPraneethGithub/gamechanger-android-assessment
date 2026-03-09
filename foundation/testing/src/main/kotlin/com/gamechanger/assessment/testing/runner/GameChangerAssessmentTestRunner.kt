package com.gamechanger.assessment.testing.runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Ui-testing Hilt-Runner.
 */
class GameChangerAssessmentTestRunner: AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}