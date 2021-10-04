package com.bignerdranch.android.studentstorage.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String = "",
    var age: Int = 0,
    var rating: Float = 0.0f
)