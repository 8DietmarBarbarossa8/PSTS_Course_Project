package com.bignerdranch.android.studentstorage.viewmodel

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.studentstorage.database.StudentRepository

class StudentListViewModel : ViewModel() {
    private val studentRepository = StudentRepository.get()
    var studentListLiveData = studentRepository.getStudents("name")

    fun sortStudents(modeOfSort: String) {
        studentListLiveData = studentRepository.getStudents(modeOfSort)
    }
}