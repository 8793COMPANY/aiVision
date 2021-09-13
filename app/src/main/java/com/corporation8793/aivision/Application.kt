package com.corporation8793.aivision

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.corporation8793.aivision.excel.Excel
import com.corporation8793.aivision.room.AppDatabase
import com.corporation8793.aivision.room.Course
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.concurrent.thread


class Application : Application() {
    lateinit var context: Context
    lateinit var excel: Excel
    lateinit var db : AppDatabase

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

        CoroutineScope(Dispatchers.IO).async {
            for (i in roomData.indices) {
                if (db.courseDao().findByCourseName(roomData[i].courseName) != null) {
                    Log.i("Application", "DB Data Already Exist !! -> ${roomData[i].courseName}")

                } else {
                    // 이 부분만 사용하시면 될 거 같아요
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
        }
    }


    fun dataSize() : Int {
        var dataLog : Int = 0
        Thread {
            dataLog = db.courseDao().getAll().size

        }.start()
        return dataLog
    }

    fun getPositionData(pos : Int) : Course{
        lateinit var dataLog : Course

        Thread {
            dataLog = db.courseDao().getAll().get(pos)

        }.start()

        return dataLog;
    }
}