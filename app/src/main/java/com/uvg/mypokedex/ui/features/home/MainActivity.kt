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
import com.uvg.mypokedex.ui.components.UnstablePokemonList
import com.uvg.mypokedex.ui.theme.MyPokedexTheme
import com.uvg.mypokedex.ui.detail.*
import com.uvg.mypokedex.ui.features.home.HomeViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // calcula el espaciado para que el contenido no tenga problemas con
        // las barras de estado y navegación del dispositivo
        setContent {
            MyPokedexTheme {
                Scaffold { innerPadding ->
                    // espaciado proporcionado por Scaffold para evitar problemas
                    HomeScreen(modifier = Modifier.padding(innerPadding))

                    // Muestra tu pantalla de prueba
                    //UnstablePokemonList(pokemons = listOf("Pikachu", "Bulbasaur", "Charmander"))
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
                    ))
                }
            }
        }
    }
}