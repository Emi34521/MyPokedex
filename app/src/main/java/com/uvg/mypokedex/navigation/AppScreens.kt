package com.uvg.mypokedex.navigation

sealed class AppScreens(val route: String){
    object home: AppScreens("home_screen")
    object detail: AppScreens("detail_screen/{pokemonId}") { // argumento de navegacion: id
        fun createRoute(pokemonId: Int) = "detail_screen/$pokemonId"
    }
    object SearchToolsDialog: AppScreens("search_tools_dialog")
}