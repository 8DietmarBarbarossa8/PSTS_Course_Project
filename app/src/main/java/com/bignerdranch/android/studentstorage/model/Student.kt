package com.bignerdranch.android.studentstorage.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Student(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var name: String = "",
    var age: Int = 0,
    var rating: Float = 0.0f,
    var date: Date = Date(),
    var pathKey: String = ""
)