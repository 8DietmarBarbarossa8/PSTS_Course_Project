package com.bignerdranch.android.studentstorage.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.studentstorage.model.Student
import java.util.concurrent.Executors

class StudentRepository private constructor(context: Context) {
    private val database: StudentDatabase = Room.databaseBuilder(
        context.applicationContext,
        StudentDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val studentDao: StudentDao
        get() = database.studentDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun getStudent(id: Int): LiveData<Student?> = studentDao.getStudent(id)

    fun getStudents(sortMode: String): LiveData<List<Student>> = studentDao.getStudents(sortMode)

    fun updateStudent(student: Student) {
        executor.execute {
            studentDao.updateStudent(student)
        }
    }

    fun addStudent(student: Student) {
        executor.execute {
            studentDao.addStudent(student)
        }
    }

    fun deleteStudent(student: Student) {
        executor.execute {
            studentDao.deleteStudent(student)
        }
    }

    companion object {
        private const val DATABASE_NAME = "Student-database"
        private var INSTANCE: StudentRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = StudentRepository(context)
            }
        }

        fun get(): StudentRepository {
            return INSTANCE ?: throw IllegalStateException("StudentRepository must be initialized")
        }
    }
}