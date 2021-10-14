package com.bignerdranch.android.studentstorage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.studentstorage.database.StudentRepository
import com.bignerdranch.android.studentstorage.model.Student
import java.util.*

class StudentDetailViewModel : ViewModel() {
    private val studentRepository = StudentRepository.get()
    private val studentIdLiveData = MutableLiveData<UUID>()

    val studentLiveData: LiveData<Student?> =
        Transformations.switchMap(studentIdLiveData) { studentID ->
            studentRepository.getStudent(studentID)
        }

    fun loadStudent(studentID: UUID) {
        studentIdLiveData.value = studentID
    }

    fun addStudent(student: Student) {
        studentRepository.addStudent(student)
    }

    fun saveStudent(student: Student) {
        studentRepository.updateStudent(student)
    }

    fun deleteStudent(student: Student) {
        studentRepository.deleteStudent(student)
    }
}