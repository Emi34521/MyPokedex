package com.uvg.mypokedex.ui.features.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import com.uvg.mypokedex.ui.theme.MyPokedexTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.UnstablePokemonList



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
                    UnstablePokemonList( pokemons = listOf("Pikachu", "Bulbasaur", "Charmander"))

                }

            }
        }
    }
}