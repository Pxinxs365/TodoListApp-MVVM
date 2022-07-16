package com.pxinxs.todolistapp.presentation.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pxinxs.todolistapp.databinding.TaskListItemBinding

class TasksAdapter(private val items: List<UiTask>, private val callback: (String) -> Unit) :
    RecyclerView.Adapter<TasksAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding =
            TaskListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvTitle.text = item.title
            tvDescription.text = item.description
            tvDate.text = item.formattedTime
            root.setOnClickListener { callback.invoke(item.id) }
        }
    }

    inner class CustomViewHolder(val binding: TaskListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    data class UiTask(
        val id: String,
        val title: String,
        val description: String,
        var formattedTime: String
    )
}