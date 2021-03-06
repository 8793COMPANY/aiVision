package com.corporation8793.aivision

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.corporation8793.aivision.excel.Excel
import com.corporation8793.aivision.room.AppDatabase
import com.corporation8793.aivision.room.Course
import kotlinx.coroutines.*


/**
 * aivision 의 애플리케이션 싱글톤 클래스 입니다.
 * @author  두동근
 * @see     Application
 */
class Application : Application() {
    lateinit var context: Context
    lateinit var excel: Excel
    lateinit var db : AppDatabase

    companion object {
        lateinit var prefs : MySharedPreferences
    }

    /**
     * 애플리케이션 인스턴스를 반환합니다.
     * @author  두동근
     * @param   c 애플리케이션 콘텍스트.
     * @return  Application
     * @see     Application
     */
    fun getInstance(c: Context) : com.corporation8793.aivision.Application {
        context = c
        excel = Excel(c, "gj_spot.xls")
        db = Room.databaseBuilder(
            c,
            AppDatabase::class.java, "CourseDB"
        ).build()

        return this
    }

    /**
     * 엑셀 파일의 데이터를 Room DB 로 마이그레이션합니다.
     * @author  두동근
     * @see     Excel
     * @see     Room
     */
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

        CoroutineScope(Dispatchers.IO).launch {
            if (db.courseDao().getAll().isNullOrEmpty()) {
                Log.i("Application", "DB Data is Empty !! X_X")
                for (i in roomData.indices) {
                    db.courseDao().insertAll(roomData[i])
                    Log.i("Application", "DB Data Successful Inserted !! -> ${roomData[i]}")
                }
            } else {
                Log.i("Application", "DB Data is Already Exist !! O_O")
                for (i in roomData.indices) {
                    Log.i("Application", "DB Data Already Exist !! -> ${roomData[i].courseName}")
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        prefs = MySharedPreferences(applicationContext)
    }
}