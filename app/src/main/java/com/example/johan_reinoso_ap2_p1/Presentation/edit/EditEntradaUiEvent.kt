package com.example.johan_reinoso_ap2_p1.Presentation.edit

sealed interface EditEntradaUiEvent {
    data class Load(val id: Int?) : EditEntradaUiEvent
    data class NameChanged(val value: String) : EditEntradaUiEvent

    data class EntradaChanged(val value: String) : EditEntradaUiEvent
    data object Save : EditEntradaUiEvent
    data object Delete : EditEntradaUiEvent
}