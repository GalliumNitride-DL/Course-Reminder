package com.zhuoruigan.coursereminder

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import java.io.File

object PersistentDataManager {
    private const val REQUEST_CODE_READ = 100;
    private const val REQUEST_CODE_WRITE = 200;

    fun saveCourseData(activity: Activity, courses: MutableList<CourseData>) {
        val g = Gson();
        val json = g.toJson(courses);

        //val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        val file = File(activity.getExternalFilesDir(null), "courses.json");
        file.printWriter().use { out -> out.print(json) };
    }

    fun loadCourseData(activity: Activity): MutableList<CourseData> {
        val g = Gson();

        //val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        val file = File(activity.getExternalFilesDir(null), "courses.json");
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