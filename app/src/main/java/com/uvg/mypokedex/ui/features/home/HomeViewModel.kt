package com.uvg.mypokedex.ui.features.home

import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats

class HomeViewModel {
    fun getPokemonList(): List<Pokemon> {
        return listOf(
            Pokemon(
                id = 1,
                name = "Bulbasaur",
                types = listOf("Grass", "Poison"),
                weight = 6.9f,
                height = 0.7f,
                stats = Stats(
                    hp = 45,
                    attack = 49,
                    defense = 49,
                    specialAttack = 65,
                    specialDefense = 65,
                    speed = 45
                )
            ),
            Pokemon(
                id = 4,
                name = "Charmander",
                types = listOf("Fire"),
                weight = 8.5f,
                height = 0.6f,
                stats = Stats(
                    hp = 39,
                    attack = 52,
                    defense = 43,
                    specialAttack = 60,
                    specialDefense = 50,
                    speed = 65
                )
            ),
            Pokemon(
                id = 7,
                name = "Squirtle",
                types = listOf("Water"),
                weight = 9.0f,
                height = 0.5f,
                stats = Stats(
                    hp = 44,
                    attack = 48,
                    defense = 65,
                    specialAttack = 50,
                    specialDefense = 64,
                    speed = 43
                )
            ),
            Pokemon(
                id = 150,
                name = "Mewtwo",
                types = listOf("Psychic"),
                weight = 122.0f,
                height = 2.0f,
                stats = Stats(
                    hp = 106,
                    attack = 110,
                    defense = 90,
                    specialAttack = 154,
                    specialDefense = 90,
                    speed = 130
                )
            )
        )
    }
}
