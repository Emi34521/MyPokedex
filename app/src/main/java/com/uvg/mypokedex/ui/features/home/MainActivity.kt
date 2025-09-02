package com.uvg.mypokedex.ui.features.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stats
import com.uvg.mypokedex.ui.components.PokemonSearchBar
import com.uvg.mypokedex.ui.components.UnstablePokemonList
import com.uvg.mypokedex.ui.theme.MyPokedexTheme
import com.uvg.mypokedex.ui.detail.*
import com.uvg.mypokedex.ui.features.home.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyPokedexTheme {
                // Opción 1 Utiliza HomeScreen con el grid y boton flotante
                HomeScreen()

                // Opción 2, Usar la barra de búsqueda
                /*
                Scaffold { innerPadding ->
                    PokemonSearchBar(
                        modifier = Modifier.padding(innerPadding),
                        allPokemons = HomeViewModel().getPokemonList()
                    )
                }
                */

                // Opción 3
                // UnstablePokemonList(pokemons = listOf("Pikachu", "Bulbasaur", "Charmander"))

                // Opción 4:
                /*
                PokemonDetailScreen(Pokemon(
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
                ), onToggleFavorite = { isFavorite ->
                    // Manejo del estado de favorito
                    println("Pokemon marcado como favorito: $isFavorite")
                })
                */
            }
        }
    }
}