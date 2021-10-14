package com.corporation8793.aivision.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.corporation8793.aivision.Application
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.R
import com.corporation8793.aivision.recyclerview.CourseAdapter
import com.corporation8793.aivision.recyclerview.RecyclerViewDecoration
import com.corporation8793.aivision.room.Course
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
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

    lateinit var mClickLister : View.OnClickListener

    lateinit var mAlertDialog : AlertDialog


    var count : Int = 0


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
        val width : Int = ((size.x / 720.0) * 190).toInt()

//        val width : Int = ((size.x / 1536.0) * 280).toInt()



        tabs.addTab(tabs.newTab().setText("횃불코스"))
        tabs.addTab(tabs.newTab().setText("희생코스"))
        tabs.addTab(tabs.newTab().setText("광장코스"))
        tabs.addTab(tabs.newTab().setText("열정코스"))
        tabs.addTab(tabs.newTab().setText("영혼코스"))

//        for (i in 0 until tabs.getTabCount()) {
//            val tab = (tabs.getChildAt(0) as ViewGroup).getChildAt(i)
//            val p = tab.layoutParams as MarginLayoutParams
//            p.setMargins(0, 0, 100, 0)
//            tab.requestLayout()
//        }


        courseAdapter = CourseAdapter(width)
        allCourseAdapter = CourseAdapter(width)

        recyclerView.adapter = courseAdapter
        recyclerView.addItemDecoration(RecyclerViewDecoration(30))

        all_course.adapter = allCourseAdapter
        all_course.addItemDecoration(RecyclerViewDecoration(30))

        notifyItem()

        changeCourseItem("횃불코스")


        allCourseAdapter.setOnItemClickListener(object : CourseAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: Course, pos: Int, courseName: String) {
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
                    0 -> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            changeCourseItem("횃불코스")
                        }, 200)
                    }
                    1 -> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            changeCourseItem("희생코스")
                        }, 200)

                    }
                    2 -> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            changeCourseItem("광장코스")
                        }, 200)
                    }
                    3 -> {
                        courseSelectMenu()
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            changeCourseItem("열정코스")
//                        }, 200)
                    }
                    4 -> {
                        nextCourseDialog()
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        changeCourseItem("영혼코스")
//                    }, 200)
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
                    add(Course(datas.size, "", "", "last_item_image", "", "", "", "", ""))
                    courseAdapter.datas = datas
                    datas.removeAt(datas.size - 1)
                    courseAdapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(courseAdapter.itemCount - 1)
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
                Log.e(courseType, str.substring(0, str.length - 1))


                Application.prefs.setString("my_course", str.substring(0, str.length - 1))
            }else{
                Application.prefs.setString("my_course", str.substring(0, str.length))
            }

        }

        mClickLister= object : View.OnClickListener{ override fun onClick(v: View?) {
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
                    if (!Application.prefs.getString("my_course", "none").equals("none")) {
                        Log.e("in", "pref")
                        var arrays : List<String> = Application.prefs.getString("my_course", "none").split(
                            ","
                        )
                        for(i in arrays){
                            Log.e("i확인", i)
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


    fun changeCourseItem(courseType: String){

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



    fun addItem(courseName: String){
        datas.apply {

            Log.e("in", "notifyItem")
            CoroutineScope(Dispatchers.IO).launch {
                val list : List<Course> = application.db.courseDao().findByCourseName(courseName)
                addAll(datas.size - 1, list)


                Handler(Looper.getMainLooper()).postDelayed({
                    courseAdapter.datas = datas
                    courseAdapter.notifyDataSetChanged()

                }, 0)

            }



        }
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

            val x = (size.x * 0.45f).toInt()
            val y = (size.y * 0.4f).toInt()

            window?.setLayout(x, y)

        }else{
            val rect = windowManager.currentWindowMetrics.bounds

            val window = mAlertDialog.window
            val x = (rect.width() * 0.45f).toInt()
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
            Toast.makeText(context, "ok!", Toast.LENGTH_SHORT).show()
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
            val y = (size.y * 0.2f).toInt()

            window?.setLayout(x, y)

        }else{
            val rect = windowManager.currentWindowMetrics.bounds

            val window = mAlertDialog.window
            val x = (rect.width() * 0.7f).toInt()
            val y = (rect.height() * 0.2f).toInt()

            window?.setLayout(x, y)
        }

    }

    //여기까지


    fun allCourseList(courseName: String){
        all_datas.clear()
        all_datas.apply {
            CoroutineScope(Dispatchers.IO).launch {

                if (Application.prefs.getString(courseType, "none").equals("none")) {
                    Log.e("in", "dao")

//                    for (i in 0..application.db.courseDao().withoutCourseType(courseName).size-1) {
//                        Log.e("in", i.toString())
//                        add(.get(i))
//                        Log.e("all data", application.db.courseDao().withoutCourseType(courseName).get(i).courseName)
//                    }
                    addAll(application.db.courseDao().withoutCourseType(courseName))
                }else{
                    Log.e("in", "pref")
                    var arrays : List<String> = Application.prefs.getString(courseType, "none").split(
                        ","
                    )

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