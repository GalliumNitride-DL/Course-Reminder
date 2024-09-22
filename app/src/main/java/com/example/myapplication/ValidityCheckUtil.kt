package com.example.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Year

object ValidityCheckUtil {

    public fun isValidDate(date: String): Boolean {
        val l = date.split('.');
        if (l.size != 2) return false;
        val m = l[0].toIntOrNull();
        val d = l[1].toIntOrNull();
        if (m == null || d == null) return false;
        if (m <= 0 || m >= 13) return false;
        if (d <= 0 || d >= 32) return false;
        when (m) {
            4, 6, 9, 11 -> if (d >= 31) return false;
            2 -> if (d > 28) return false;
        }
        return true;
    }

    public fun isValidWeek(weeks: String): Boolean {
        if (weeks.isEmpty()) return false;
        val regex = "^[0-9]*(,[0-9]+)*-[0-9]+(,[0-9]+)*[0-9]*$".toRegex();
        return regex.matches(weeks);
    }
}