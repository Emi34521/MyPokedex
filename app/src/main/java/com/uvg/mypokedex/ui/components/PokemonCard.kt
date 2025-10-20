package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.uvg.mypokedex.data.model.Pokemon

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    typeColor: Color = Color.Gray// modo default de pokemon
) {
    // toma url de la imagen de pokemon,
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = typeColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp), // Margen interno de 16dp
            horizontalAlignment = Alignment.CenterHorizontally, // centra horizontalmente
            verticalArrangement = Arrangement.Center // centra verticalmente
        ) {
            // imagen del pokemon
            AsyncImage(
                model = imageUrl, // URL mencionada anteriormente
                contentDescription = "Imagen de ${pokemon.name}",
                modifier = Modifier
                    .size(120.dp) // Tamaño de la imagen
                    .padding(8.dp) // espaciado de la imagen
            )

            Spacer(modifier = Modifier.height(8.dp))// espacio entre la imagen y el texto

            // Nombre del pokemon
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(pokemon.id.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },// Capitaliza primera letra
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                    // Ocupa el espacio disponible y empuja el resto
                )
            }

            // Aquí se obtenía un error porque no reconocía "type" para resolver este error se usó IA cuyo promp fué que no se reconocía type
            Text(
                text = "Tipo: ${pokemon.types.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium

            )
        }
    }
}

@Composable
fun PokemonCardClickable(
    pokemon: Pokemon,
    typeColor: Color = Color.Gray,
    onClick: () -> Unit = {}
) {
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = typeColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Imagen de ${pokemon.name}",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(pokemon.id.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Tipo: ${pokemon.types.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}