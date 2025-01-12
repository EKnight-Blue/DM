package com.loicche.todo.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.loicche.todo.data.Api
import com.loicche.todo.databinding.FragmentTaskListBinding
import com.loicche.todo.detail.DetailActivity
import kotlinx.coroutines.launch


interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
    fun onClickShare(task: Task)
}

class TaskListFragment : Fragment() {
    companion object {
        const val TASK_KEY = "task"
        const val LIST_KEY = "full list"
    }

    private var taskList: List<Task> = listOf()

    val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            viewModel.remove(task)
        }
        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TASK_KEY, task)
            editTask.launch(intent)
        }

        override fun onClickShare(task: Task) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${task.title}\n${task.description}")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, "Share your task"))
        }
    }
    private val adapter = TaskListAdapter(adapterListener)
    private var userName: TextView? = null
    private val viewModel: TaskListViewModel by viewModels()

    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra(TASK_KEY) as Task?
        viewModel.create(task!!)
    }

    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra(TASK_KEY) as Task?
        viewModel.update(task!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        var index: Int = 0
//        while (savedInstanceState?.getSerializable(LIST_KEY + index) != null) {
//            taskList = taskList + savedInstanceState.getSerializable(LIST_KEY + index) as Task
//            index += 1
//        }
//        adapter.submitList(taskList)
        return FragmentTaskListBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTaskListBinding.bind(view)

        binding.taskViewList.adapter = this.adapter

        binding.addTaskButton.setOnClickListener{
            createTask.launch(Intent(context, DetailActivity::class.java))
        }
        userName = binding.userName

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                adapter.submitList(newList)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        taskList.forEachIndexed({ idx, task ->
//            outState.putSerializable(LIST_KEY + idx, task)
//        })
//        outState.putSerializable(LIST_KEY + taskList.count(), null)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            userName?.text = user.name
        }
        viewModel.refresh()

    }
}