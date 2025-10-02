package com.example.johan_reinoso_ap2_p1.Presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.DeleteEntradaUseCase
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.ObserveEntradaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ListEntradaViewModel @Inject constructor(
    private val observeEntradaUseCase: ObserveEntradaUseCase,
    private val deleteEntradaCase: DeleteEntradaUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ListEntradaUiState(isLoading = true))
    val state: StateFlow<ListEntradaUiState> = _state.asStateFlow()

    init {
        onEvent(ListEntradaUiEvent.Load)
    }

    fun onEvent(event: ListEntradaUiEvent) {
        when (event) {
            ListEntradaUiEvent.Load -> observeEntradas()
            is ListEntradaUiEvent.Delete -> onDelete(event.id)
            ListEntradaUiEvent.CreateNewEntrada -> _state.update { it.copy(navigateToCreate = true) }
            is ListEntradaUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is ListEntradaUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }

    private fun observeEntradas() {
        viewModelScope.launch {
            observeEntradaUseCase().collectLatest { entradaList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        entradas = entradaList,
                        message = null
                    )
                }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            try {
                deleteEntradaCase(id)
                onEvent(ListEntradaUiEvent.ShowMessage("Entrada eliminada"))
            } catch (e: Exception) {
                onEvent(ListEntradaUiEvent.ShowMessage("Error al eliminar: ${e.message}"))
            }
        }
    }

    fun onNavigationHandled() {
        _state.update {
            it.copy(
                navigateToCreate = false,
                navigateToEditId = null,
                message = null
            )
        }
    }
}