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

        Log.i("Application.kt", "$xlsData")


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
            Log.i("Application.kt", "")
        }

        Thread {
            for (i in roomData.indices) {
//                if (db.courseDao().findByCourseName(rd.courseName) != null) {
//                    db.courseDao().update(rd)
//                } else {
//                    db.courseDao().insertAll(rd)
//                }
                db.courseDao().insertAll(roomData[i])
                Log.i("Application", "${roomData[i]}")
            }

            Log.i("Application", "=== xlsToRoom - DB All Out Start ===")
            val dataLog = db.courseDao().getAll()
            for (DL in dataLog) {
                Log.i("Application", "$DL")
            }
            Log.i("Application", "=== xlsToRoom - DB All Out End ===")
        }.start()
    }
}