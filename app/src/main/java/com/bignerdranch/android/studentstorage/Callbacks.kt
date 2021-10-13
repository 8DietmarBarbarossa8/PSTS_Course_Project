package com.bignerdranch.android.studentstorage

interface Callbacks {
    fun onCreateNewStudent()
    fun onStudentSelected(studentID: Int)
    fun onSettings()
    fun onMainScreen(sortingMode: String = "")
}