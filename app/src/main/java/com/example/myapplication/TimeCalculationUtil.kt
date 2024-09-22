package com.example.myapplication

import android.util.Log
import com.example.myapplication.CourseData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object TimeCalculationUtil {

    private lateinit var firstWeekDate: String;
    private lateinit var firstDay: LocalDateTime;

    private fun getFirstMonday(): LocalDateTime {
        val md = firstWeekDate.split('.');
        val m = md[0].toInt(); val d = md[1].toInt();
        val now = LocalDateTime.now();
        val localDate = LocalDate.of(now.year, m, d);

        val currentYear = now.year;
        val targetYear = if (localDate.isAfter(LocalDate.now())) currentYear - 1 else currentYear
        val targetDate = localDate.withYear(targetYear)

        val dayOfWeek = targetDate.dayOfWeek.value
        val closestMonday = targetDate.minusDays(dayOfWeek.toLong() - 1)

        return closestMonday.atStartOfDay()
    }

    public fun getNextCourse(): CourseData? {
        firstWeekDate = MainActivity.firstWeekDate;
        firstDay = getFirstMonday();
        val coursesComingToday = MainActivity.courses.filter(::isTodayComingCourse).sortedWith(compareBy(CourseData::startHour, CourseData::startMinute));
        return if (coursesComingToday.isEmpty()) null else coursesComingToday.first();
    }

    private fun isTodayComingCourse(course: CourseData): Boolean {
        val currentTime = LocalDateTime.now();

        if (!course.enabled) return false;

        val weeksBetween = ChronoUnit.WEEKS.between(firstDay.toLocalDate(), currentTime.toLocalDate()) + 1;

        var flag = false;
        val weeks = course.weeks.split(',');
        for (w in weeks) {
            if (!w.contains('-')) {
                val week = w.toLong();
                if (week == weeksBetween) {
                    flag = true; break;
                }
            }
            else {
                val startEnd = w.split('-');
                val start = startEnd[0].toLong();
                val end = startEnd[1].toLong();
                if (weeksBetween in start..end) {
                    flag = true; break;
                }
            }
        }
        if (!flag) return false;

        val daysBetween = ChronoUnit.DAYS.between(firstDay.toLocalDate(), currentTime.toLocalDate());
        if (daysBetween % 7 != course.dayOfWeek.toLong() - 1) return false;

        val courseTime = LocalDateTime.of(currentTime.year, currentTime.month, currentTime.dayOfMonth, course.startHour, course.startMinute);
        val timeBetween = ChronoUnit.SECONDS.between(currentTime, courseTime);
        if (timeBetween < 0) return false;
        return true;
    }
}