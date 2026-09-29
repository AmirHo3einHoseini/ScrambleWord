package com.example.scrambleword.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scrambleword.data.local.converter.Converters
import com.example.scrambleword.data.local.dao.WordDao
import com.example.scrambleword.data.local.entity.WordEntity

@Database(
    entities = [WordEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class WordDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao
}