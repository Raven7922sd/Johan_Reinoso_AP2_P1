package com.example.johan_reinoso_ap2_p1.Presentation.list

import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada

data class ListEntradaUiState(
    val isLoading: Boolean = false,
    val entradas: List<Entrada> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
) {
}