package com.corporation8793.aivision

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.corporation8793.aivision.excel.Excel
import com.corporation8793.aivision.room.AppDatabase
import com.corporation8793.aivision.room.Course

class Application(context: Context) : Application() {
    private val excel : Excel = Excel(context, "gj_spot.xls")

    private val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "CourseDB"
    ).build()

    fun xlsToRoom() {
        val xlsData : List<Array<String>> = excel.extractTotalSheet(arrayOf("A", "B", "C", "D", "E", "F", "G", "H"))
        val roomData : List<Course> = mutableListOf()
        for (xd in xlsData) {
            roomData.plus(Course(
                courseType = xd[0],
                courseName = xd[1],
                courseImgName = xd[2],
                courseTitle = xd[3],
                courseContent = xd[4],
                courseLatitude = xd[5],
                courseLongitude = xd[6],
                courseURL = xd[7]
            ))
        }
        db.courseDao().insertAll(roomData)
    }
}