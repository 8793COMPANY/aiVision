package com.corporation8793.aivision.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
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

    @Query("SELECT * FROM course WHERE courseName == :courseName")
    fun findCourseData(courseName: String) : Course


    @Query("SELECT * FROM course WHERE courseType != :courseName")
    fun withoutCourseType(courseName: String) : List<Course>

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg course: Course)

    @Update
    fun update(vararg course: Course)
}