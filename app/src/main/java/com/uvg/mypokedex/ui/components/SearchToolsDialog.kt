package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.ui.features.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchToolsDialog(
    onDismiss: () -> Unit,
    viewModel: HomeViewModel,
    currentSortOrder: String
) {
    var expanded by remember { mutableStateOf(false) }
    val (currentSortType, currentSortDirection) = remember(currentSortOrder) {
        when (currentSortOrder) {
            "BY_NUMBER_ASC" -> Pair("Número", true)
            "BY_NUMBER_DESC" -> Pair("Número", false)
            "BY_NAME_ASC" -> Pair("Nombre", true)
            "BY_NAME_DESC" -> Pair("Nombre", false)
            else -> Pair("Número", true)
        }
    }
    var selectedOption by remember { mutableStateOf(currentSortType) }
    var isAscending by remember { mutableStateOf(currentSortDirection) }

    val options = listOf("Número", "Nombre")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Herramientas de Búsqueda") },
        text = {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HorizontalDivider()

                    // Selector de ordenamiento
                    Text(text = "Ordenar por:", style = MaterialTheme.typography.bodyLarge)

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = selectedOption,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedOption = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider()

                    // Selector de orden
                    Text(text = "Orden:", style = MaterialTheme.typography.bodyLarge)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = isAscending,
                                    onClick = { isAscending = true }
                                )
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isAscending,
                                onClick = { isAscending = true }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Ascendente")
                        }

                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = !isAscending,
                                    onClick = { isAscending = false }
                                )
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !isAscending,
                                onClick = { isAscending = false }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Descendente")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row {
                // ← CAMBIAR COMPLETAMENTE el TextButton de "Aplicar"
                TextButton(
                    onClick = {
                        val newSortOrder = when {
                            selectedOption == "Número" && isAscending -> "BY_NUMBER_ASC"
                            selectedOption == "Número" && !isAscending -> "BY_NUMBER_DESC"
                            selectedOption == "Nombre" && isAscending -> "BY_NAME_ASC"
                            selectedOption == "Nombre" && !isAscending -> "BY_NAME_DESC"
                            else -> "BY_NUMBER_ASC"
                        }
                        viewModel.changeSortOrder(newSortOrder) // ← AÑADIR esta línea
                        onDismiss()
                    }
                ) {
                    Text("Aplicar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        }
    )
}



