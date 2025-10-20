package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.DropdownItem

val itemList = listOf(
    DropdownItem("Número"),
    DropdownItem("Nombre")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TODO(implementar herramientas de busqueda)
fun SearchTools(){
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                text = "Herramientas de Búsqueda"
            )
            //generado por gemini
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            var expanded by remember { mutableStateOf(false) }
            var selectedOption by remember { mutableStateOf(itemList[0]) }
            val options = listOf("Número", "Nombre")


            ExposedDropdownMenuBox(
                modifier = Modifier
                    .padding(16.dp)
                    .width(200.dp),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ){
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = selectedOption.title,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ){
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedOption = DropdownItem(title = option)
                                    expanded = false
                                }
                            )
                        }
                    }

                }

            }

            var order by remember { mutableStateOf(itemList[0]) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround, // distribuye de manera pareja
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ordenar por:") // texto para claridad

                // radio boton para cada item
                itemList.forEach { item ->
                    Row(
                        // fila completa clicable, no solo el boton
                        modifier = Modifier
                            .selectable(
                                selected = (item == order), // Use 'order' to check if this item is selected
                                onClick = { order = item }  // Update 'order' when clicked
                            )
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (item == order), // se lee order
                            onClick = { order = item }  // se escribe order
                        )
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}