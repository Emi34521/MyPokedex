package com.uvg.mypokedex.ui.components
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PokemonSearchBar(
    searchQuery: String,                  // 1. Receive the current search query
    onQueryChanged: (String) -> Unit,     // 2. Receive a function to call when it changes
    modifier: Modifier = Modifier
) {
    TextField(
        value = searchQuery,              // Use the received value
        onValueChange = onQueryChanged,   // Call the received function on change
        label = { Text(text = "Buscar Pokémon") },
        modifier = modifier.fillMaxWidth()
    )
}
