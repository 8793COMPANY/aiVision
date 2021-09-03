package com.corporation8793.aivision.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Course(
    @PrimaryKey val uid : Int,
    @ColumnInfo(name = "courseType") val courseType : String? = null,
    @ColumnInfo(name = "courseName") val courseName : String? = null,
    @ColumnInfo(name = "courseImgName") val courseImgName : String? = null,
    @ColumnInfo(name = "courseTitle") val courseTitle : String? = null,
    @ColumnInfo(name = "courseContent") val courseContent : String? = null,
    @ColumnInfo(name = "courseLatitude") val courseLatitude : String? = null,
    @ColumnInfo(name = "courseLongitude") val courseLongitude : String? = null,
    @ColumnInfo(name = "courseURL") val courseURL : String? = null
)