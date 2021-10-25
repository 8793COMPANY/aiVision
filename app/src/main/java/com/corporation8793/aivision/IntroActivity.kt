package com.corporation8793.aivision

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class IntroActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        var viewpager :ViewPager2 = findViewById(R.id.intro_viewpager)
        viewpager.adapter = ViewPagerAdapter(getIntroList())
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        var invisible_forever_btn : Button = findViewById(R.id.invisible_forever)
        var close_btn : Button = findViewById(R.id.close)

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 5){
                    close_btn.isEnabled = true
                    invisible_forever_btn.isEnabled = true
                }else{
                    close_btn.isEnabled = false
                    invisible_forever_btn.isEnabled = false

                }
            }

        })

        invisible_forever_btn.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        close_btn.setOnClickListener{
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

        }

    }

    private fun getIntroList(): ArrayList<Int>{
        return arrayListOf<Int>(R.drawable.intro1,R.drawable.intro2,R.drawable.intro3,R.drawable.intro4,R.drawable.intro5,R.drawable.intro6)
    }
}