package com.corporation8793.aivision.recyclerview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.corporation8793.aivision.R
import com.corporation8793.aivision.room.Course

class CourseAdapter  : RecyclerView.Adapter<CourseAdapter.ViewHolder>(){

    var datas = mutableListOf<Course>()
    var check : Boolean = false
    lateinit var context : Context
    lateinit var drawable : GradientDrawable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
        context = parent.context
        return ViewHolder(view)
    }

    fun editMode(){
        if (check)
            check = false
        else
            check = true
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
        Log.e("datas",datas[position].courseName)

//        val layoutParams = holder.itemView.layoutParams
//        layoutParams.width = 170
//        holder.itemView.requestLayout()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val course_img: ImageView = itemView.findViewById(R.id.course_img)
        private val course_name: TextView = itemView.findViewById(R.id.course_name)
        private val cancel_btn: Button = itemView.findViewById(R.id.cancel_btn)


        fun bind(item: Course) {



            course_name.text = item.courseName
//            course_img.setBackgroundResource(item.img)
            if (check)
                cancel_btn.visibility = View.VISIBLE
            else
                cancel_btn.visibility = View.INVISIBLE

            drawable = context.getDrawable(R.drawable.background_rounding) as GradientDrawable
            course_img.setBackground(drawable);
            course_img.setClipToOutline(true);

//            course_img.setImageResource(item.img)
            Log.e("hi","bind");
//            Glide.with(itemView).load(item.img).into(course_img)

        }
    }
}