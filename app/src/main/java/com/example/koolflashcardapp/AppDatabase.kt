package com.example.koolflashcardapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.koolflashcardapp.Flashcard
import com.example.koolflashcardapp.FlashcardDao

@Database(entities = [Flashcard::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
}
