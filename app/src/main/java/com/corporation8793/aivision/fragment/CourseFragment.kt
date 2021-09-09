package com.corporation8793.aivision.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.corporation8793.aivision.Application
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.R
import com.corporation8793.aivision.room.Course
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CourseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourseFragment(activity: MainActivity) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
        val view: View = inflater.inflate(R.layout.fragment_course, container, false)
        val course_list : Spinner = view.findViewById(R.id.course_list)
        val myMap : TextView = view.findViewById(R.id.map)
        val application = Application().getInstance(mActivity.applicationContext)

        course_list.adapter = ArrayAdapter.createFromResource(
            view.context,
            R.array.itemList,
            android.R.layout.simple_spinner_item
        )

        course_list.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0, 1, 2, 3, 4 -> spinnerSelected(application, course_list.selectedItem.toString())
                    5 -> mActivity.replaceFragment(MyFragment(), 3)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return view
    }

    fun spinnerSelected(application : Application, name : String) {
        var result: List<Course>

        CoroutineScope(Dispatchers.IO).launch {
            result = application.db.courseDao().getAllByCourseType(name)

            for (DL in result.indices) {
                Log.i("CourseFragment", "${result[DL]}")
            }
        }
    }
}