package com.example.johan_reinoso_ap2_p1.Data.repository

import com.example.johan_reinoso_ap2_p1.Data.local.EntradaDao
import com.example.johan_reinoso_ap2_p1.Data.mapper.toDomain
import com.example.johan_reinoso_ap2_p1.Data.mapper.toEntity
import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada
import com.example.johan_reinoso_ap2_p1.Domain.repository.EntradaRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EntradaRepositoryImpl @Inject constructor(
    private val entradaDao: EntradaDao
) : EntradaRepository {

    override fun ObserveEntrada(): Flow<List<Entrada>> {
        return entradaDao.observeEntrada().map { entities ->
            entities.map { it.toDomain() }}
    }

    override suspend fun getEntrada(id: Int): Entrada? {
        return entradaDao.getById(id)?.toDomain()
    }
    override suspend fun upsertEntrada(entrada: Entrada): Int {
        val entity = entrada.toEntity()
        val result = entradaDao.upsert(entity)
        return if (entrada.IdEntrada == 0) result.toInt() else entrada.IdEntrada
    }

    override suspend fun deleteEntrada(id: Int) {
        entradaDao.deleteById(id)
    }

    override suspend fun getAllEntrada(): List<Entrada> {
        return entradaDao.getAllEntradas().map { it.toDomain() }
    }
}