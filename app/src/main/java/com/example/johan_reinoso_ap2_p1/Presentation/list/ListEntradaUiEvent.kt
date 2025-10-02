package com.example.johan_reinoso_ap2_p1.Presentation.list

sealed interface ListEntradaUiEvent {
    data object Load: ListEntradaUiEvent
    data object CreateNewEntrada: ListEntradaUiEvent

    data class Delete(val id: Int): ListEntradaUiEvent
    data class Edit(val id: Int): ListEntradaUiEvent
    data class ShowMessage(val message: String): ListEntradaUiEvent


}