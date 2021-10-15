package com.corporation8793.aivision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.corporation8793.aivision.fragment.CourseFragment
import com.corporation8793.aivision.fragment.HomeFragment
import com.corporation8793.aivision.fragment.MyFragment

class IntroActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        var viewpager :ViewPager2 = findViewById(R.id.intro_viewpager)
        viewpager.adapter = ViewPagerAdapter(getIntroList())
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        var before_btn : Button = findViewById(R.id.before_btn)
        var next_btn : Button = findViewById(R.id.next_btn)

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0){
                    before_btn.visibility = View.INVISIBLE
                }else if (position == 2){
                    next_btn.setText("완료")
                }else{
                    before_btn.visibility = View.VISIBLE
                    next_btn.setText("다음")
                }
            }

        })

        before_btn.setOnClickListener{
            if (viewpager.currentItem !=0){
                viewpager.setCurrentItem(viewpager.currentItem-1,true)
            }else{
//                before_btn.visibility = View.VISIBLE
            }

        }

        next_btn.setOnClickListener{
            if (next_btn.text.equals("완료")){
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                viewpager.setCurrentItem(viewpager.currentItem+1,true)
            }
        }

    }

    private fun getIntroList(): ArrayList<Int>{
        return arrayListOf<Int>(R.color.black,R.color.design_default_color_primary,R.color.purple_200)
    }
}