package com.corporation8793.aivision

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(introList: ArrayList<Int>) : PagerAdapter() {
    var item = introList

    fun ViewPagerAdapter(list : ArrayList<Int>){
        item = list
    }

    //position에 해당하는 페이지 생성
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=LayoutInflater.from(container.context).inflate(R.layout.intro_list_item,container,false)
        val imageview = view.findViewById<ImageView>(R.id.imageview)
        imageview.setImageResource(item[position])
        container.addView(view)
        return view
    }

    //position에 위치한 페이지 제거
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }


    //사용가능한 뷰 개수 리턴
    override fun getCount(): Int {
        return 6
    }

    //페이지뷰가 특정 키 객체(key object)와 연관 되는지 여부
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view==`object`)
    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))
//
//    override fun getItemCount(): Int = item.size
//
//    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
//        holder.imageview.setImageResource(item[position])
//    }
//
//    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
//        (LayoutInflater.from(parent.context).inflate(R.layout.intro_list_item, parent, false)){
//            val imageview = itemView.findViewById<ImageView>(R.id.imageview)
//    }
}