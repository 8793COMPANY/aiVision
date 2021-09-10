package com.corporation8793.aivision.fragment

import android.os.Bundle
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFragment(activity: MainActivity)  : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var courseAdapter: CourseAdapter
    lateinit var allCourseAdapter: CourseAdapter

    val datas = mutableListOf<Course>()
    val all_datas = mutableListOf<Course>()
    var check : Boolean = false
    val mActivity = activity


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
        var view : View = inflater.inflate(R.layout.fragment_my, container, false)
        var tabs : TabLayout = view.findViewById(R.id.course_tab)
//        var linear : LinearLayout = view.findViewById(R.id.edit_linear)
        var recyclerView : RecyclerView = view.findViewById(R.id.recyclerview)
        var all_course : RecyclerView = view.findViewById(R.id.all_course_list)

        var edit_btn : Button = view.findViewById(R.id.edit_btn)
        var save_btn : Button = view.findViewById(R.id.save_btn)

        val application = Application().getInstance(mActivity.applicationContext)


        //room data 뿌리기
//        val db = Application().getInstance(mActivity.applicationContext)


        //room data 뿌리기

        tabs.addTab(tabs.newTab().setText("횃불코스"))
        tabs.addTab(tabs.newTab().setText("희생코스"))
        tabs.addTab(tabs.newTab().setText("광장코스"))
        tabs.addTab(tabs.newTab().setText("열정코스"))
        tabs.addTab(tabs.newTab().setText("영혼코스"))
        tabs.addTab(tabs.newTab().setText("내맘코스"))


        courseAdapter = CourseAdapter()
        allCourseAdapter = CourseAdapter()

        recyclerView.adapter = courseAdapter
        recyclerView.addItemDecoration(RecyclerViewDecoration(30))

        all_course.adapter = allCourseAdapter
        all_course.addItemDecoration(RecyclerViewDecoration(30))

        datas.apply {
//            add(CourseData(img = R.color.black, item_name = "mary"))
//            add(CourseData(img = R.color.purple_200, item_name = "jenny"))
//            add(CourseData(img = R.color.teal_200, item_name = "jhon"))
//            add(CourseData(img = R.color.design_default_color_error, item_name = "ruby"))
//            add(CourseData(img = R.color.design_default_color_surface, item_name = "yuna"))

            courseAdapter.datas = datas
            courseAdapter.notifyDataSetChanged()

        }

        all_datas.apply {

            //이 부분 확인해주세요
            Thread {
                Log.e("size check", application.db.courseDao().getAll().size.toString());
                if (application.db.courseDao().getAll().size != 0) {
                    for (i in 0..application.db.courseDao().getAll().size) {
                        Log.e("in", i.toString())
                        add(application.db.courseDao().findPosData(i))
                    }

                    allCourseAdapter.datas = all_datas
                    allCourseAdapter.notifyDataSetChanged()

                }

                }.start()


            }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> courseAdapter.notifyDataSetChanged()
//                    0-> text.setText("0")
//                    1-> text.setText("1")
//                    2-> text.setText("2")
//                    3-> text.setText("3")
//                    4-> text.setText("4")
//                    5->text.setText("5")

                }
            }

        })



        edit_btn.setOnClickListener{
            if (check){
                edit_btn.setBackgroundResource(R.drawable.my_edit_btn)
                check = false
                courseAdapter.editMode()
            }else{
                edit_btn.setBackgroundResource(R.drawable.my_finish_btn)
                check = true
                courseAdapter.editMode()
            }
        }

        save_btn.setOnClickListener{
            Toast.makeText(context, "저장하였습니다.", Toast.LENGTH_SHORT).show()
        }


        return view
    }


}