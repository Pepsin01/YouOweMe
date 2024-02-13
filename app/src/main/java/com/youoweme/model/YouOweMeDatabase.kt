package com.youoweme.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Database(entities = [EventEntity::class, DebtEntity::class, TransactionEntity::class], version = 1)
abstract class YouOweMeDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun debtDao(): DebtDao
    abstract fun transactionDao(): TransactionDao
}

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): YouOweMeDatabase {
        return Room.databaseBuilder(
            appContext,
            YouOweMeDatabase::class.java,
            "you_owe_me.db"
        )
        .build()
    }
}
