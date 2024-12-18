package fr.vitesse.rh.ui.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("home")

    data object DetailCandidate : Screen(
        route = "candidate/{candidateId}",
        navArguments = listOf(navArgument("candidateId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(candidateId: String): String = "candidate/$candidateId"
    }

    data object CreateOrUpdateCandidate : Screen(
        route = "saveCandidate/{candidateId}",
        navArguments = listOf(navArgument("candidateId") {
            type = NavType.StringType
            nullable = true
        })
    ) {
        fun createRoute(candidateId: String?): String =
            if (candidateId.isNullOrEmpty()) "saveCandidate/" else "saveCandidate/$candidateId"
    }
}
