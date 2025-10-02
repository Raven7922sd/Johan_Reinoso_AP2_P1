package com.example.johan_reinoso_ap2_p1.Data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Entradas")
class EntradaEntity (
    @PrimaryKey(autoGenerate = true )
    val EntradaId: Int=0,
    val Fecha: String,
    val ClienteNombre: String,
    val Cantidad: Int,
    val Precio: Double)
{}