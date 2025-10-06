package com.example.johan_reinoso_ap2_p1.Presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.DeleteEntradaUseCase
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.GetEntradaUseCase
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.UpsertEntradaUseCase
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.ValidationEntradaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEntradaViewModel @Inject constructor(
    private val getEntradaUseCase: GetEntradaUseCase,
    private val upsertEntradaUseCase: UpsertEntradaUseCase,
    private val deleteEntradaUseCase: DeleteEntradaUseCase,
    private val validationEntradaUseCase: ValidationEntradaUseCase
): ViewModel(){
    private val _state = MutableStateFlow(EditEntradaUiState())
    val state: StateFlow<EditEntradaUiState> = _state.asStateFlow()

    private fun validateNombre(nombre: String) {
        viewModelScope.launch {
            val result = validationEntradaUseCase(
                nombre = nombre,
                fecha = _state.value.fecha,
                cantidad = _state.value.cantidadTotal,
                precio = _state.value.precioTotal,
                currentEntradaId = _state.value.id
            )
            _state.update { it.copy(nameError = result.nombreError) }
        }
    }

    private fun validateEntrada(cantidad: Int?) {
        viewModelScope.launch {
            val result = validationEntradaUseCase(
                nombre = _state.value.name,
                fecha = _state.value.fecha,
                cantidad = cantidad,
                precio = _state.value.precioTotal,
                currentEntradaId = _state.value.id
            )
            _state.update { it.copy(cantidadTotalError = result.cantidadError) }
        }
    }

    private fun validatePrecio(precio: Double?) {
        viewModelScope.launch {
            val result = validationEntradaUseCase(
                nombre = _state.value.name,
                fecha = _state.value.fecha,
                cantidad = _state.value.cantidadTotal,
                precio = precio,
                currentEntradaId = _state.value.id
            )
            _state.update { it.copy(precioTotalError = result.precioError) }
        }
    }

    private fun validateFecha(fecha: String) {
        viewModelScope.launch {
            val result = validationEntradaUseCase(
                nombre = _state.value.name,
                fecha = fecha,
                cantidad = _state.value.cantidadTotal,
                precio = _state.value.precioTotal,
                currentEntradaId = _state.value.id
            )
            _state.update { it.copy(fechaError = result.fechaError) }
        }
    }

    fun onEvent(event: EditEntradaUiEvent) {
        when (event) {
            is EditEntradaUiEvent.Load -> onLoad(event.id)
            is EditEntradaUiEvent.NameChanged -> {
                _state.update {
                    it.copy(
                        name = event.value,
                        nameError = null
                    )
                }
                validateNombre(event.value)
            }
            is EditEntradaUiEvent.EntradaChanged -> {
                val entradaInt = event.value.toIntOrNull()
                _state.update {
                    it.copy(
                        cantidadTotal = entradaInt,
                        cantidadTotalError = null
                    )
                }
                validateEntrada(entradaInt)
            }
            is EditEntradaUiEvent.PrecioChanged -> {
                val precioDouble = event.value.toDoubleOrNull()
                _state.update {
                    it.copy(
                        precioTotal = precioDouble,
                        precioTotalError = null
                    )
                }
                validatePrecio(precioDouble)
            }
            is EditEntradaUiEvent.FechaChanged -> {
                _state.update {
                    it.copy(
                        fecha = event.value,
                        fechaError = null
                    )
                }
                validateFecha(event.value)
            }
            EditEntradaUiEvent.Save -> onSave()
            EditEntradaUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, id = null) }
            return
        }
        viewModelScope.launch {
            val entrada = getEntradaUseCase(id)
            if (entrada != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        id = entrada.IdEntrada,
                        name = entrada.NombreCliente,
                        cantidadTotal = entrada.Cantidad,
                        precioTotal = entrada.Precio,
                        fecha = entrada.Fecha,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val validationResult = validationEntradaUseCase(
                nombre = _state.value.name,
                fecha = _state.value.fecha,
                cantidad = _state.value.cantidadTotal,
                precio = _state.value.precioTotal,
                currentEntradaId = _state.value.id
            )

            if (!validationResult.isValid) {
                _state.update {
                    it.copy(
                        nameError = validationResult.nombreError,
                        cantidadTotalError = validationResult.cantidadError,
                        fechaError = validationResult.fechaError,
                        precioTotalError = validationResult.precioError,
                        isSaving = false
                    )
                }
                return@launch
            }

            _state.update { it.copy(isSaving = true) }
            try {
                val entrada = Entrada(
                    IdEntrada = _state.value.id ?: 0,
                    NombreCliente = _state.value.name,
                    Cantidad = _state.value.cantidadTotal ?: 0,
                    Precio = _state.value.precioTotal ?: 0.0,
                    Fecha = _state.value.fecha
                )

                upsertEntradaUseCase(entrada)
                _state.update {
                    it.copy(
                        isSaving = false,
                        isEntradaSaved = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        nameError = "Error al guardar: ${e.message}"
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val id = _state.value.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteEntradaUseCase(id)
                _state.update { it.copy(isDeleting = false, isEntradaDeleted = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        nameError = "Error al eliminar: ${e.message}"
                    )
                }
            }
        }
    }
}
