package com.example.alias.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        WordEnglishEntity::class,
        WordGeorgianEntity::class,
        WordRussianEntity::class
    ]
)
abstract class WordsDatabase : RoomDatabase() {
    abstract val wordsDao: WordsDao

    companion object {
        @Volatile
        private var INSTANCE: WordsDatabase? = null

        fun getInstance(context: Context): WordsDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    WordsDatabase::class.java,
                    "words"
                )
                    .createFromAsset("databases/words.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}
