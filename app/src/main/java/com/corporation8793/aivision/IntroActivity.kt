package com.corporation8793.aivision

import android.content.Intent
import android.gesture.GestureOverlayView.ORIENTATION_HORIZONTAL
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class IntroActivity  : AppCompatActivity() {
    // push 확인
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        var viewpager :ViewPager = findViewById(R.id.intro_viewpager)
        var adapter : ViewPagerAdapter = ViewPagerAdapter(getIntroList());
        viewpager.adapter = adapter

        var wormDotsIndicator  : WormDotsIndicator = findViewById<WormDotsIndicator>(R.id.spring_dots_indicator)
        wormDotsIndicator .setViewPager(viewpager)

        var invisible_forever_btn : Button = findViewById(R.id.invisible_forever)
        var close_btn : Button = findViewById(R.id.close)

        viewpager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                Log.e("select","확인")
            }

        })

//        viewpager.registerOnPageChangeCallback(object : ViewPager.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                if (position == 5){
//                    close_btn.isEnabled = true
//                    invisible_forever_btn.isEnabled = true
//                }else{
//                    close_btn.isEnabled = false
//                    invisible_forever_btn.isEnabled = false
//
//                }
//            }
//
//        })

        invisible_forever_btn.setOnClickListener{
            Application.prefs.setBoolean("invisible", true)
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