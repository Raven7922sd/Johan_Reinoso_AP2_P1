package com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase

import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada
import com.example.johan_reinoso_ap2_p1.Domain.repository.EntradaRepository
import kotlinx.coroutines.flow.Flow

class ObserveEntradaUseCase(
    private val repository: EntradaRepository
) {
    operator fun invoke(): Flow<List<Entrada>> {
        return repository.ObserveEntrada()
    }
}