package com.example.johan_reinoso_ap2_p1.Presentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntradaScreen(
    EntradaId: Int?,
    navController: NavController,
    viewModel: EditEntradaViewModel = hiltViewModel()
) {
    LaunchedEffect(EntradaId) {
        viewModel.onEvent(EditEntradaUiEvent.Load(EntradaId))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaving) {
        if (state.isSaving) {
            navController.popBackStack()
        }
    }

    EditEntradaBody(state, viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditEntradaBody(
    state: EditEntradaUiState,
    onEvent: (EditEntradaUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Datos de la entrada",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF7E57C2)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onEvent(EditEntradaUiEvent.NameChanged(it)) },
                label = { Text("Nombre") },
                isError = state.nameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.nameError != null) {
                Text(
                    state.nameError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.cantidadTotal?.toString() ?: "",
                onValueChange = { onEvent(EditEntradaUiEvent.EntradaChanged(it)) },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.cantidadTotalError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.cantidadTotalError != null) {
                Text(
                    state.cantidadTotalError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.precioTotal?.toString() ?: "",
                onValueChange = { onEvent(EditEntradaUiEvent.PrecioChanged(it)) },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = state.precioTotalError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.precioTotalError != null) {
                Text(
                    state.precioTotalError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.fecha,
                onValueChange = { onEvent(EditEntradaUiEvent.FechaChanged(it)) },
                label = { Text("Fecha (YYYY-MM-DD)") },
                isError = state.fechaError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.fechaError != null) {
                Text(
                    state.fechaError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { onEvent(EditEntradaUiEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
                Spacer(Modifier.width(16.dp))
                if (!state.isNew) {
                    OutlinedButton(
                        onClick = { onEvent(EditEntradaUiEvent.Delete) },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditEntradaBodyPreview() {
    val state = EditEntradaUiState()
    MaterialTheme {
        EditEntradaBody(state = state) {}
    }
}
