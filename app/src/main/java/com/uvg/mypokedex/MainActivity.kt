package com.uvg.mypokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.uvg.mypokedex.ui.screens.PokemonListScreen
import com.uvg.mypokedex.ui.theme.MyPokedexTheme
import com.uvg.mypokedex.ui.viewmodel.PokemonViewModel
import com.uvg.mypokedex.ui.viewmodel.PokemonViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el repositorio de la aplicación
        val app = application as PokemonApplication

        // Crear el ViewModel con el factory
        viewModel = ViewModelProvider(
            this,
            PokemonViewModelFactory(app.pokemonRepository)
        )[PokemonViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            MyPokedexTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    PokemonListScreen(
                        viewModel = viewModel,
                        modifier = Modifier.Companion.padding(innerPadding)
                    )
                }
            }
        }
    }
}