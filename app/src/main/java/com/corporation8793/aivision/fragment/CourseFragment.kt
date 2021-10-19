package com.corporation8793.aivision.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corporation8793.aivision.Application
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.R
import com.corporation8793.aivision.recyclerview.course_fragment.CoursePagerAdapter
import com.corporation8793.aivision.room.Course
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.NaverMap.LAYER_GROUP_TRANSIT
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.*
import okhttp3.Dispatcher

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
    var currentLocation = activity.currentLocation
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var course_list_view : RecyclerView? = null
    var course_list_view_adaptor : CoursePagerAdapter? = null
    var result: List<Course> = listOf()
    private lateinit var bottomSheetView : View
    private lateinit var bottomSheetDialog : BottomSheetDialog
    var totalPage = 0
    var currentPage = 0
    var contents : List<String> = listOf()
    lateinit var mClickLister : View.OnClickListener
    lateinit var mAlertDialog : AlertDialog
    lateinit var course_list_select : TextView
    var count : Int = 0
    val mActivity = activity
    lateinit var finish_btn : AppCompatButton
    lateinit var map : View
    lateinit var ypv : YouTubePlayerView
    lateinit var ypv_temp : YouTubePlayerView
    var listDataSet : MutableList<CoursePagerAdapter.listData> = mutableListOf()
    var ypv_object : YouTubePlayerListener? = null
    var prev_position : Int = 0
    var yp : YouTubePlayer? = null
    lateinit var course_list_view_container : LinearLayout
    lateinit var actionbar : ConstraintLayout
    lateinit var ypv_container : LinearLayout
    lateinit var callback: OnBackPressedCallback
    var name = ""
    var command = ""
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

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_course, container, false)
        val course_list : LinearLayout = view.findViewById(R.id.course_list)
        course_list_select = view.findViewById(R.id.course_list_select)
        actionbar = view.findViewById(R.id.actionbar)
        course_list_view_container = view.findViewById(R.id.course_list_view_container)
        finish_btn = view.findViewById(R.id.finish_btn)
        ypv_container = view.findViewById(R.id.ypv_container)
        map = view.findViewById(R.id.map)
        ypv = view.findViewById(R.id.youtube_player_view)
        ypv_temp = view.findViewById(R.id.youtube_player_view)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity)

        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_layout, container, false)
        bottomSheetDialog = BottomSheetDialog(mActivity)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.behavior.skipCollapsed = true
        bottomSheetDialog.behavior.saveFlags = BottomSheetBehavior.SAVE_SKIP_COLLAPSED
        bottomSheetDialog.behavior.isFitToContents = true

        finish_btn.setOnClickListener {
            mActivity.replaceFragment(HomeFragment(mActivity), 1)
        }

        ypv.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                yp = youTubePlayer
                yp!!.cueVideo("", 0F)
            }
        })
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
            when (mCourseFlag) {
                0 -> name = "횃불코스"
                1 -> name = "희생코스"
                2 -> name = "광장코스"
                3 -> name = "열정코스"
                4 -> name = "영혼코스"
                5 -> name = "나만의 VR코스"
            }

            course_list_select.text = name
            count = mCourseFlag

            if (mCourseFlag != 5) {
                result = application.db.courseDao().getAllByCourseType(name)

                val lds : MutableList<CoursePagerAdapter.listData> = mutableListOf()
                for ((index, rs) in result.withIndex()) {
                    lds.add(CoursePagerAdapter.listData(index, false, false))
                }
                listDataSet = lds

                CoroutineScope(Dispatchers.Main).launch {
                    refreshCourseListView()
                }
            } else {
                myCourseSelected(application, 1)
            }
        }

        mapFragment.getMapAsync {
            nMap = it
            it.apply {
                mapType = NaverMap.MapType.Basic
                setLayerGroupEnabled(LAYER_GROUP_TRANSIT, true)

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location->
                        if (location != null) {
                            moveCamera(
                                CameraUpdate.scrollAndZoomTo(
                                    LatLng(35.153629399146354, 126.88364996966044),
                                    10.0
                                )
                            )
                            currentLocation = LatLng(location.latitude, location.longitude)
                            Log.e("lastLocation", "onCreateView: location ${location.latitude}, ${location.longitude} !!")
                        } else {
                            currentLocation = LatLng(result[0].courseLatitude.toDouble(), result[0].courseLongitude.toDouble())
                            Log.e("lastLocation", "onCreateView: location NULL !!")
                        }
                    }
            }

            var coords : MutableList<LatLng> = mutableListOf()

            for (rs in result) {
                val coordinateInScope = LatLng(rs.courseLatitude.toDouble(), rs.courseLongitude.toDouble())
                coords.add(coordinateInScope)
                markers.add(Marker(coordinateInScope))
            }

            for (mk in markers) {
                mk.map = nMap
            }

            path.apply {
                this.coords = coords
                map = nMap
            }
        }

        course_list.setOnClickListener {
            courseSelectMenu()
            course_list_select.requestLayout()
        }

        return view
    }

    override fun onDestroy() {
        ypv.release()
        super.onDestroy()
    }

    fun spinnerSelected(application : Application, name : String, flag : Int = 0) {
        course_list_select.text = name
        course_list_select.requestLayout()

        CoroutineScope(Dispatchers.IO).launch {
            result = application.db.courseDao().getAllByCourseType(name)
            when (flag) {
                1 -> {
                    val lds : MutableList<CoursePagerAdapter.listData> = mutableListOf()
                    for ((index, rs) in result.withIndex()) {
                        lds.add(CoursePagerAdapter.listData(index,false, false))
                    }
                    listDataSet = lds
                }
            }

            withContext(Dispatchers.Main) {
                refreshMap(result)
                refreshCourseListView()
            }
        }
    }

    fun myCourseSelected(application : Application, flag : Int = 0) {
        course_list_select.text = name
        course_list_select.requestLayout()

        CoroutineScope(Dispatchers.IO).launch {
            var ml : MutableList<Course> = mutableListOf()
            ml.apply {
                if (Application.prefs.getString("my_course","none") != "none") {
                    var arrays : List<String> = Application.prefs.getString("my_course","none").split(",")
                    for(i in arrays){
                        if (application.db.courseDao().findCourseData(i) != null) {
                            add(application.db.courseDao().findCourseData(i))
                        }
                    }
                }
            }

            result = ml.toList()
            when (flag) {
                1 -> {
                    val lds : MutableList<CoursePagerAdapter.listData> = mutableListOf()
                    for ((index, rs) in result.withIndex()) {
                        lds.add(CoursePagerAdapter.listData(index,false, false))
                    }
                    listDataSet = lds
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                if (result.isNotEmpty()) {
                    refreshMap(result)
                    refreshCourseListView()
                } else {
                    mActivity.replaceFragment(MyFragment(mActivity), 3)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun refreshMap(res : List<Course> = listOf(), position : Int = 0) {
        // 오버레이 초기화
        if (ypv_flag) {
            map.visibility = View.VISIBLE
            ypv.visibility = View.INVISIBLE
            ypv_flag = !ypv_flag

            nMap?.apply {
                mapType = NaverMap.MapType.Basic
                setLayerGroupEnabled(LAYER_GROUP_TRANSIT, true)

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location->
                        if (location != null) {
                            moveCamera(
                                CameraUpdate.scrollAndZoomTo(
                                    LatLng(res[position].courseLatitude.toDouble(), res[position].courseLongitude.toDouble()),
                                    13.0
                                )
                            )
                            currentLocation = LatLng(location.latitude, location.longitude)
                            Log.e("lastLocation", "onCreateView: location ${location.latitude}, ${location.longitude} !!")
                        } else {
                            currentLocation = LatLng(result[0].courseLatitude.toDouble(), result[0].courseLongitude.toDouble())
                            Log.e("lastLocation", "onCreateView: location NULL !!")
                        }
                    }

            }
        } else {
            nMap?.apply {
                mapType = NaverMap.MapType.Basic
                setLayerGroupEnabled(LAYER_GROUP_TRANSIT, true)

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location->
                        if (location != null) {
                            moveCamera(
                                CameraUpdate.scrollAndZoomTo(
                                    LatLng(35.153629399146354, 126.88364996966044),
                                    10.0
                                )
                            )
                            currentLocation = LatLng(location.latitude, location.longitude)
                            Log.e("lastLocation", "onCreateView: location ${location.latitude}, ${location.longitude} !!")
                        } else {
                            currentLocation = LatLng(result[0].courseLatitude.toDouble(), result[0].courseLongitude.toDouble())
                            Log.e("lastLocation", "onCreateView: location NULL !!")
                        }
                    }

            }
        }

        path.map = null
        for (mk in markers) {
            mk.map = null
        }

        if (res.isNotEmpty()) {
            result = res
        } else {

        }

        path = PathOverlay()
        markers = mutableListOf()
        var coords : MutableList<LatLng> = mutableListOf()

        for ((index, rs) in result.withIndex()) {
            val coordinateInScope = LatLng(rs.courseLatitude.toDouble(), rs.courseLongitude.toDouble())
            coords.add(coordinateInScope)

            if (listDataSet[index].course_visit_chk) {
                var m = Marker(coordinateInScope)
                m.apply {
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.RED
                }

                markers.add(m)
            } else {
                markers.add(Marker(coordinateInScope))
            }
        }

        for (mk in markers) {
            mk.map = nMap
        }

        path.apply {
            this.coords = coords
            map = nMap
        }

        finish_btn.setOnClickListener {
            mActivity.replaceFragment(HomeFragment(mActivity), 1)
        }
    }

    @SuppressLint("MissingPermission")
    fun refreshCourseListView() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    Log.e("lastLocation", "onCreateView: location ${location.latitude}, ${location.longitude} !!")
                } else {
                    currentLocation = LatLng(result[0].courseLatitude.toDouble(), result[0].courseLongitude.toDouble())
                    Log.e("lastLocation", "onCreateView: location NULL !!")
                }
            }
        course_list_view_adaptor = CoursePagerAdapter(mActivity.applicationContext, mFragment, result, listDataSet)
        course_list_view?.adapter = course_list_view_adaptor
        course_list_view?.adapter?.notifyDataSetChanged()
    }

    fun playVR_ver2(dataSet : List<Course>, position : Int) {
        map.visibility = View.INVISIBLE
        ypv.visibility = View.VISIBLE

        ypv.enableBackgroundPlayback(false)

        prev_position = position
        ypv_object = object : AbstractYouTubePlayerListener() {
            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
                super.onVideoId(youTubePlayer, videoId)
                Log.e("ypv_temp", "onVideoId: $videoId")
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                super.onStateChange(youTubePlayer, state)
                Log.e("onStateChange", "onStateChange: $state" )
                when (state) {
                    PlayerConstants.PlayerState.PLAYING -> {
                        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        mActivity.linear.visibility = View.GONE
                        course_list_view_container.visibility = View.GONE
                        actionbar.visibility = View.GONE

                        ypv_container.layoutParams.height = MATCH_PARENT
                        ypv_container.requestLayout()

                        course_list_view_adaptor?.listDataSet?.get(position)?.course_progress = true
                    }
                    PlayerConstants.PlayerState.PAUSED -> {
                        course_list_view_adaptor?.listDataSet?.get(position)?.course_progress = true

                        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        mActivity.linear.visibility = View.VISIBLE
                        course_list_view_container.visibility = View.VISIBLE
                        actionbar.visibility = View.VISIBLE

                        ypv_container.layoutParams.height = 0
                        ypv_container.requestLayout()

                        finish_btn.setOnClickListener {
                            if (command != "5") {
                                spinnerSelected(application, command)
                            } else {
                                myCourseSelected(application)
                            }
                        }
                    }
                    PlayerConstants.PlayerState.ENDED -> {
                        course_list_view_adaptor?.listDataSet?.get(position)?.course_progress = false
                        course_list_view_adaptor?.listDataSet?.get(position)?.course_visit_chk = true

                        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        mActivity.linear.visibility = View.VISIBLE
                        course_list_view_container.visibility = View.VISIBLE
                        actionbar.visibility = View.VISIBLE

                        ypv_container.layoutParams.height = 0
                        ypv_container.requestLayout()

                        finish_btn.setOnClickListener {
                            if (command != "5") {
                                spinnerSelected(application, command)
                            } else {
                                myCourseSelected(application)
                            }
                        }

                        listDataSet = course_list_view_adaptor?.listDataSet!!

                        ypv.removeYouTubePlayerListener(this)

                        refreshMap(dataSet, position)
                        if (position != (dataSet.size - 1)) {
                            Handler().postDelayed({nextCourseDialog()}, 500)
                        }
                    }
                }
                course_list_view_adaptor?.notifyDataSetChanged()
            }
        }
        ypv.addYouTubePlayerListener(ypv_object as AbstractYouTubePlayerListener)
        ypv.enterFullScreen()
        yp?.loadVideo(dataSet[position].courseURL.replace("https://youtu.be/", ""),70F)

        ypv_flag = true
        lifecycle.addObserver(ypv)
    }

    fun knowMore(dataSet : List<Course>, position : Int) {
        val naverSearchQuery = "https://search.naver.com/search.naver?query=${dataSet[position].courseName}"
        val chunkSize = 175
        contents = dataSet[position].courseContent.chunked(chunkSize)
        totalPage = contents.size
        currentPage = 0

        // 바텀 시트 초기화
        bottomSheetView.findViewById<ImageView>(R.id.bottom_sheet_dialog_picture).background = ResourcesCompat.getDrawable(resources,
            resources.getIdentifier(dataSet[position].courseImgName, "drawable", mActivity.packageName),
            mActivity.theme)
        bottomSheetView.findViewById<TextView>(R.id.course_name).text = dataSet[position].courseName
        // 버튼 리스너 초기화
        bottomSheetView.findViewById<Button>(R.id.bottom_sheet_dialog_dismiss_btn).setOnClickListener {
            totalPage = 0
            currentPage = 0
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<Button>(R.id.play_vr_btn).setOnClickListener {
            playVR_ver2(dataSet, position)
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<Button>(R.id.bts_open_webview).setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(naverSearchQuery)
            startActivity(i)
        }
        bottomSheetView.findViewById<Button>(R.id.bts_prev_page).setOnClickListener {
            currentPage -= 1
            if (currentPage > 0) {
                bottomSheetView.findViewById<TextView>(R.id.course_content).text = contents[currentPage] + "..."
                bottomSheetView.findViewById<TextView>(R.id.bts_pager).text = "${currentPage + 1}/${totalPage}"
                bottomSheetView.findViewById<Button>(R.id.bts_prev_page).isEnabled = true
                bottomSheetView.findViewById<Button>(R.id.bts_prev_page).background = ResourcesCompat.getDrawable(resources,
                    resources.getIdentifier("bts_prev_page_on", "drawable", mActivity.packageName),
                    mActivity.theme)
                bottomSheetView.findViewById<Button>(R.id.bts_next_page).isEnabled = true
                bottomSheetView.findViewById<Button>(R.id.bts_next_page).background = ResourcesCompat.getDrawable(resources,
                    resources.getIdentifier("bts_next_page_on", "drawable", mActivity.packageName),
                    mActivity.theme)
            } else if (currentPage <= 0) {
                currentPage = 0
                bottomSheetView.findViewById<TextView>(R.id.course_content).text = contents[currentPage] + "..."
                bottomSheetView.findViewById<TextView>(R.id.bts_pager).text = "${currentPage + 1}/${totalPage}"
                bottomSheetView.findViewById<Button>(R.id.bts_prev_page).isEnabled = false
                bottomSheetView.findViewById<Button>(R.id.bts_prev_page).background = ResourcesCompat.getDrawable(resources,
                    resources.getIdentifier("bts_prev_page_off", "drawable", mActivity.packageName),
                    mActivity.theme)
                bottomSheetView.findViewById<Button>(R.id.bts_next_page).isEnabled = true
                bottomSheetView.findViewById<Button>(R.id.bts_next_page).background = ResourcesCompat.getDrawable(resources,
                    resources.getIdentifier("bts_next_page_on", "drawable", mActivity.packageName),
                    mActivity.theme)
            }
        }
        bottomSheetView.findViewById<Button>(R.id.bts_next_page).setOnClickListener {
            currentPage += 1
            if (currentPage < (totalPage-1)) {
                bottomSheetView.findViewById<TextView>(R.id.course_content).text = contents[currentPage] + "..."
                bottomSheetView.findViewById<TextView>(R.id.bts_pager).text = "${currentPage + 1}/${totalPage}"
                bottomSheetView.findViewById<Button>(R.id.bts_prev_page).isEnabled = true
                bottomSheetView.findViewById<Button>(R.id.bts_prev_page).background = ResourcesCompat.getDrawable(resources,
                    resources.getIdentifier("bts_prev_page_on", "drawable", mActivity.packageName),
                    mActivity.theme)
                bottomSheetView.findViewById<Button>(R.id.bts_next_page).isEnabled = true
                bottomSheetView.findViewById<Button>(R.id.bts_next_page).background = ResourcesCompat.getDrawable(resources,
                    resources.getIdentifier("bts_next_page_on", "drawable", mActivity.packageName),
                    mActivity.theme)
            } else if (currentPage >= (totalPage-1)) {
                currentPage = totalPage-1
                bottomSheetView.findViewById<TextView>(R.id.course_content).text = contents[currentPage]
                bottomSheetView.findViewById<TextView>(R.id.bts_pager).text = "${totalPage}/${totalPage}"
                bottomSheetView.findViewById<Button>(R.id.bts_prev_page).isEnabled = true
                bottomSheetView.findViewById<Button>(R.id.bts_prev_page).background = ResourcesCompat.getDrawable(resources,
                    resources.getIdentifier("bts_prev_page_on", "drawable", mActivity.packageName),
                    mActivity.theme)
                bottomSheetView.findViewById<Button>(R.id.bts_next_page).isEnabled = false
                bottomSheetView.findViewById<Button>(R.id.bts_next_page).background = ResourcesCompat.getDrawable(resources,
                    resources.getIdentifier("bts_next_page_off", "drawable", mActivity.packageName),
                    mActivity.theme)
            }
        }
        // 페이지네이션
        if (dataSet[position].courseContent.length > chunkSize) {
            bottomSheetView.findViewById<TextView>(R.id.course_content).text = contents[currentPage] + "..."
            bottomSheetView.findViewById<TextView>(R.id.bts_pager).text = "${currentPage + 1}/${totalPage}"
            bottomSheetView.findViewById<Button>(R.id.bts_prev_page).isEnabled = false
            bottomSheetView.findViewById<Button>(R.id.bts_prev_page).background = ResourcesCompat.getDrawable(resources,
                resources.getIdentifier("bts_prev_page_off", "drawable", mActivity.packageName),
                mActivity.theme)
            bottomSheetView.findViewById<Button>(R.id.bts_next_page).isEnabled = true
            bottomSheetView.findViewById<Button>(R.id.bts_next_page).background = ResourcesCompat.getDrawable(resources,
                resources.getIdentifier("bts_next_page_on", "drawable", mActivity.packageName),
                mActivity.theme)
        } else {
            bottomSheetView.findViewById<TextView>(R.id.course_content).text = dataSet[position].courseContent
            bottomSheetView.findViewById<TextView>(R.id.bts_pager).text = "${currentPage + 1}/1"
            bottomSheetView.findViewById<Button>(R.id.bts_prev_page).isEnabled = false
            bottomSheetView.findViewById<Button>(R.id.bts_prev_page).background = ResourcesCompat.getDrawable(resources,
                resources.getIdentifier("bts_prev_page_off", "drawable", mActivity.packageName),
                mActivity.theme)
            bottomSheetView.findViewById<Button>(R.id.bts_next_page).isEnabled = false
            bottomSheetView.findViewById<Button>(R.id.bts_next_page).background = ResourcesCompat.getDrawable(resources,
                resources.getIdentifier("bts_next_page_off", "drawable", mActivity.packageName),
                mActivity.theme)
        }

        // 스테이트 초기화
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.behavior.skipCollapsed = true
        bottomSheetDialog.behavior.saveFlags = BottomSheetBehavior.SAVE_SKIP_COLLAPSED
        bottomSheetDialog.behavior.isFitToContents = true
        // 초기화 끝~
        bottomSheetDialog.setContentView(bottomSheetView)

        // 쑈~ 끝은 없는거야~ 😁
        bottomSheetDialog.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (ypv.isFullScreen()) {
                    yp?.pause()
                    mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    mActivity.linear.visibility = View.VISIBLE
                    course_list_view_container.visibility = View.VISIBLE
                    actionbar.visibility = View.VISIBLE

                    ypv_container.layoutParams.height = 0
                    ypv_container.requestLayout()
                } else {
                    yp?.pause()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    // 여기서부터

    fun courseSelectMenu(){
        val builder = AlertDialog.Builder(this.requireContext())
        val dialogView = layoutInflater.inflate(R.layout.course_select_layout, null)

        val course1 = dialogView.findViewById<LinearLayout>(R.id.course1)
        val course2 = dialogView.findViewById<LinearLayout>(R.id.course2)
        val course3 = dialogView.findViewById<LinearLayout>(R.id.course3)
        val course4 = dialogView.findViewById<LinearLayout>(R.id.course4)
        val course5 = dialogView.findViewById<LinearLayout>(R.id.course5)
        val course6 = dialogView.findViewById<LinearLayout>(R.id.course6)

        mClickLister= View.OnClickListener { v ->
            if (v?.id == R.id.course1){
                count=0
                mAlertDialog.dismiss()
            }
            if (v?.id == R.id.course2){
                count=1
                mAlertDialog.dismiss()
            }
            if (v?.id == R.id.course3){
                count=2
                mAlertDialog.dismiss()
            }
            if (v?.id == R.id.course4){
                count=3
                mAlertDialog.dismiss()
            }
            if (v?.id == R.id.course5){
                count=4
                mAlertDialog.dismiss()
            }
            if (v?.id == R.id.course6){
                count=5
                mAlertDialog.dismiss()
            }

            when (count) {
                0 -> name = "횃불코스"
                1 -> name = "희생코스"
                2 -> name = "광장코스"
                3 -> name = "열정코스"
                4 -> name = "영혼코스"
                5 -> {
                    name = "나만의 VR코스"
                    myCourseSelected(application, 1)
                }
            }
            command = name

            if (count != 5) {
                spinnerSelected(application, command, 1)
            }
        }

        course1.setOnClickListener(mClickLister)
        course2.setOnClickListener(mClickLister)
        course3.setOnClickListener(mClickLister)
        course4.setOnClickListener(mClickLister)
        course5.setOnClickListener(mClickLister)
        course6.setOnClickListener(mClickLister)

        if (count != 0){
            dialogView.findViewById<ImageView>(R.id.course1_sign).setBackgroundResource(android.R.color.transparent)
            var array = arrayOf(
                R.id.course1_sign,
                R.id.course2_sign,
                R.id.course3_sign,
                R.id.course4_sign,
                R.id.course5_sign,
                R.id.course6_sign
            )

            dialogView.findViewById<ImageView>(array[count]).setBackgroundResource(R.drawable.current_course_sign)
        }

        mAlertDialog = builder.setView(dialogView).show()

        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (Build.VERSION.SDK_INT < 30){
            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = mAlertDialog.window

            val x = (size.x * 0.6f).toInt()
            val y = (size.y * 0.4f).toInt()

            window?.setLayout(x, y)

        }else{
            val rect = windowManager.currentWindowMetrics.bounds

            val window = mAlertDialog.window
            val x = (rect.width() * 0.6f).toInt()
            val y = (rect.height() * 0.4f).toInt()

            window?.setLayout(x, y)
        }

    }



    fun nextCourseDialog(){
        val builder = AlertDialog.Builder(this.requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_next_course, null)
        val ok_btn = dialogView.findViewById<Button>(R.id.ok_btn)
        val cancel_btn = dialogView.findViewById<Button>(R.id.cancel_btn)
        val mAlertDialog = builder.setView(dialogView).show()
        ok_btn.setOnClickListener{
            prev_position += 1
            playVR_ver2(result, prev_position)
            mAlertDialog.dismiss()
        }
        cancel_btn.setOnClickListener{
            mAlertDialog.dismiss()
        }

        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (Build.VERSION.SDK_INT < 30){
            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = mAlertDialog.window

            val x = (size.x * 0.7f).toInt()
            val y = (size.y * 0.3f).toInt()

            window?.setLayout(x, y)

        }else{
            val rect = windowManager.currentWindowMetrics.bounds

            val window = mAlertDialog.window
            val x = (rect.width() * 0.7f).toInt()
            val y = (rect.height() * 0.3f).toInt()

            window?.setLayout(x, y)
        }

    }

    //여기까지
}