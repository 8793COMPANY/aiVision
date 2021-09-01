package com.corporation8793.aivision

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(
            R.id.fragment,
            HomeFragment()
        )


        transaction.commit()

        val home_btn: Button = findViewById(R.id.home_btn);
        val course_btn: Button = findViewById(R.id.course_btn);
        val my_btn: Button = findViewById(R.id.my_btn);

        home_btn.setSelected(true);

        home_btn.setOnClickListener{
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragment,
                HomeFragment()
            )

            transaction.commit()
            settingBtn(home_btn, course_btn, my_btn)
        }

        course_btn.setOnClickListener{
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragment,
                CourseFragment()
            )

            transaction.commit()
            settingBtn(course_btn,home_btn,my_btn)
        }

        my_btn.setOnClickListener{
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragment,
                MyFragment()
            )

            transaction.commit()
            settingBtn(my_btn,home_btn,course_btn)
        }


    }

    fun settingBtn(btn1 : Button, btn2 : Button, btn3 : Button){
        btn1.setSelected(true);
        btn2.setSelected(false);
        btn3.setSelected(false);
    }
}