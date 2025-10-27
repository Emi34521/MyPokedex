package com.uvg.mypokedex.ui.features.home

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.NetworkStatusBanner
import com.uvg.mypokedex.ui.components.PokemonCardClickable
import com.uvg.mypokedex.ui.components.PokemonSearchBar
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = HomeViewModel(
        LocalContext.current.applicationContext as Application
    ),
    onPokemonClick: (Int) -> Unit = {},
    onSearchToolsClick: () -> Unit = {}
) {
    // Estados del ViewModel
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val currentSortOrder by viewModel.currentSortOrder.collectAsState()

    // Estado local para búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Estado para mostrar banner de reconexión
    var showReconnectedBanner by remember { mutableStateOf(false) }
    var previousConnectionState by remember { mutableStateOf(isConnected) }

    // Detectar cambios en conexión
    LaunchedEffect(isConnected) {
        if (isConnected && !previousConnectionState) {
            // Se restauró la conexión
            showReconnectedBanner = true
            viewModel.forceRefresh()

            // Ocultar banner después de 3 segundos
            delay(3000)
            showReconnectedBanner = false
        }
        previousConnectionState = isConnected
    }

    // Actualizar búsqueda en el ViewModel
    LaunchedEffect(searchQuery) {
        viewModel.updateSearchQuery(searchQuery)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Pokédex") },
                    actions = {
                        IconButton(
                            onClick = { viewModel.forceRefresh() },
                            enabled = isConnected && !isLoading
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Actualizar",
                                tint = if (isConnected && !isLoading) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                }
                            )
                        }
                    }
                )

                // Banner de estado de conexión
                NetworkStatusBanner(
                    isConnected = isConnected,
                    isLoading = isLoading,
                    onRetry = { viewModel.forceRefresh() }
                )

                // Banner de reconexión exitosa
                if (showReconnectedBanner) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✓ Conexión restaurada. Actualizando datos...",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de búsqueda
            PokemonSearchBar(
                searchQuery = searchQuery,
                onQueryChanged = { searchQuery = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Mensaje de error
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                }
            }

            // Indicador de carga
            if (isLoading && pokemonList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cargando Pokémon...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else if (pokemonList.isEmpty()) {
                // Sin resultados
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isNotEmpty()) {
                            "No se encontraron Pokémon"
                        } else {
                            "No hay Pokémon guardados.\n${if (!isConnected) "Conecta a internet para cargar datos." else ""}"
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // Lista de Pokémon
                val gridState = rememberLazyGridState()

                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = pokemonList,
                        key = { it.id }
                    ) { pokemon ->
                        PokemonCardClickable(
                            pokemon = pokemon,
                            onClick = { onPokemonClick(pokemon.id) }
                        )
                    }

                    // Botón para cargar más (solo si hay conexión o datos JSON)
                    item {
                        if (!isLoading) {
                            Button(
                                onClick = { viewModel.loadMorePokemonFromJson() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cargar más")
                            }
                        }
                    }
                }
            }
        }
    }
}