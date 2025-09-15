package com.uvg.mypokedex.navigate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.detail.DetailScreen
import com.uvg.mypokedex.ui.features.home.HomeScreen

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("HomeScreen")

    object Detail : NavRoutes("DetailScreen/{pokemonId}") {
        fun createRoute(pokemon: Pokemon) = "DetailScreen/${pokemon.id}"
    }
    object SearchTools : NavRoutes("SearchTools")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController// No necesario nombre completo del paquete si está importado.
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route, // La vista predeterminada HomeScreen.
        modifier = modifier
    ) {
        // Ruta para HomeScreen
        composable(NavRoutes.Home.route) {
            HomeScreen(modifier = modifier,
            //     TODO("Agregar viewModel")
            //     onPokemonClick = { pokemonId ->
            //         // Navega a la pantalla de detalle pasando el ID del Pokémon.
            //         navController.navigate(NavRoutes.Detail.createRoute(pokemon))
            //     },
            //     onSearchClick = {
            //         // Navega al diálogo de herramientas de búsqueda.
            //         navController.navigate(NavRoutes.SearchTools.route)
            //     }
            )
        }

        // Ruta para DetailScreen
        composable(
            route = NavRoutes.Detail.route,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Recupera el argumento desde el backStackEntry.
            // BackStack: Stack que guarda las pantallas que se han visitado.
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId")

            requireNotNull(pokemonId) { "El ID del Pokémon no puede ser nulo." }

//            DetailScreen(
//                 pokemonId = pokemonId,
//                 onNavigateBack = {
//                     // Permite retroceder a la pantalla anterior (HomeScreen).
//                     navController.navigateUp()
//                 }
//            )
        }

        // Ruta para el diálogo de herramientas de búsqueda (SearchToolsScreen)
        dialog(NavRoutes.SearchTools.route) { // Mostrar la pantalla como un diálogo.
            // SearchToolsScreen(
            //     onDismiss = {
            //         // Cierra el diálogo.
            //         navController.popBackStack()
            //     }
            // )
        }
    }
}