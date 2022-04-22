package com.livepokernotes.framework.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.livepokernotes.framework.datasource.cache.model.SessionCacheEntity

@Database(entities = [SessionCacheEntity::class], version = 1)
abstract class SessionDatabase: RoomDatabase() {

    abstract fun sessionDao(): SessionDao

    companion object{
        const val DATABASE_NAME = "session_db"
    }
}