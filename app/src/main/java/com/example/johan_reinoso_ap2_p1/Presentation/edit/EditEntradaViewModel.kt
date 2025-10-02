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
                cantidad = _state.value.entradasTotales,
                currentEntradaId = _state.value.id
            )

            _state.update {
                it.copy(nameError = result.nombreError)
            }
        }
    }
    private fun validateentrada(cantidad: Int?) {
        viewModelScope.launch {
            val result = validationEntradaUseCase(
                nombre = _state.value.name,
                cantidad = cantidad,
                currentEntradaId = _state.value.id
            )

            _state.update {
                it.copy(cantidadTotalError = result.entradaError)
            }
        }
    }

    fun onEvent(event: EditEntradaUiEvent){
        when(event){
            is EditEntradaUiEvent.Load -> onLoad(event.id)
            is EditEntradaUiEvent.NameChanged ->{
                _state.update {
                    it.copy(
                        name = event.value,
                        nameError = null
                    )
                }
                validateNombre(event.value)
            }
            is EditEntradaUiEvent.EntradaChanged ->{
                val entradaInt = event.value.toIntOrNull()
                _state.update {
                    it.copy(
                         = cantidadInt,
                        cantidadTotalesError = null
                    )
                }
                validateentrada(entradaInt)
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
                        cantidadTotal = entrada.Cantidad
                    )
                }
            }
        }
    }
    private fun onSave() {
        viewModelScope.launch {
            val entradaInt = state.value.cantidadTotal ?: return@launch
            val validationResult = validationEntradaUseCase(
                nombre = _state.value.name,
                entrada = _state.value.cantidadTotal,
                currentEntradaId = _state.value.id
            )

            if (!validationResult.isValid) {
                _state.update {
                    it.copy(
                        nameError = validationResult.nombreError,
                        error = validationResult.entradaError,
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
                    Cantidad = Cantidad
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