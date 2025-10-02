package com.example.johan_reinoso_ap2_p1.Data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EntradaDao {

    @Query("SELECT*FROM entradas ORDER BY EntradaId DESC")
        fun observeEntrada(): Flow<List<EntradaEntity>>

    @Query("SELECT*FROM entradas WHERE EntradaId=:id")
        suspend fun getById(id:Int): EntradaEntity?

    @Query("SELECT*FROM entradas WHERE LOWER(ClienteNombre) = LOWER(:nombre)")
    suspend fun getByName(nombre: String): List<EntradaEntity>

    @Upsert
    suspend fun upsert(entrada: EntradaEntity):Long

    @Delete
    suspend fun delete(entity: EntradaEntity)

    @Query("DELETE FROM entradas WHERE EntradaId=:id")
    suspend fun deleteById(id:Int)

    @Query("SELECT * FROM Entradas")
    suspend fun getAllEntradas(): List<EntradaEntity>
}