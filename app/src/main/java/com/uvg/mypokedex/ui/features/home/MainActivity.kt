package com.uvg.mypokedex.ui.features.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import com.uvg.mypokedex.ui.theme.MyPokedexTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier


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
                }
            }
        }
    }
}