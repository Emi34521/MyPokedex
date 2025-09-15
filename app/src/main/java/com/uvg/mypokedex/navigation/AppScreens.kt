package com.uvg.mypokedex.navigation

sealed class AppScreens(val route: String) {
    object Home: AppScreens("home_screen")
    object Detail: AppScreens("detail_screen/{pokemonId}")
    object SearchToolsDialog: AppScreens("search_tools_dialog")
}