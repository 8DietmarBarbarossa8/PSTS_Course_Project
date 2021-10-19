package com.company.dima.studentsfirebase.inter_face

interface Callbacks {
    fun onCreateNewStudent()
    fun onStudentSelected(pathKey: String)
    fun onSettings()
    fun onMainScreen(sortingMode: String = "")
}