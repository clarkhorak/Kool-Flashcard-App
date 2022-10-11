package com.example.koolflashcardapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Flashcard(
    @ColumnInfo(name = "question") val question: String?,
    @ColumnInfo(name = "answer") val answer: String?,
    @ColumnInfo(name = "wrong_answer_1") val wrongAnswer1: String? = null,
    @ColumnInfo(name = "wrong_answer_2") val wrongAnswer2: String? = null,
    @PrimaryKey(autoGenerate = true) var uuid: Int = 0,
)
