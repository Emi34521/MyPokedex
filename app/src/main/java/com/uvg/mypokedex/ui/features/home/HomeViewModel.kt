package com.uvg.mypokedex.ui.features.home
import com.uvg.mypokedex.data.model.Pokemon

class HomeViewModel {
    fun getPokemonList(): List<Pokemon> {
        return listOf(
            Pokemon(1, 45, 45, 49, 49, 45, 45, 65, 65, 65, 65, "Bulbasaur", "Grass"),
            Pokemon(4, 39, 39, 43, 43, 65, 65, 60, 60, 50, 50, "Charmander", "Fire"),
            Pokemon(7, 58, 58, 55, 55, 60, 60, 50, 50, 50, 50, "Squirtle", "Water"),
            Pokemon(150, 106, 106, 130, 130, 140, 140, 100, 100, 100, 100, "Mewtwo", "Psychic")
        )
    }
}