package com.gamechanger.assessment.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Launch WebLink utility.
 */
fun launchWebLink(
    context: Context,
    url: String
) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        url.toUri()
    )
    val chooser = Intent.createChooser(
        intent,
        null
    )
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(chooser)
    } else {
        context.startActivity(intent)
    }
}