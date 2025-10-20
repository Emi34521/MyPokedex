package com.uvg.mypokedex.ui.features.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.uvg.mypokedex.navigation.AppNavigation
import com.uvg.mypokedex.ui.theme.MyPokedexTheme


class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    // gracias a by no hay que preocuparse por brindar contexto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // calcula el espaciado para que el contenido no tenga problemas con
        // las barras de estado y navegación del dispositivo
        setContent {
            MyPokedexTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        homeViewModel = homeViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}