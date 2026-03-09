package com.gamechanger.assessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.gamechanger.assessment.compose.GameChangerAssessmentNavHostScreen
import com.gamechangers.assessment.compose.theme.GameChangerAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Main Activity.
 */
@AndroidEntryPoint
internal class GameChangerAssessmentMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameChangerAssessmentTheme {
                GameChangerAssessmentNavHostScreen(rememberNavController())
            }
        }
    }
}