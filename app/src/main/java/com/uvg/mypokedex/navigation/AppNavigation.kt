package com.uvg.mypokedex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.uvg.mypokedex.ui.components.SearchToolsDialog
import com.uvg.mypokedex.ui.detail.PokemonDetailScreen
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.features.home.HomeViewModel


@Composable
fun AppNavigation(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.home.route,
        modifier = modifier
    ) {
        // Pantalla principal (Home)
        composable(route = AppScreens.home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onPokemonClick = { pokemonId ->
                    navController.navigate(AppScreens.detail.createRoute(pokemonId))
                },
                onSearchToolsClick = {
                    navController.navigate(AppScreens.SearchToolsDialog.route)
                }
            )
        }

        // Pantalla de detalle
        composable(
            route = AppScreens.detail.route,
            arguments = listOf(
                navArgument("pokemonId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 0
            val pokemon = homeViewModel.getPokemons().find { it.id == pokemonId }

            if (pokemon != null) {
                PokemonDetailScreen(
                    pokemon = pokemon,
                    onBack = { navController.popBackStack() },
                    onToggleFavorite = { isFavorite ->
                        // Logica para agregar/quitar de favoritos
                    }
                )
            }
        }

        // Dialogo de herramientas de búsqueda
        dialog(route = AppScreens.SearchToolsDialog.route) {
            SearchToolsDialog(
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}