package com.example.johan_reinoso_ap2_p1.Data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.johan_reinoso_ap2_p1.Data.local.EntradaDao
import com.example.johan_reinoso_ap2_p1.Data.local.EntradaEntity

@Database(entities = [EntradaEntity::class],
    version = 2,
    exportSchema = false)

abstract class EntradaDb: RoomDatabase() {

    abstract fun entradaDao(): EntradaDao
}
