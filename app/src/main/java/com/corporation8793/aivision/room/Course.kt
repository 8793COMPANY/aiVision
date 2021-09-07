package com.corporation8793.aivision.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Course(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "courseType") val courseType: String,
    @ColumnInfo(name = "courseName") val courseName: String,
    @ColumnInfo(name = "courseImgName") val courseImgName: String,
    @ColumnInfo(name = "courseTitle") val courseTitle: String,
    @ColumnInfo(name = "courseContent") val courseContent: String,
    @ColumnInfo(name = "courseLatitude") val courseLatitude: String,
    @ColumnInfo(name = "courseLongitude") val courseLongitude: String,
    @ColumnInfo(name = "courseURL") val courseURL: String
)