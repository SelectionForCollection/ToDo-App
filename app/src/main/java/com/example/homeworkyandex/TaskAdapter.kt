package com.example.homeworkyandex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworkyandex.databinding.RecyclerItemBinding

class TaskAdapter(mDataSet: ArrayList<Task>): RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    val taskList = mDataSet

    class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerItemBinding.bind(itemView)
        fun bind(task: Task) {

            binding.itemCheckbox.isChecked = task.isDone
            binding.itemText.text = task.title

            binding.itemCheckbox.setOnClickListener {
                task.isDone = binding.itemCheckbox.isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(taskList[position])

    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
