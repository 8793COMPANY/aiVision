package com.corporation8793.aivision.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.corporation8793.aivision.Application
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.R
import com.corporation8793.aivision.recyclerview.course_fragment.CoursePagerAdapter
import com.corporation8793.aivision.room.Course
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllCourseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllCourseFragment(activity: MainActivity) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var nMap: NaverMap? = null
    lateinit var map: View

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val mActivity = activity

    var currentLocation = activity.currentLocation
    var result: List<Course> = listOf()
    var result2: List<Course> = listOf()
    var result3: List<Course> = listOf()
    var result4: List<Course> = listOf()
    var result5: List<Course> = listOf()
    val application = Application().getInstance(mActivity.applicationContext)
    val mCourseFlag = 2

    var listDataSet : MutableList<CoursePagerAdapter.listData> = mutableListOf()



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
        val view: View = inflater.inflate(R.layout.fragment_all_course, container, false)
        map = view.findViewById(R.id.map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        CoroutineScope(Dispatchers.IO).launch {
            if (mCourseFlag != 5) {
                result = application.db.courseDao().getAllByCourseType("횃불코스")
                result2 = application.db.courseDao().getAllByCourseType("희생코스")
                result3 = application.db.courseDao().getAllByCourseType("광장코스")
                result4 = application.db.courseDao().getAllByCourseType("열정코스")
                result5 = application.db.courseDao().getAllByCourseType("영혼코스")

                val lds: MutableList<CoursePagerAdapter.listData> = mutableListOf()
                for ((index, rs) in result.withIndex()) {
                    lds.add(CoursePagerAdapter.listData(index, false, false))
                }
                listDataSet = lds

//                val lds2: MutableList<CoursePagerAdapter.listData> = mutableListOf()
//                for ((index, rs) in result2.withIndex()) {
//                    lds2.add(CoursePagerAdapter.listData(index, false, false))
//                }
//                listDataSet = lds


            } else {
//            myCourseSelected(application, 1)
            }
        }



        mapFragment.getMapAsync {
            nMap = it
            it.apply {
                mapType = NaverMap.MapType.Basic
                setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            moveCamera(
                                CameraUpdate.scrollAndZoomTo(
                                    LatLng(35.153629399146354, 126.88364996966044),
                                    10.0
                                )
                            )
                            currentLocation = LatLng(location.latitude, location.longitude)
                            Log.e(
                                "lastLocation",
                                "onCreateView: location ${location.latitude}, ${location.longitude} !!"
                            )
                        } else {
                            currentLocation = LatLng(
                                result[0].courseLatitude.toDouble(),
                                result[0].courseLongitude.toDouble()
                            )
                            Log.e("lastLocation", "onCreateView: location NULL !!")
                        }
                    }
            }


            course_marker(result,Color.RED )
            course_marker(result2, Color.CYAN )
            course_marker(result3, Color.BLUE )
            course_marker(result4, Color.MAGENTA )
            course_marker(result5, Color.LTGRAY )




        }



        return view;
    }

    fun course_marker(result: List<Course>, color : Int){

        var markers : MutableList<Marker> = mutableListOf()
        var path = PathOverlay()
        var coords : MutableList<LatLng> = mutableListOf()

        for (rs in result) {

            val coordinateInScope = LatLng(rs.courseLatitude.toDouble(), rs.courseLongitude.toDouble())
            coords.add(coordinateInScope)
            val marker =Marker(coordinateInScope)
            marker.iconTintColor = color
            marker.alpha = 0.8f
            markers.add(marker)
        }



        for (mk in markers) {
            mk.map = nMap
        }


        path.apply {
            this.coords = coords
            map = nMap
            path.color =color
        }



    }
}