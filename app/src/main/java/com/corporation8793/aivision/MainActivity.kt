package com.corporation8793.aivision

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.corporation8793.aivision.fragment.AllCourseFragment
import com.corporation8793.aivision.fragment.CourseFragment
import com.corporation8793.aivision.fragment.HomeFragment
import com.corporation8793.aivision.fragment.MyFragment
import com.corporation8793.aivision.recyclerview.course_fragment.CoursePagerAdapter
import com.corporation8793.aivision.room.Course
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Future
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var home_btn: Button
    lateinit var course_btn: Button
    lateinit var my_btn: Button
    lateinit var all_course_btn: Button
    lateinit var linear: LinearLayout
    lateinit var currentLocation : LatLng
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mActivity = this

        home_btn = findViewById(R.id.home_btn)
        course_btn = findViewById(R.id.course_btn)
        my_btn = findViewById(R.id.my_btn)
        linear = findViewById(R.id.linear)
        all_course_btn = findViewById(R.id.all_course_btn)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    Log.e("lastLocation", "onCreateView: location ${location.latitude}, ${location.longitude} !!")
                } else {
                    currentLocation = LatLng(0.0, 0.0)
                    Log.e("lastLocation", "onCreateView: location NULL !!")
                }
            }

        val application = Application().getInstance(applicationContext)
        application.xlsToRoom()

        replaceFragment(HomeFragment(this), 1)

        home_btn.isSelected = true

        home_btn.setOnClickListener {
            replaceFragment(HomeFragment(this), 1)
        }

        course_btn.setOnClickListener {
            replaceFragment(CourseFragment(this, 0), 2)
        }

        my_btn.setOnClickListener {
            replaceFragment(MyFragment(mActivity), 3)
        }

        all_course_btn.setOnClickListener{
            replaceFragment(AllCourseFragment(this), 4)
        }
    }

    private fun settingBtn(btn1: Button, btn2: Button, btn3: Button, btn4: Button) {
        btn1.isSelected = true
        btn2.isSelected = false
        btn3.isSelected = false
        btn4.isSelected = false
    }

        private fun settingFlagBtn(flag: Int) {
            when (flag) {
                1 -> settingBtn(home_btn, course_btn, my_btn,all_course_btn)
                2 -> settingBtn(course_btn, home_btn, my_btn,all_course_btn)
                3 -> settingBtn(my_btn, home_btn, course_btn,all_course_btn)
                4 -> settingBtn(all_course_btn, home_btn, course_btn,my_btn)
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