package com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase

import com.example.johan_reinoso_ap2_p1.Domain.repository.EntradaRepository

class DeleteEntradaUseCase(
    private val repository: EntradaRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteEntrada(id)
    }
}