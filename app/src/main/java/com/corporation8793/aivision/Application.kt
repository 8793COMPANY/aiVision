package com.corporation8793.aivision

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.corporation8793.aivision.excel.Excel
import com.corporation8793.aivision.room.AppDatabase
import com.corporation8793.aivision.room.Course

class Application : Application() {
    lateinit var context: Context
    private lateinit var excel: Excel
    private lateinit var db : AppDatabase

    fun getInstance(c: Context) : com.corporation8793.aivision.Application {
        context = c
        excel = Excel(c, "gj_spot.xls")
        db = Room.databaseBuilder(
            c,
            AppDatabase::class.java, "CourseDB"
        ).build()

        return this
    }

    fun xlsToRoom() {
        val xlsData : List<Array<String>> = excel.extractTotalSheet(arrayOf("A", "B", "C", "D", "E", "F", "G", "H"))

        val roomData : MutableList<Course> = mutableListOf()
        for (row in xlsData.indices) {
            roomData.add(Course(
                courseType = xlsData[row][0],
                courseName = xlsData[row][1],
                courseImgName = xlsData[row][2],
                courseTitle = xlsData[row][3],
                courseContent = xlsData[row][4],
                courseLatitude = xlsData[row][5],
                courseLongitude = xlsData[row][6],
                courseURL = xlsData[row][7]
            ))
        }

        Thread {
            for (i in roomData.indices) {
                if (db.courseDao().findByCourseName(roomData[i].courseName) != null) {
                    Log.i("Application", "DB Data Already Exist !! -> ${roomData[i].courseName}")
                } else {
                    db.courseDao().insertAll(roomData[i])
                    Log.i("Application", "DB Data Successful Inserted !! -> ${roomData[i]}")
                }
            }

            Log.i("Application", "<<=== xlsToRoom - DB All Out Start ===>>")

            val dataLog = db.courseDao().getAll()
            for (DL in dataLog) {
                Log.i("Application", "$DL")
            }

            Log.i("Application", "<<=== xlsToRoom - DB All Out End ===>>")
        }.start()
    }
}