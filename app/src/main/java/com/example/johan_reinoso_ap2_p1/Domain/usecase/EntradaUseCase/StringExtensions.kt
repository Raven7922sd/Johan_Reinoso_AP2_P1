package com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase

import java.text.Normalizer

fun String.normalize(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(temp, "").lowercase()
}