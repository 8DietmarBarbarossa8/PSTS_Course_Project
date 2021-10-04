package com.bignerdranch.android.studentstorage.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.studentstorage.model.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM student WHERE id=(:id)")
    fun getStudent(id: Int): LiveData<Student?>

    @Query(
        "SELECT * FROM Student ORDER BY " +
                "CASE WHEN :sortMode = 'name' THEN name END ASC," +
                "CASE WHEN :sortMode = 'age' THEN age END ASC," +
                "CASE WHEN :sortMode = 'rating' THEN rating END DESC"
    )
    fun getStudents(sortMode: String): LiveData<List<Student>>

    @Update
    fun updateStudent(student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)
}