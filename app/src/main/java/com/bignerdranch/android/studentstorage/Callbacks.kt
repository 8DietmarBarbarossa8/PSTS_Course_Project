package com.bignerdranch.android.studentstorage

interface Callbacks {
    fun onCreateNewStudent()
    fun onStudentSelected(studentID: Int)
    fun onSortSelected()
    fun onMainScreen(sortingMode: String = "")
}