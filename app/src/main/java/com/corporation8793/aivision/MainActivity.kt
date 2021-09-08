package com.corporation8793.aivision

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.corporation8793.aivision.fragment.CourseFragment
import com.corporation8793.aivision.fragment.HomeFragment
import com.corporation8793.aivision.fragment.MyFragment

class MainActivity : AppCompatActivity() {
    lateinit var home_btn: Button
    lateinit var course_btn: Button
    lateinit var my_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        home_btn = findViewById(R.id.home_btn)
        course_btn = findViewById(R.id.course_btn)
        my_btn = findViewById(R.id.my_btn)

        val application = Application().getInstance(applicationContext)
        application.xlsToRoom()
        val dataLog = application.getAllByCourseType("횃불코스")
        Log.i("MainActivity", "$dataLog")
        for (dl in dataLog) {
            Log.i("MainActivity", "$dl")
        }
        Log.i("MainActivity", "DONE")

        replaceFragment(HomeFragment(this), 1)

        home_btn.isSelected = true

        home_btn.setOnClickListener {
            replaceFragment(HomeFragment(this), 1)
        }

        course_btn.setOnClickListener {
            replaceFragment(CourseFragment(this), 2)
        }

        my_btn.setOnClickListener {
            replaceFragment(MyFragment(), 3)
        }
    }

    private fun settingBtn(btn1: Button, btn2: Button, btn3: Button) {
        btn1.isSelected = true
        btn2.isSelected = false
        btn3.isSelected = false
    }

        private fun settingFlagBtn(flag: Int) {
            when (flag) {
                1 -> settingBtn(home_btn, course_btn, my_btn)
                2 -> settingBtn(course_btn, home_btn, my_btn)
                3 -> settingBtn(my_btn, home_btn, course_btn)
            }
        }

    fun replaceFragment(fragment: Fragment, flag: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragment,
            fragment
        )

        transaction.commit()
        settingFlagBtn(flag)
    }
}