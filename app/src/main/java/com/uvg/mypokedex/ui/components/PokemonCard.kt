package com.uvg.mypokedex.ui.components

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
    // URL oficial basada en el ID del Pokémon
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
            horizontalAlignment = Alignment.CenterHorizontally, // Centra el contenido horizontalmente
            verticalArrangement = Arrangement.Center // Centra verticalmente
        ) {
            // Imagen del pokemon
            AsyncImage(
               model = imageUrl, // URL de la imagen del pokemon
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
                    // Ocupa el espacio disponible y empuja a los otros elementos
                )
            }

            // Clase del pokemon
            Text(
                text = "Tipo: ${pokemon.type}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}