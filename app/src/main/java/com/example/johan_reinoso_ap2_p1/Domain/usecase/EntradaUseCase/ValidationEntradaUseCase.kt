package com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase

import com.example.johan_reinoso_ap2_p1.Domain.repository.EntradaRepository
import jakarta.inject.Inject

class ValidationEntradaUseCase @Inject constructor(
    private val entradaRepository: EntradaRepository
) {
    data class ValidationResult(
        val isValid: Boolean,
        val nombreError: String? = null,
        val fechaError: String? = null,
        val cantidadError: String? = null,
        val precioError: String? = null
    )

    suspend operator fun invoke(
        nombre: String,
        fecha: String,
        cantidad: Int?,
        precio: Double?,
        currentEntradaId: Int? = null
    ): ValidationResult {

        val nombreError = when {
            nombre.isBlank() -> "El nombre es requerido"
            else -> {
                val cleanedName = nombre.trim().normalize()
                val existingEntradas = entradaRepository.
                getAllEntrada().filter { it.NombreCliente.trim().normalize()== cleanedName }
                val isDuplicate = if (currentEntradaId != null) {
                    existingEntradas.any { it.IdEntrada != currentEntradaId }
                } else {
                    existingEntradas.isNotEmpty()
                }
                if (isDuplicate) "Ese nombre ya existe" else null
            }
        }

        val fechaError= when {
            fecha.isBlank() -> "La fecha es requerida"
            !fecha.matches(Regex("""\d{4}-\d{2}-\d{2}""")) -> "La fecha debe tener el formato YYYY-MM-DD"
            else -> null
        }

        val cantidadError = when {
            cantidad == null -> "La cantidad es requerida"
            cantidad < 0 -> "La cantidad no puede ser negativa"
            else -> null
        }

        val precioError = when {
            precio == null -> "El precio es requerido"
            precio < 0.0 -> "El precio no puede ser negativo"
            else -> null
        }

        return ValidationResult(
            isValid = nombreError == null && cantidadError == null && fechaError == null && precioError == null,
            nombreError = nombreError,
            fechaError = fechaError,
            cantidadError = cantidadError,
            precioError = precioError
        )
    }
}