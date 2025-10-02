package com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase

import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada
import com.example.johan_reinoso_ap2_p1.Domain.repository.EntradaRepository

class GetEntradaUseCase(
    private val repository: EntradaRepository
) {
    suspend operator fun invoke(id: Int): Entrada? {
        return repository.getEntrada(id)
    }
}