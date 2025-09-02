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
                id = 25,
                name = "Pikachu",
                types = listOf("Electric"),
                weight = 6.0f,
                height = 0.4f,
                stats = Stats(
                    hp = 35,
                    attack = 55,
                    defense = 40,
                    specialAttack = 50,
                    specialDefense = 50,
                    speed = 90
                )
            ),
            Pokemon(
                id = 39,
                name = "Jigglypuff",
                types = listOf("Normal", "Fairy"),
                weight = 5.5f,
                height = 0.5f,
                stats = Stats(
                    hp = 115,
                    attack = 45,
                    defense = 20,
                    specialAttack = 45,
                    specialDefense = 25,
                    speed = 20
                )
            ),
            Pokemon(
                id = 54,
                name = "Psyduck",
                types = listOf("Water"),
                weight = 19.6f,
                height = 0.8f,
                stats = Stats(
                    hp = 50,
                    attack = 52,
                    defense = 48,
                    specialAttack = 65,
                    specialDefense = 50,
                    speed = 55
                )
            ),
            Pokemon(
                id = 104,
                name = "Cubone",
                types = listOf("Ground"),
                weight = 6.5f,
                height = 0.4f,
                stats = Stats(
                    hp = 50,
                    attack = 50,
                    defense = 95,
                    specialAttack = 40,
                    specialDefense = 50,
                    speed = 35
                )
            ),
            Pokemon(
                id = 131,
                name = "Lapras",
                types = listOf("Water", "Ice"),
                weight = 220.0f,
                height = 2.5f,
                stats = Stats(
                    hp = 130,
                    attack = 85,
                    defense = 80,
                    specialAttack = 85,
                    specialDefense = 95,
                    speed = 60
                )
            ),
            Pokemon(
                id = 143,
                name = "Snorlax",
                types = listOf("Normal"),
                weight = 460.0f,
                height = 2.1f,
                stats = Stats(
                    hp = 160,
                    attack = 110,
                    defense = 65,
                    specialAttack = 65,
                    specialDefense = 110,
                    speed = 30
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
            ),
            Pokemon(
                id = 151,
                name = "Mew",
                types = listOf("Psychic"),
                weight = 4.0f,
                height = 0.4f,
                stats = Stats(
                    hp = 100,
                    attack = 100,
                    defense = 100,
                    specialAttack = 100,
                    specialDefense = 100,
                    speed = 100
                )
            ),
            Pokemon(
                id = 448,
                name = "Lucario",
                types = listOf("Fighting", "Steel"),
                weight = 54.0f,
                height = 1.2f,
                stats = Stats(
                    hp = 70,
                    attack = 110,
                    defense = 70,
                    specialAttack = 115,
                    specialDefense = 70,
                    speed = 90
                )
            )
        )
    }
}
