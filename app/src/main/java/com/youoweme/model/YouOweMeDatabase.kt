package com.youoweme.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.youoweme.model.event.EventDao
import com.youoweme.model.event.EventEntity
import com.youoweme.model.person.PersonDao
import com.youoweme.model.person.PersonEntity
import com.youoweme.model.transaction.TransactionDao
import com.youoweme.model.transaction.TransactionEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [EventEntity::class, TransactionEntity::class, PersonEntity::class], version = 1)
abstract class YouOweMeDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun transactionDao(): TransactionDao
    abstract fun personDao(): PersonDao
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
