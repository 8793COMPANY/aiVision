package com.corporation8793.aivision.card

class CardItem (
    courseType: Int,
    courseName: String,
    courseTime: String,
    courseStart: String,
    courseGoal: String,
    courseDistance: String,
    courseStar: Boolean) {

    private var mCourseType : Int = courseType
    private var mCourseName : String = courseName
    private var mCourseTime : String = courseTime
    private var mCourseStart : String = courseStart
    private var mCourseGoal : String = courseGoal
    private var mCourseDistance : String = courseDistance
    private var mCourseStar : Boolean = courseStar

    fun getCourseType():Int {
        return mCourseType
    }

    fun getCourseName():String {
        return mCourseName
    }

    fun getCourseTime():String {
        return mCourseTime
    }

    fun getCourseStart():String {
        return mCourseStart
    }

    fun getCourseGoal():String {
        return mCourseGoal
    }

    fun getCourseDistance():String {
        return mCourseDistance
    }

    fun getCourseStar():Boolean {
        return mCourseStar
    }
}