package com.example.johan_reinoso_ap2_p1.Domain.repository

import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada
import kotlinx.coroutines.flow.Flow

interface EntradaRepository {
    fun ObserveEntrada(): Flow<List<Entrada>>
    suspend fun getEntrada(id:Int): Entrada?
    suspend fun getAllEntrada():List<Entrada>
    suspend fun upsertEntrada(entrada: Entrada):Int
    suspend fun deleteEntrada(id:Int)
}