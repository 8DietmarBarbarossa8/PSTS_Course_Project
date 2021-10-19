package com.company.dima.studentsfirebase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    var name: String = "",
    var age: Int = 0,
    var rating: Float = 0.0f,
    var pathKey: String = ""
) : Parcelable