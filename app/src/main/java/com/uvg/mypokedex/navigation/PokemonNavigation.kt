package com.uvg.mypokedex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uvg.mypokedex.presentation.detail.PokemonDetailScreen
import com.uvg.mypokedex.presentation.list.PokemonListScreen

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list")
    object PokemonDetail : Screen("pokemon_detail/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "pokemon_detail/$pokemonId"
    }
}

@Composable
fun PokedexNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route
    ) {
        composable(Screen.PokemonList.route) {
            PokemonListScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                }
            )
        }

        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(
                navArgument("pokemonId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 0
            PokemonDetailScreen(
                pokemonId = pokemonId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}