package com.loicche.todo.list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.loicche.todo.databinding.FragmentTaskListBinding
import com.loicche.todo.detail.DetailActivity
import java.util.UUID




class TaskListFragment : Fragment() {
    companion object {
        const val TASK_KEY = "task"
    }

    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private val adapter = TaskListAdapter()
    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra(TASK_KEY) as Task?
        taskList = taskList + task!!
        adapter.submitList(taskList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adapter.submitList(taskList)
        return FragmentTaskListBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTaskListBinding.bind(view)

        binding.taskViewList.adapter = this.adapter
        this.adapter.onClickDelete=  {task ->
            this.taskList = this.taskList.filterNot { it.id == task.id }
            this.adapter.submitList(this.taskList)
        }

        binding.addTaskButton.setOnClickListener{
            /*
            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask
            adapter.submitList(taskList)
            */
            createTask.launch(Intent(context, DetailActivity::class.java))
        }
    }
}