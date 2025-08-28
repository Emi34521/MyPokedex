package com.uvg.mypokedex.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.toMap

@Composable
fun PokemonDetailScreen(
    pokemon: Pokemon,
    onBack: () -> Unit = {},
    onToggleFavorite: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔹 Barra superior
        TopBar(
            name = pokemon.name,
            onBack = onBack,
            onToggleFavorite = onToggleFavorite
        )

        Spacer(modifier = Modifier.height(16.dp))

        // imagen del pokemon
        AsyncImage(
            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
            contentDescription = pokemon.name,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Medidas de peso y altura
        PokemonMeasurements(weight = pokemon.weight, height = pokemon.height)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Stats con barras
        Column {
            pokemon.stats.toMap().forEach { (name, value) ->
                PokemonStatRow(statName = name, value = value, maxValue = 255)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

//Prompt: El programa esta avisando que topappbar es una función experimental que puede cambiar, como puedo arreglar esto?
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(name: String, onBack: () -> Unit, onToggleFavorite: () -> Unit) {
    TopAppBar(
        title = { Text(name.replaceFirstChar { it.uppercase() }) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            FavoriteButton(onClick = onToggleFavorite)
        }
    )
}

@Composable
fun FavoriteButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
    }
}

@Composable
fun PokemonMeasurements(weight: Float, height: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Peso", style = MaterialTheme.typography.bodyLarge)
            Text(text = "$weight kg", style = MaterialTheme.typography.bodyMedium)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Altura", style = MaterialTheme.typography.bodyLarge)
            Text(text = "$height m", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun PokemonStatRow(statName: String, value: Int, maxValue: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = statName, style = MaterialTheme.typography.bodyMedium)
            Text(text = value.toString(), style = MaterialTheme.typography.bodyMedium)
        }
        //Prompt: Como agregar una barra de progresión en jetpack compose
        LinearProgressIndicator(
            progress = value / maxValue.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}
