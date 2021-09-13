package com.corporation8793.aivision.recyclerview.course_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.R
import com.corporation8793.aivision.room.Course

class CoursePagerAdapter(val mContext: Context, val mActivity: MainActivity, private val dataSet:List<Course>) : RecyclerView.Adapter<CoursePagerAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mCourseName : TextView = itemView.findViewById(R.id.mCourseName)
        val course_progress : TextView = itemView.findViewById(R.id.course_progress)
        val course_visit_chk : ImageView = itemView.findViewById(R.id.course_visit_chk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.course_fragment_listview_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.mCourseName.text = dataSet[position].courseName
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}