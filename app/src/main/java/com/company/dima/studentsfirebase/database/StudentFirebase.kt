package com.company.dima.studentsfirebase.database

import com.company.dima.studentsfirebase.model.Student
import com.google.firebase.database.FirebaseDatabase

object StudentFirebase {
    private const val KEY = "STUDENT_DB"
    private val firebase = FirebaseDatabase.getInstance()

    val reference = firebase.getReference(KEY)
    val students = arrayListOf<Student>()

    fun addStudent(student: Student) {
        student.pathKey = firebase.reference.push().key ?: ""
        firebase.reference.child(KEY).child(student.pathKey).setValue(student)
    }

    fun deleteStudent(student: Student) {
        firebase.reference.child(KEY).child(student.pathKey).removeValue()
    }

    fun updateStudent(student: Student) {
        firebase.reference.child(KEY).child(student.pathKey).setValue(student)
    }

    fun sortStudents(sortMode: String) {
        when (sortMode) {
            "name" -> students.sortBy { it.name }
            "age" -> students.sortBy { it.age }
            "rating" -> students.sortBy { it.rating }
            else -> students.sortBy { it.pathKey }
        }
    }
}