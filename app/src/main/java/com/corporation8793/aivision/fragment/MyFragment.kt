package com.corporation8793.aivision.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


        tabs.addTab(tabs.newTab().setText("횃불코스"))
        tabs.addTab(tabs.newTab().setText("희생코스"))
        tabs.addTab(tabs.newTab().setText("광장코스"))
        tabs.addTab(tabs.newTab().setText("열정코스"))
        tabs.addTab(tabs.newTab().setText("영혼코스"))


        courseAdapter = CourseAdapter()
        allCourseAdapter = CourseAdapter()

        recyclerView.adapter = courseAdapter
        recyclerView.addItemDecoration(RecyclerViewDecoration(30))

        all_course.adapter = allCourseAdapter
        all_course.addItemDecoration(RecyclerViewDecoration(30))

        notifyItem("횃불코스")



        allCourseAdapter.setOnItemClickListener(object : CourseAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: Course, pos : Int, courseName : String) {
                if (check)
                    addItem(courseName)
                    recyclerView.scrollToPosition(datas.size)
                }
            })


        all_datas.apply {
            CoroutineScope(Dispatchers.IO).launch {
                Log.e("size check", application.db.courseDao().getAll().size.toString());
                if (application.db.courseDao().getAll().size != 0) {
                    for (i in 0..application.db.courseDao().getAll().size-1) {
                        Log.e("in", i.toString())
                        add(application.db.courseDao().getAll().get(i))
                    }
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    allCourseAdapter.datas = all_datas
                    allCourseAdapter.notifyDataSetChanged()
                }, 0)
            }
        }



        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0-> notifyItem("횃불코스")
                    1-> notifyItem("희생코스")
                    2-> notifyItem("광장코스")
                    3-> notifyItem("열정코스")
                    4-> notifyItem("영혼코스")

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
                    courseAdapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(courseAdapter.itemCount-1)
                }
            }else{
                edit_btn.setBackgroundResource(R.drawable.my_finish_btn)
                check = true
                courseAdapter.editMode()
                datas.removeAt(datas.size-1)
            }
        }

        save_btn.setOnClickListener{
            Toast.makeText(context, "저장하였습니다.", Toast.LENGTH_SHORT).show()
            var str : String = ""
            for (i in courseAdapter.datas){
                str += i.courseName+","
            }
            Log.e(courseType,str.substring(0,str.length-2))

            Application.prefs.setString(courseType,str.substring(0,str.length-2))
        }


        return view
    }

    fun notifyItem(courseType : String){

            this.courseType = courseType
            datas.clear()
            datas.apply {
//            add(CourseData(img = R.color.black, item_name = "mary"))
//            add(CourseData(img = R.color.purple_200, item_name = "jenny"))
//            add(CourseData(img = R.color.teal_200, item_name = "jhon"))
//            add(CourseData(img = R.color.design_default_color_error, item_name = "ruby"))
//            add(CourseData(img = R.color.design_default_color_surface, item_name = "yuna"))
                Log.e("in", "notifyItem")
                CoroutineScope(Dispatchers.IO).launch {

                    lateinit var list: List<Course>
                    var listsize: Int = 0
                    if (Application.prefs.getString(courseType,"none").equals("none")) {
                        Log.e("in","dao")
                          list =   application.db.courseDao().getAllByCourseType(courseType)
                            for (i in list) {
                                add(i)
                            }
                        listsize = list.size
                    }else{
                        Log.e("in","pref")
                        var arrays : List<String> = Application.prefs.getString(courseType,"none").split(",")
                        for(i in arrays){
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



    fun addItem(courseName : String){
        datas.apply {

            Log.e("in","notifyItem")
            CoroutineScope(Dispatchers.IO).launch {
                val list : List<Course> = application.db.courseDao().findByCourseName(courseName)
                for (i in list){
//                    Log.e("list",list.toString())
                    add(i)
                }


//                add(Course(list.size,"","","last_item_image","","","","",""))

                Handler(Looper.getMainLooper()).postDelayed({
                    courseAdapter.datas = datas
                    courseAdapter.notifyDataSetChanged()

                }, 0)

            }



        }
    }


}