package com.bignerdranch.android.studentstorage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.studentstorage.model.Student

@Database(entities = [Student::class], version = 1)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}