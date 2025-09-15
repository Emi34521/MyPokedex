package com.uvg.mypokedex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.detail.PokemonDetailScreen
import com.uvg.mypokedex.ui.components.SearchTools

@Composable
fun NavigationSetup(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Home.route,
        modifier = modifier
    ) {
        composable(route = AppScreens.Home.route) {
            HomeScreen()
        }

        composable(
            route = "detail_screen/{pokemonId}",
            arguments = listOf(
                navArgument("pokemonId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 0

            val dummyPokemon = com.uvg.mypokedex.data.model.Pokemon(
                id = pokemonId,
                name = "Pokemon $pokemonId",
                types = listOf("Normal"),
                weight = 10.0f,
                height = 1.0f,
                stats = com.uvg.mypokedex.data.model.Stats(50, 50, 50, 50, 50, 50)
            )

            PokemonDetailScreen(
                pokemon = dummyPokemon,
                onBack = {
                    navController.popBackStack()
                },
                onToggleFavorite = { isFavorite ->
                    // Por ahora vacio
                }
            )
        }

        dialog(route = AppScreens.SearchToolsDialog.route) {
            SearchTools(
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }
    }
}