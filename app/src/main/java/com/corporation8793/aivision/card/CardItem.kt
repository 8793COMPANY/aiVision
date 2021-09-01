package com.corporation8793.aivision.card

class CardItem (
    courseType: Int,
    courseName: String,
    courseTime: String,
    courseStart: String,
    courseGoal: String,
    courseDistance: String,
    courseStar_b: Boolean) {

    private var mCourseType : Int = courseType
    private var mCourseName : String = courseName
    private var mCourseTime : String = courseTime
    private var mCourseStart : String = courseStart
    private var mCourseGoal : String = courseGoal
    private var mCourseDistance : String = courseDistance
    private var mCourseStar_b : Boolean = courseStar_b

    fun getCourseName():String {
        return mCourseName
    }
}