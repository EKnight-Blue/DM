package com.loicche.todo.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.loicche.todo.databinding.ActivityMainBinding
import com.loicche.todo.databinding.FragmentTaskListBinding
import java.util.UUID


class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )


    private val adapter = TaskListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adapter.submitList(taskList)
        val binding = ActivityMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTaskListBinding.bind(view)

        val recyclerView = binding.taskViewList
        recyclerView.adapter = this.adapter


        val button = binding.floatingActionButton

        button.setOnClickListener {
            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask

            adapter.submitList(taskList)
        }

    }
}