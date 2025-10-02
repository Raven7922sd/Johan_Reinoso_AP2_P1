package com.example.johan_reinoso_ap2_p1.Data.mapper

import com.example.johan_reinoso_ap2_p1.Data.local.EntradaEntity
import com.example.johan_reinoso_ap2_p1.Domain.model.Entrada

fun EntradaEntity.toDomain(): Entrada =
    Entrada(
        IdEntrada = this.EntradaId,
        NombreCliente = this.ClienteNombre,
        Cantidad = this.Cantidad,
        Precio = this.Precio,
        Fecha = this.Fecha
    )

fun Entrada.toEntity(): EntradaEntity=
    EntradaEntity(
        EntradaId = IdEntrada,
        ClienteNombre = NombreCliente,
        Cantidad = Cantidad,
        Precio = Precio,
        Fecha = Fecha
    )