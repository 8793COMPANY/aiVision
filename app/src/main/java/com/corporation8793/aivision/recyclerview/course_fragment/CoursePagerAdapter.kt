package com.corporation8793.aivision.recyclerview.course_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.R
import com.corporation8793.aivision.fragment.CourseFragment
import com.corporation8793.aivision.room.Course
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class CoursePagerAdapter(val mContext: Context, val mFragment: CourseFragment, private val dataSet:List<Course>) : RecyclerView.Adapter<CoursePagerAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCourseName : TextView = itemView.findViewById(R.id.mCourseName)
        var dash_line_before : ImageView = itemView.findViewById(R.id.dash_line_before)
        var dash_line_after : ImageView = itemView.findViewById(R.id.dash_line_after)
        var course_progress : TextView = itemView.findViewById(R.id.course_progress)
        var course_visit_chk : ImageView = itemView.findViewById(R.id.course_visit_chk)
        var course_vr_start_btn : Button = itemView.findViewById(R.id.course_vr_start_btn)
    }

    val listDataSet : MutableList<listData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.course_fragment_listview_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.mCourseName.text = dataSet[position].courseName
        listDataSet.add(listData(course_progress = false, course_visit_chk = false))
        when(position) {
            0 -> holder.dash_line_before.visibility = View.INVISIBLE
            (itemCount - 1) -> holder.dash_line_after.visibility = View.INVISIBLE
            else -> {
                holder.dash_line_before.visibility = View.VISIBLE
                holder.dash_line_after.visibility = View.VISIBLE
            }
        }
        when {
            listDataSet[position].course_progress -> holder.course_progress.visibility = View.VISIBLE
            !listDataSet[position].course_progress -> holder.course_progress.visibility = View.INVISIBLE
        }

        when {
            listDataSet[position].course_visit_chk -> holder.course_visit_chk.setBackgroundResource(R.drawable.course_visit_chk_on)
            !listDataSet[position].course_visit_chk -> holder.course_visit_chk.setBackgroundResource(R.drawable.course_visit_chk_off)
        }

        holder.course_vr_start_btn.setOnClickListener {
            if (mFragment.ypv_object != null) {
                mFragment.course_list_view_adaptor?.listDataSet?.get(mFragment.prev_position)?.course_progress = false
                mFragment.ypv.removeYouTubePlayerListener(mFragment.ypv_object!!)
            }
            mFragment.playVR_ver2(dataSet, position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    data class listData(
        var course_progress : Boolean,
        var course_visit_chk : Boolean
    )
}