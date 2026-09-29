package com.example.scrambleword.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                ALTER TABLE words
                ADD COLUMN category TEXT NOT NULL DEFAULT 'عمومی'
            """.trimIndent()
        )
    }
}