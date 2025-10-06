package com.example.johan_reinoso_ap2_p1.Data.module

import android.content.Context
import androidx.room.Room
import com.example.johan_reinoso_ap2_p1.Data.db.EntradaDb
import com.example.johan_reinoso_ap2_p1.Data.local.EntradaDao
import com.example.johan_reinoso_ap2_p1.Data.repository.EntradaRepositoryImpl
import com.example.johan_reinoso_ap2_p1.Domain.repository.EntradaRepository
import com.example.johan_reinoso_ap2_p1.Domain.usecase.EntradaUseCase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

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
    fun provideEntradaDao(entradaDb: EntradaDb): EntradaDao {
        return entradaDb.entradaDao()
    }

    @Provides
    @Singleton
    fun provideEntradaRepositoryImpl(entradaDao: EntradaDao): EntradaRepositoryImpl {
        return EntradaRepositoryImpl(entradaDao)
    }

    @Provides
    @Singleton
    fun provideEntradaRepository(impl: EntradaRepositoryImpl): EntradaRepository = impl

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
}
