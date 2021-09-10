package com.corporation8793.aivision.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CourseDao {
    @Query("SELECT * FROM course")
    fun getAll(): List<Course>

    @Query("SELECT * FROM course WHERE courseType == :courseType")
    fun getAllByCourseType(courseType: String): List<Course>

    @Query("SELECT * FROM course WHERE courseName == :courseName")
    fun findByCourseName(courseName: String) : List<Course>

    @Query("SELECT * FROM course WHERE uid == :position")
    fun findPosData(position: Int) : Course

    @Insert
    fun insertAll(vararg course: Course)

    @Update
    fun update(vararg course: Course)
}