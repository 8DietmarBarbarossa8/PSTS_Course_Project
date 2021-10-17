package com.bignerdranch.android.studentstorage.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.studentstorage.model.Student
import com.google.firebase.database.FirebaseDatabase
import java.util.*
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

    private val firebase = FirebaseDatabase.getInstance()

    fun getStudent(id: UUID): LiveData<Student?> = studentDao.getStudent(id)

    fun getStudents(sortMode: String): LiveData<List<Student>> = studentDao.getStudents(sortMode)

    fun updateStudent(student: Student) {
        executor.execute {
            studentDao.updateStudent(student)
        }
        firebase.reference.child(PATH_KEY_STUDENT).child(student.pathKey).setValue(student)
    }

    fun addStudent(student: Student) {
        executor.execute {
            studentDao.addStudent(student)
        }
        student.pathKey = firebase.reference.push().key ?: ""
        firebase.reference.child(PATH_KEY_STUDENT).child(student.pathKey).setValue(student)
    }

    fun deleteStudent(student: Student) {
        executor.execute {
            studentDao.deleteStudent(student)
        }
        firebase.reference.child(PATH_KEY_STUDENT).child(student.pathKey).removeValue()
    }

    companion object {
        private const val DATABASE_NAME = "Student-database"
        private const val PATH_KEY_STUDENT = "STUDENT_DB"

        private var INSTANCE: StudentRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = StudentRepository(context)
        }

        fun get(): StudentRepository {
            return INSTANCE ?: throw IllegalStateException("Repository must be initialized")
        }
    }
}