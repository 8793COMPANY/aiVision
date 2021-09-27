package com.corporation8793.aivision.fragment

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.corporation8793.aivision.Application
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.R
import com.corporation8793.aivision.recyclerview.CourseAdapter
import com.corporation8793.aivision.recyclerview.RecyclerViewDecoration
import com.corporation8793.aivision.room.AppDatabase
import com.corporation8793.aivision.room.Course
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// : Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFragment(activity: MainActivity)  : Fragment() {
    // : Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var courseAdapter: CourseAdapter
    lateinit var allCourseAdapter: CourseAdapter

    val datas = mutableListOf<Course>()
    val all_datas = mutableListOf<Course>()
    var check : Boolean = false
    val mActivity = activity
    lateinit var application: Application

    var courseType: String = "횃불코스"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
         application= Application().getInstance(mActivity.applicationContext)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View = inflater.inflate(R.layout.fragment_my, container, false)
        var tabs : TabLayout = view.findViewById(R.id.course_tab)
        var recyclerView : RecyclerView = view.findViewById(R.id.recyclerview)
        var all_course : RecyclerView = view.findViewById(R.id.all_course_list)

        var edit_btn : Button = view.findViewById(R.id.edit_btn)
        var save_btn : Button = view.findViewById(R.id.save_btn)

        val display = mActivity.windowManager.defaultDisplay// in case of Activity
/* val display = activity!!.windowManaver.defaultDisplay */ // in case of Fragment
        val size = Point()
        display.getRealSize(size) // or getSize(size)
        val width : Int = ((size.x / 720.0) * 170).toInt()


        tabs.addTab(tabs.newTab().setText("횃불코스"))
        tabs.addTab(tabs.newTab().setText("희생코스"))
        tabs.addTab(tabs.newTab().setText("광장코스"))
        tabs.addTab(tabs.newTab().setText("열정코스"))
        tabs.addTab(tabs.newTab().setText("영혼코스"))


        courseAdapter = CourseAdapter(width)
        allCourseAdapter = CourseAdapter(width)

        recyclerView.adapter = courseAdapter
        recyclerView.addItemDecoration(RecyclerViewDecoration(30))

        all_course.adapter = allCourseAdapter
        all_course.addItemDecoration(RecyclerViewDecoration(30))

        notifyItem()

        changeCourseItem("횃불코스")


        allCourseAdapter.setOnItemClickListener(object : CourseAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: Course, pos : Int, courseName : String) {
                if (check)
                    addItem(courseName)
                    recyclerView.scrollToPosition(datas.size)
                }
            })


        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0-> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            changeCourseItem("횃불코스")
                        }, 200)
                    }
                    1-> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            changeCourseItem("희생코스")
                        }, 200)

                    }
                    2-> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            changeCourseItem("광장코스")
                        }, 200)
                    }
                    3-> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            changeCourseItem("열정코스")
                        }, 200)
                    }
                    4->{
                    Handler(Looper.getMainLooper()).postDelayed({
                        changeCourseItem("영혼코스")
                    }, 200)
                    }

                }
            }

        })



        edit_btn.setOnClickListener{
            if (check){
                edit_btn.setBackgroundResource(R.drawable.my_edit_btn)
                check = false
                courseAdapter.editMode()
                datas.apply {
                    add(Course(datas.size,"","","last_item_image","","","","",""))
                    courseAdapter.datas = datas
                    datas.removeAt(datas.size-1)
                    courseAdapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(courseAdapter.itemCount-1)
                }
            }else{
                edit_btn.setBackgroundResource(R.drawable.my_finish_btn)
                check = true
                courseAdapter.editMode()
//
            }
        }

        save_btn.setOnClickListener{
            Toast.makeText(context, "저장하였습니다.", Toast.LENGTH_SHORT).show()
            var str : String = ""
            for (i in courseAdapter.datas){
                str += i.courseName+","
            }
            if (!str.trim().equals("")){
                Log.e(courseType,str.substring(0,str.length-1))


                Application.prefs.setString("my_course",str.substring(0,str.length-1))
            }else{
                Application.prefs.setString("my_course",str.substring(0,str.length))
            }

        }


        return view
    }

    fun notifyItem(){

            datas.clear()
            datas.apply {
                Log.e("in", "notifyItem")
                CoroutineScope(Dispatchers.IO).launch {

                    var listsize: Int = 0
                    if (!Application.prefs.getString("my_course","none").equals("none")) {
                        Log.e("in","pref")
                        var arrays : List<String> = Application.prefs.getString("my_course","none").split(",")
                        for(i in arrays){
                            Log.e("i확인",i)
                            if (application.db.courseDao().findCourseData(i) != null) {
                                add(application.db.courseDao().findCourseData(i))
                            }
                        }
                        listsize = arrays.size
                    }


                    add(Course(listsize, "", "", "last_item_image", "", "", "", "", ""))


                    Handler(Looper.getMainLooper()).postDelayed({
                        courseAdapter.datas = datas
                        courseAdapter.notifyDataSetChanged()
                    }, 0)

                }


            }
        }


    fun changeCourseItem(courseType : String){

        this.courseType = courseType
        all_datas.clear()
        all_datas.apply {
            Log.e("in", "notifyItem")
            CoroutineScope(Dispatchers.IO).launch {

               addAll(application.db.courseDao().getAllByCourseType(courseType))

                Handler(Looper.getMainLooper()).postDelayed({
                    allCourseAdapter.datas = all_datas
                    allCourseAdapter.notifyDataSetChanged()
                }, 0)

            }


        }
    }



    fun addItem(courseName : String){
        datas.apply {

            Log.e("in","notifyItem")
            CoroutineScope(Dispatchers.IO).launch {
                val list : List<Course> = application.db.courseDao().findByCourseName(courseName)
                addAll(datas.size-1,list)


                Handler(Looper.getMainLooper()).postDelayed({
                    courseAdapter.datas = datas
                    courseAdapter.notifyDataSetChanged()

                }, 0)

            }



        }
    }

    fun allCourseList(courseName: String){
        all_datas.clear()
        all_datas.apply {
            CoroutineScope(Dispatchers.IO).launch {

                if (Application.prefs.getString(courseType,"none").equals("none")) {
                    Log.e("in","dao")

//                    for (i in 0..application.db.courseDao().withoutCourseType(courseName).size-1) {
//                        Log.e("in", i.toString())
//                        add(.get(i))
//                        Log.e("all data", application.db.courseDao().withoutCourseType(courseName).get(i).courseName)
//                    }
                    addAll(application.db.courseDao().withoutCourseType(courseName))
                }else{
                    Log.e("in","pref")
                    var arrays : List<String> = Application.prefs.getString(courseType,"none").split(",")

                    for (i in 0..application.db.courseDao().getAll().size-1) {
                        if (application.db.courseDao().getAll().get(i).courseName in arrays){
                            Log.e("이미 있음 !", "넘어감")
                        }else{
                            add(application.db.courseDao().getAll().get(i))
                        }
                    }


                }


                Handler(Looper.getMainLooper()).postDelayed({
                    allCourseAdapter.datas = all_datas
                    allCourseAdapter.notifyDataSetChanged()
                }, 0)
            }
        }
    }

}