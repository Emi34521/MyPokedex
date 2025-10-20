package com.uvg.mypokedex.navigation

sealed class AppScreens(val route: String){
    object home: AppScreens("home_screen")
    object detail: AppScreens("detail_screen{pokemonId}") // argumento de navegacion: id
    object SearchResults: AppScreens("search_results_screen")
}
