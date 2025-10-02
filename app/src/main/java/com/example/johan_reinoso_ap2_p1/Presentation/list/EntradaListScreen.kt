package com.example.johan_reinoso_ap2_p1.Presentation.list
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntradaListScreen(
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: ListEntradaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Donaciones",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF7E57C2

                    )
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToCreate() }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir donación")
            }
        }
    ) { paddingValues ->
        EntradaListContent(
            state = state,
            onEvent = { event ->
                when (event) {
                    is ListEntradaUiEvent.Edit -> onNavigateToEdit(event.id)
                    is ListEntradaUiEvent.Delete -> viewModel.onEvent(event)
                    else -> Unit
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun EntradaListContent(
    state: ListEntradaUiState,
    onEvent: (ListEntradaUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var entradaToDelete by remember { mutableStateOf<Entrada?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> CircularProgressIndicator()
            state.entradas.isEmpty() -> Text(
                text = "No hay donaciones registradas",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.entradas, key = { it.IdEntrada }) { entrada ->
                    EntradaCard(
                        entrada = entrada,
                        onClick = { onEvent(ListEntradaUiEvent.Edit(entrada.IdEntrada)) },
                        onDelete = { entradaToDelete = entrada }
                    )
                }
            }
        }

        if (entradaToDelete != null) {
            AlertDialog(
                onDismissRequest = { entradaToDelete = null },
                title = { Text("Eliminar donación") },
                text = { Text("¿Estás seguro de eliminar esta donación?") },
                confirmButton = {
                    TextButton(onClick = {
                        onEvent(ListEntradaUiEvent.Delete(entradaToDelete!!.IdEntrada))
                        entradaToDelete = null
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { entradaToDelete = null }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
private fun EntradaCard(
    entrada: Entrada,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Nombre: ${entrada.NombreCliente}")
                Text("Partidas: ${entrada.Cantidad}")
                Text("Partidas: ${entrada.Precio}")
                Text("Partidas: ${entrada.Fecha}")
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar")
            }
        }
    }
}