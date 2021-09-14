package com.corporation8793.aivision.recyclerview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.corporation8793.aivision.R
import com.corporation8793.aivision.room.Course

class CourseAdapter  : RecyclerView.Adapter<CourseAdapter.ViewHolder>(){

    var datas = mutableListOf<Course>()
    var check : Boolean = false
    lateinit var context : Context
    lateinit var drawable : GradientDrawable
    var pos : Int = 0

    interface OnItemClickListener{
        fun onItemClick(v:View, data: Course, pos : Int, courseName : String)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recyclerview_item,
            parent,
            false
        )
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

    fun removeItem(position: Int){
        datas.removeAt(position)
        notifyDataSetChanged()
    }




    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
        Log.e("datas", datas[position].courseName)
//        val layoutParams = holder.itemView.layoutParams
//        layoutParams.width = 170
//        holder.itemView.requestLayout()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val course_img: ImageView = itemView.findViewById(R.id.course_img)
        private val course_name: TextView = itemView.findViewById(R.id.course_name)
        private val cancel_btn: Button = itemView.findViewById(R.id.cancel_btn)


        fun bind(item: Course) {

//            if (!(check and item.courseImgName.equals("last_item_image"))) {


                course_name.text = item.courseName
//            course_img.setBackgroundResource(item.img)
                if (check)
                    cancel_btn.visibility = View.VISIBLE
                else
                    cancel_btn.visibility = View.INVISIBLE
//
//            drawable = context.getDrawable(R.drawable.background_rounding) as GradientDrawable
//            course_img.setBackground(drawable);
                course_img.background = context.getDrawable(R.drawable.background_rounding)
                course_img.setClipToOutline(true);

                cancel_btn.setOnClickListener {
                    removeItem(adapterPosition)
                    Log.e("in!", adapterPosition.toString())
                }


                if (!item.courseImgName.equals("")) {
                    val resId: Int = context.getResources().getIdentifier(
                        item.courseImgName, "drawable",
                        "com.corporation8793.aivision"
                    )

                    course_img.setImageResource(resId)
                } else {
                    course_img.setImageResource(R.color.black)
                }

                if (pos != RecyclerView.NO_POSITION) {
                    itemView.setOnClickListener {
                        listener?.onItemClick(itemView, item, adapterPosition, item.courseName)
                    }
                }

        }
    }
}