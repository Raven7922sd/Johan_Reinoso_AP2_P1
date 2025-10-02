package com.example.johan_reinoso_ap2_p1.Data.module

import android.content.Context
import androidx.room.Room
import com.example.johan_reinoso_ap2_p1.Data.db.EntradaDb
import com.example.johan_reinoso_ap2_p1.Data.local.EntradaDao
import com.example.johan_reinoso_ap2_p1.Data.repository.EntradaRepositoryImpl
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.DeleteEntradaUseCase
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.GetEntradaUseCase
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.ObserveEntradaUseCase
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.UpsertEntradaUseCase
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.ValidationEntradaUseCase
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Provides
@Singleton
fun provideEntradaDB(@ApplicationContext appContext: Context): EntradaDb {
    return Room.databaseBuilder(
        appContext,
        EntradaDb::class.java,
        "EntradaDb"
    ).fallbackToDestructiveMigration(false)
        .build()
}

@Provides
@Singleton
fun provideEntradaDao(EntradaDb: EntradaDb): EntradaDao {
    return EntradaDb.entradaDao()
}
    @Provides
    @Singleton
    fun provideEntradaRepositoryImpl(entradaDao: EntradaDao): EntradaRepositoryImpl {
        return EntradaRepositoryImpl(entradaDao)
    }

    @Provides
    @Singleton
    fun provideEntradaRepository(impl: EntradaRepositoryImpl): EntradaRepositoryImpl {
        return impl
    }

    @Provides
    @Singleton
    fun provideGetEntradaCase(repo: EntradaRepositoryImpl) = GetEntradaUseCase(repo)

    @Provides
    @Singleton
    fun provideUpsertEntradaUseCase(repo: EntradaRepositoryImpl) = UpsertEntradaUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteEntradaUseCase(repo: EntradaRepositoryImpl) = DeleteEntradaUseCase(repo)

    @Provides
    @Singleton
    fun provideObserveEntradaUseCase(repo: EntradaRepositoryImpl) = ObserveEntradaUseCase(repo)

    @Provides
    @Singleton
    fun provideValidationEntradaUseCase(repo: EntradaRepositoryImpl) = ValidationEntradaUseCase(repo)