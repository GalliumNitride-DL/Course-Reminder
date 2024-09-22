package com.example.myapplication

import android.content.Context
import com.google.gson.Gson
import java.io.File

object PersistentDataManager {
    fun saveCourseData(context: Context, courses: MutableList<CourseData>) {
        val g = Gson();
        val json = g.toJson(courses);

        val file = File(context.filesDir, "courses.json");
        file.printWriter().use { out -> out.print(json) };
    }

    fun loadCourseData(context: Context): MutableList<CourseData> {
        val g = Gson();
        val file = File(context.filesDir, "courses.json");
        return if (file.exists()) file.reader().use { reader -> g.fromJson(reader.readText(), Array<CourseData>::class.java).toMutableList() }
        else mutableListOf();
    }

    fun saveFirstWeekData(context: Context, date: String) {
        val file = File(context.filesDir, "firstWeekDate.txt");
        file.printWriter().use { out -> out.write(date) };
    }

    fun loadFirstWeekData(context: Context): String {
        val file = File(context.filesDir, "firstWeekDate.txt");
        return if (file.exists()) file.reader().use { reader -> reader.readText() };
        else "9.1";
    }
}