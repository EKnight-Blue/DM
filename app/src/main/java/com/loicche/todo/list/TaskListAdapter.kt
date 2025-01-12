package com.loicche.todo.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.loicche.todo.R
import com.loicche.todo.databinding.ItemTaskBinding
import com.loicche.todo.list.TaskListFragment.Companion.TASK_KEY


object TaskCallBack: DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.title == newItem.title && oldItem.description == newItem.description
    }

}

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskCallBack) {
    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}


    inner class TaskViewHolder(itemView: View, private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description

            binding.taskDelete.setOnClickListener {
                onClickDelete(task)
            }
            binding.taskEdit.setOnClickListener {
                onClickEdit(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding: ItemTaskBinding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context))
        return TaskViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}