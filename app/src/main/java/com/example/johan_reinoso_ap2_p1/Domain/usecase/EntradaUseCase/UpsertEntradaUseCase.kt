package com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase

import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada
import com.example.johan_reinoso_ap2_p1.Domain.repository.EntradaRepository

class UpsertEntradaUseCase(
    private val repository: EntradaRepository
) {
    suspend operator fun invoke(entrada: Entrada): Result<Int> {

        if (entrada.NombreCliente.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío"))
        }

        if (entrada.NombreCliente.length>50){
            return Result.failure(IllegalArgumentException("El nombre no puede tener más de 50 caracteres"))
        }

        if (entrada.Cantidad < 0) {
            return Result.failure(IllegalArgumentException("La cantidad no puede ser negativa"))
        }
        if(entrada.Precio < 0.0){
            return Result.failure(IllegalArgumentException("El precio no puede ser negativo"))
        }

        if(!entrada.Fecha.matches(Regex("""\d{4}-\d{2}-\d{2}"""))){
            return Result.failure(IllegalArgumentException("La fecha debe tener el formato YYYY-MM-DD"))
        }

        return runCatching {
            repository.upsertEntrada(entrada)
        }
    }
}