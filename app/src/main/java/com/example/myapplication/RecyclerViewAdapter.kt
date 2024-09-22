package com.example.myapplication

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ValidityCheckUtil

class RecyclerViewAdapter(private val courses: MutableList<CourseData>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTextEdit: EditText = itemView.findViewById(R.id.nameTextEdit);
        val locationTextEdit: EditText = itemView.findViewById(R.id.locationTextEdit);
        val hourTimeEdit: EditText = itemView.findViewById(R.id.hourTimeEdit);
        val minuteTimeEdit: EditText = itemView.findViewById(R.id.minuteTimeEdit);
        val toWeekEdit: EditText = itemView.findViewById(R.id.toWeekEdit);
        val dayOfWeekTextEdit: EditText = itemView.findViewById(R.id.dayOfWeekTextEdit);
        val enabledSwitch: Switch = itemView.findViewById(R.id.enabledSwitch);
        val removeButton: Button = itemView.findViewById(R.id.removeButton);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false);
        return ViewHolder(itemView);
    }

    override fun getItemCount(): Int = courses.size;

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position];

        holder.nameTextEdit.setText(course.name);
        holder.locationTextEdit.setText(course.location);
        holder.hourTimeEdit.setText(course.startHour.toString());
        holder.minuteTimeEdit.setText(course.startMinute.toString());
        holder.toWeekEdit.setText(course.weeks);
        holder.dayOfWeekTextEdit.setText(course.dayOfWeek.toString());

        holder.enabledSwitch.isChecked = course.enabled;

        holder.nameTextEdit.doAfterTextChanged { text: Editable? -> updateCourse(holder.adapterPosition, R.id.nameTextEdit, text?.toString() ?: "") };
        holder.locationTextEdit.doAfterTextChanged { text: Editable? -> updateCourse(holder.adapterPosition, R.id.locationTextEdit, text?.toString() ?: "") };
        holder.hourTimeEdit.doAfterTextChanged { text: Editable? -> updateCourse(holder.adapterPosition, R.id.hourTimeEdit, text?.toString() ?: "") };
        holder.minuteTimeEdit.doAfterTextChanged { text: Editable? -> updateCourse(holder.adapterPosition, R.id.minuteTimeEdit, text?.toString() ?: "") };
        holder.toWeekEdit.doAfterTextChanged { text: Editable? -> updateCourse(holder.adapterPosition, R.id.toWeekEdit, text?.toString() ?: "") };
        holder.dayOfWeekTextEdit.doAfterTextChanged { text: Editable? -> updateCourse(holder.adapterPosition, R.id.dayOfWeekTextEdit, text?.toString() ?: "") };

        holder.enabledSwitch.setOnCheckedChangeListener { _, isChecked -> updateCourseEnabled(holder.adapterPosition, isChecked) };
        holder.removeButton.setOnClickListener { removeCourse(holder.adapterPosition) };
    }

    private fun removeCourse(position: Int) {
        courses.removeAt(position);
        notifyItemRemoved(position);
    }

    public fun addCourse(courseData: CourseData) {
        courses.add(0, courseData);
        notifyItemInserted(0);
    }

    private fun updateCourse(position: Int, id: Int, text: String)
    {
        when (id) {
            R.id.nameTextEdit -> courses[position] = courses[position].copy(name = text);
            R.id.locationTextEdit -> courses[position] = courses[position].copy(location = text);
            R.id.hourTimeEdit -> {
                val result = text.toIntOrNull();
                if (result != null)
                    courses[position] = courses[position].copy(startHour = result);
            }
            R.id.minuteTimeEdit -> {
                val result = text.toIntOrNull();
                if (result != null)
                    courses[position] = courses[position].copy(startMinute = result);
            }
            R.id.toWeekEdit -> {
                if (ValidityCheckUtil.isValidWeek(text))
                    courses[position] = courses[position].copy(weeks = text);
            }
            R.id.dayOfWeekTextEdit -> {
                val result = text.toIntOrNull();
                if (result != null)
                    courses[position] = courses[position].copy(dayOfWeek = result);
            }
        }
    }

    private fun updateCourseEnabled(position: Int, enabled: Boolean) {
        courses[position] = courses[position].copy(enabled = enabled);
    }
}