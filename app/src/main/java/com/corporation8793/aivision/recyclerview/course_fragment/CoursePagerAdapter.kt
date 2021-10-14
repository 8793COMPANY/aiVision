package com.corporation8793.aivision.recyclerview.course_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
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

class CoursePagerAdapter(val mContext: Context, val mFragment: CourseFragment, private val dataSet:List<Course>,
                         val listDataSet : MutableList<listData> = mutableListOf()) : RecyclerView.Adapter<CoursePagerAdapter.Holder>() {
    lateinit var wifiManager : WifiManager

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCourseName : TextView = itemView.findViewById(R.id.mCourseName)
        var dash_line_before : ImageView = itemView.findViewById(R.id.dash_line_before)
        var dash_line_before_done : ImageView = itemView.findViewById(R.id.dash_line_before_done)
        var dash_line_after : ImageView = itemView.findViewById(R.id.dash_line_after)
        var dash_line_after_done : ImageView = itemView.findViewById(R.id.dash_line_after_done)
        var course_progress : TextView = itemView.findViewById(R.id.course_progress)
        var course_visit_chk : ImageView = itemView.findViewById(R.id.course_visit_chk)
        var course_know_more : Button = itemView.findViewById(R.id.course_know_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.course_fragment_listview_item, parent, false)

        wifiManager = mContext.getSystemService(WIFI_SERVICE) as WifiManager

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setIsRecyclable(false)
        holder.mCourseName.text = dataSet[position].courseName
        listDataSet.add(listData(position = position, course_progress = false, course_visit_chk = false))
        when(position) {
            // 첫 번째
            0 -> {
                holder.dash_line_before.visibility = View.INVISIBLE
                holder.dash_line_before_done.visibility = View.INVISIBLE
            }

            // 마지막
            (itemCount - 1) -> {
                holder.dash_line_after.visibility = View.INVISIBLE
                holder.dash_line_after_done.visibility = View.INVISIBLE
            }

            // 중간
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

        if (listDataSet[position].course_visit_chk) {
            for ((i, d) in listDataSet.withIndex()) {
                if (d.course_visit_chk && (i > position)) {
                    holder.dash_line_after_done.visibility = View.VISIBLE
                    holder.dash_line_after.visibility = View.INVISIBLE
                } else if (d.course_visit_chk && (i < position)) {
                    holder.dash_line_before_done.visibility = View.VISIBLE
                    holder.dash_line_before.visibility = View.INVISIBLE
                }
            }
        }

        holder.course_know_more.setOnClickListener {
            if (mFragment.ypv_object != null) {
                mFragment.course_list_view_adaptor?.listDataSet?.get(mFragment.prev_position)?.course_progress = false
                mFragment.ypv.removeYouTubePlayerListener(mFragment.ypv_object!!)
            }
            if (wifiManager.connectionInfo.ssid != WifiManager.UNKNOWN_SSID) {
                //mFragment.playVR_ver2(dataSet, position)
                mFragment.knowMore(dataSet, position)
            } else {
                // TODO : 임시코드
                mFragment.knowMore(dataSet, position)
                //Toast.makeText(mFragment.mActivity, "Wi-Fi 연결을 확인해주세요", Toast.LENGTH_SHORT).show()
                Toast.makeText(mFragment.mActivity, "Wi-Fi 연결을 권장합니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    data class listData(
        var position : Int,
        var course_progress : Boolean,
        var course_visit_chk : Boolean
    )
}