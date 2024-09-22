package com.example.myapplication

data class CourseData(
    val name: String = "微积分A",
    val location: String = "东一",
    val startHour: Int = 12,
    val startMinute: Int = 15,
    val dayOfWeek: Int = 1,
    val weeks: String = "1-7,14,15",
    val enabled: Boolean = true


)
