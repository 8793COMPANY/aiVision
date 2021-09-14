package com.corporation8793.aivision.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corporation8793.aivision.Application
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.R
import com.corporation8793.aivision.recyclerview.course_fragment.CoursePagerAdapter
import com.corporation8793.aivision.room.Course
import com.naver.maps.geometry.Coord
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMap.LAYER_GROUP_TRANSIT
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

// : Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CourseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourseFragment(activity: MainActivity, courseFlag: Int) : Fragment() {
    // : Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var nMap : NaverMap? = null
    var course_list_view : RecyclerView? = null
    var course_list_view_adaptor : CoursePagerAdapter? = null
    var result: List<Course> = listOf()
    val mActivity = activity
    lateinit var map : View
    lateinit var ypv : YouTubePlayerView
    lateinit var ypv_temp : YouTubePlayerView
    var ypv_flag : Boolean = false
    val mFragment = this
    val application = Application().getInstance(mActivity.applicationContext)
    var path = PathOverlay()
    var markers : MutableList<Marker> = mutableListOf()
    val mCourseFlag = courseFlag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_course, container, false)
        val course_list : Spinner = view.findViewById(R.id.course_list)
        map = view.findViewById(R.id.map)
        ypv = view.findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(ypv)
        ypv_flag = false

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        course_list_view = view.findViewById(R.id.course_list_view)
        course_list_view?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        CoroutineScope(Dispatchers.IO).launch {
            var name = ""
            when (mCourseFlag) {
                0 -> name = "횃불코스"
                1 -> name = "희생코스"
                2 -> name = "광장코스"
                3 -> name = "열정코스"
                4 -> name = "영혼코스"
            }

            result = application.db.courseDao().getAllByCourseType(name)

            CoroutineScope(Dispatchers.Main).launch {
                refreshCourseListView()
            }
        }

        mapFragment.getMapAsync {
            nMap = it
            it.mapType = NaverMap.MapType.Basic
            it.setLayerGroupEnabled(LAYER_GROUP_TRANSIT, true)

            val cameraUpdate = CameraUpdate.scrollTo(
                LatLng(result[0].courseLatitude.toDouble(),
                    result[0].courseLongitude.toDouble()))
            it.moveCamera(cameraUpdate)

            var coords : MutableList<LatLng> = mutableListOf()
            for (rs in result) {
                coords.add(LatLng(rs.courseLatitude.toDouble(), rs.courseLongitude.toDouble()))
                markers.add(Marker(LatLng(rs.courseLatitude.toDouble(), rs.courseLongitude.toDouble())))
            }

            for (mk in markers) {
                mk.map = nMap
            }

            path.coords = coords
            path.map = nMap
        }

        course_list.adapter = ArrayAdapter.createFromResource(
            view.context,
            R.array.itemList,
            android.R.layout.simple_spinner_item
        )

        course_list.setSelection(mCourseFlag)

        course_list.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var command = ""
                when (position) {
                    0, 1, 2, 3, 4 -> command = course_list.selectedItem.toString()
                    5 -> mActivity.replaceFragment(MyFragment(mActivity), 3)
                }

                if (position != 5) {
                    spinnerSelected(application, command)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return view
    }

    override fun onDestroy() {
        ypv.release()
        super.onDestroy()
    }

    fun spinnerSelected(application : Application, name : String) {
        CoroutineScope(Dispatchers.IO).launch {
            result = application.db.courseDao().getAllByCourseType(name)

            CoroutineScope(Dispatchers.Main).launch {
                refreshMap()
                refreshCourseListView()
            }
        }
    }

    fun refreshMap() {
        // 오버레이 초기화
        if (ypv_flag) {
            ypv_temp.release()
            map.visibility = View.VISIBLE
            ypv.visibility = View.INVISIBLE
            ypv_flag = !ypv_flag
        }

        path.map = null
        for (mk in markers) {
            mk.map = null
        }

        nMap?.mapType = NaverMap.MapType.Basic
        nMap?.setLayerGroupEnabled(LAYER_GROUP_TRANSIT, true)

        val cameraUpdate = CameraUpdate.scrollTo(
            LatLng(result[0].courseLatitude.toDouble(),
                result[0].courseLongitude.toDouble()))
        nMap?.moveCamera(cameraUpdate)

        path = PathOverlay()
        markers = mutableListOf()
        var coords : MutableList<LatLng> = mutableListOf()

        for (rs in result) {
            coords.add(LatLng(rs.courseLatitude.toDouble(), rs.courseLongitude.toDouble()))
            markers.add(Marker(LatLng(rs.courseLatitude.toDouble(), rs.courseLongitude.toDouble())))
        }

        for (mk in markers) {
            mk.map = nMap
        }

        path.coords = coords
        path.map = nMap
    }

    fun refreshCourseListView() {
        course_list_view_adaptor = CoursePagerAdapter(mActivity.applicationContext, mFragment, result)
        course_list_view?.adapter = course_list_view_adaptor
        course_list_view_adaptor!!.notifyDataSetChanged()
    }

    fun playVR(dataSet : List<Course>, position : Int) {
        map.visibility = View.INVISIBLE
        ypv.visibility = View.VISIBLE

        ypv.removeView(ypv)
        ypv.release()
        Log.e("playVR", "onVideoposition: $position")

        ypv_temp = YouTubePlayerView(mActivity.applicationContext)
        ypv_temp.enableAutomaticInitialization = false
        ypv_temp.enableBackgroundPlayback(false)
        ypv_temp.initialize(object : AbstractYouTubePlayerListener() {
            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
                super.onVideoId(youTubePlayer, videoId)
                Log.e("ypv_temp", "onVideoId: $videoId")
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                super.onStateChange(youTubePlayer, state)
                Log.e("onStateChange", "onStateChange: $state" )
                when (state) {
                    PlayerConstants.PlayerState.PLAYING -> {
                        course_list_view_adaptor?.listDataSet?.get(position)?.course_progress = true
                    }
                    PlayerConstants.PlayerState.PAUSED -> {
                        course_list_view_adaptor?.listDataSet?.get(position)?.course_progress = false
                    }
                    PlayerConstants.PlayerState.ENDED -> {
                        course_list_view_adaptor?.listDataSet?.get(position)?.course_progress = false
                        course_list_view_adaptor?.listDataSet?.get(position)?.course_visit_chk = true
                    }
                }
                course_list_view_adaptor?.notifyDataSetChanged()
            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                CoroutineScope(Dispatchers.IO).launch {
                    youTubePlayer.loadVideo(dataSet[position].courseURL.replace("https://youtu.be/", ""),0F)

                    CoroutineScope(Dispatchers.Main).launch {
                        youTubePlayer.play()
                    }
                }
            }
        })
        ypv_flag = true
        ypv.addView(ypv_temp)
        lifecycle.addObserver(ypv_temp)
    }
}