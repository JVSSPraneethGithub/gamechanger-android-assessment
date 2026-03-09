package com.gamechanger.assessment.compose.nav

/**
 * Copyright : GameChangers Assessment
 * Author: Praneeth Jataprolu
 * https://www.github.com/JVSSPraneethGithub
 *
 * Navigation items.
 */
enum class GameChangerAssessmentNav(
    private val subNav: List<Pair<String, List<String>>>
) {
    LEAGUES(
        listOf("leagues" to emptyList())
    ),
    TEAMS(
        listOf(
            "leagues/teams" to listOf("ARG_LEAGUE_NAME"),
            "leagues/teams/details" to
                    listOf("ARG_TEAM_ID", "ARG_TEAM_NAME", "ARG_LEAGUE_NAME")
        )
    );

    fun asRouteAtIndex(index: Int): String = with(subNav[index]) {
        first.plus(
            second.takeIf { it.isNotEmpty() }
                ?.joinToString(
                    separator = "/",
                    prefix = "/"
                ) { "{$it}" } ?: ""
        )
    }

    fun navArgsAtIndex(index: Int): List<String> = subNav[index].second
}