package com.bignerdranch.android.studentstorage

import java.util.UUID

interface Callbacks {
    fun onCreateNewStudent()
    fun onStudentSelected(studentID: UUID)
    fun onSettings()
    fun onMainScreen(sortingMode: String = "")
}