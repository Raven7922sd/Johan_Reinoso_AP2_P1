package com.example.johan_reinoso_ap2_p1.Presentation.edit

import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada

data class EditEntradaUiState(
    val id: Int? = null,
    val name: String = "",
    val cantidadTotal: Int? = null,
    val nameError: String? = null,
    val cantidadTotalError: String? = null,
    val precioTotal: Double? = null,
    val precioTotalError: String? = null,
    val fecha: String = "",
    val fechaError: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEntradaSaved: Boolean = false,
    val isDeleting: Boolean = false,
    val isEntradaDeleted: Boolean = false,
    val isNew: Boolean = true,
)