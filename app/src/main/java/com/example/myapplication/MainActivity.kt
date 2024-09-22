package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ValidityCheckUtil


class MainActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: RecyclerViewAdapter;

    private lateinit var addCourseButton: Button;
    private lateinit var saveDataButton: Button;
    private lateinit var firstWeekText: EditText;

    private lateinit var pinNotificationManager: PinNotificationManager;

    companion object {
        lateinit var courses: MutableList<CourseData>;
        lateinit var firstWeekDate: String;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mylayout);
        View.inflate(this, R.layout.mylayout, null);

        courses = PersistentDataManager.loadCourseData(this);
        firstWeekDate = PersistentDataManager.loadFirstWeekData(this);

        initializeRecycler();

        addCourseButton = findViewById(R.id.addCourseButton);
        saveDataButton = findViewById(R.id.saveDataButton);
        firstWeekText = findViewById(R.id.firstWeekText);

        addCourseButton.setOnClickListener {
            adapter.addCourse(CourseData());
            recyclerView.scrollToPosition(0);
        }
        saveDataButton.setOnClickListener {
            PersistentDataManager.saveCourseData(this, courses);
            val date = firstWeekText.text.toString();
            if (ValidityCheckUtil.isValidDate(date))
                PersistentDataManager.saveFirstWeekData(this, date);
        }
        firstWeekText.setText(firstWeekDate);

        val notificationIntent = Intent(this, PinNotificationManager::class.java);
        startForegroundService(notificationIntent);

    }

    private fun initializeRecycler() {
        recyclerView = findViewById(R.id.recyclerView1);

        recyclerView.layoutManager = LinearLayoutManager(this);

        adapter = RecyclerViewAdapter(courses);
        recyclerView.adapter = adapter;

        val itemAnimator = DefaultItemAnimator();
        itemAnimator.addDuration = 500; itemAnimator.removeDuration = 500;
        recyclerView.itemAnimator = itemAnimator;
    }

}