package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchToolsDialog(
    onDismiss: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Número") }
    var isAscending by remember { mutableStateOf(true) }

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
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}