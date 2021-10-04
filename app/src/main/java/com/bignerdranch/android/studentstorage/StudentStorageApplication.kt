package com.bignerdranch.android.studentstorage

import android.app.Application
import com.bignerdranch.android.studentstorage.database.StudentRepository

class StudentStorageApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StudentRepository.initialize(this)
    }
}