package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    //Inyección de dependencias
    viewModel: HomeViewModel = HomeViewModel()
) {
    // Estado para controlar el orden (true = ascendente, false = descendente)
    var isAscending by remember { mutableStateOf(false) }

    val pokemonList = viewModel.getPokemonList()
    val state = rememberLazyGridState()
    // guardar el ultimo estado de la lista

    // Lista ordenada según el estado actual
    val sortedPokemonList = if (isAscending) {
        pokemonList.sortedBy { it.name }
    } else {
        pokemonList.sortedByDescending { it.name }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isAscending = !isAscending // Cambiar entre ascendente y descendente
                }
            ) {
                Icon(
                    imageVector = if (isAscending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isAscending) "Orden ascendente" else "Orden descendente"
                )
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            state = state,
            //siempre que se corre el codigo, recomposicione, se mostrara el ultimo estado
            modifier = Modifier,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp,
                bottom = 8.dp + paddingValues.calculateBottomPadding()
            ),
            //espaciado del objeto
            verticalArrangement = Arrangement.SpaceBetween,
            //espaciado entre elementos
            columns = GridCells.Fixed(2)
            //cantidad de columnas
        ) {
            items(sortedPokemonList) { pokemon: Pokemon ->
                //los elementos del grid son las cartas
                PokemonCard(pokemon)
            }
        }
    }
}