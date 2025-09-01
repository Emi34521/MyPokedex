package com.uvg.mypokedex.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    viewModel: PokemonDetailViewModel = PokemonDetailViewModel(),
    // Inyección de dependencias, buena practica modular
    onToggleFavorite: (Boolean) -> Unit // recibe bool devuelve nada
) {
    var isFavorite by remember(viewModel.getIsFavourite()) { mutableStateOf(viewModel.getIsFavourite()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra superior
        TopBar(
            name = pokemon.name,
            onBack = onBack,
            isFavorite = isFavorite,
            onToggleFavorite = {
                val newFavoriteStatus = !isFavorite
                // negar estado actual al hacer click
                isFavorite = newFavoriteStatus
                onToggleFavorite(newFavoriteStatus)
            }
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

        // Medidas de peso y altura
        PokemonMeasurements(weight = pokemon.weight, height = pokemon.height)

        Spacer(modifier = Modifier.height(16.dp))

        // Stats con barras
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
fun TopBar(
    modifier: Modifier = Modifier,
    name: String,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    isFavorite: Boolean
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(name.replaceFirstChar { it.uppercase() }) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            FavoriteButton(isFavorite = isFavorite, onClick = onToggleFavorite)
        }
    )
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean, // estado icono
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
            // cambiar icono y descripcion segun estado
            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
        )
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
        progress = { value / maxValue.toFloat() },
        modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
        color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}