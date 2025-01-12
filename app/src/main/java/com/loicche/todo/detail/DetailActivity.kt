package com.loicche.todo.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.loicche.todo.detail.ui.theme.TODO_LoicChevalierTheme
import com.loicche.todo.list.Task
import com.loicche.todo.list.TaskListFragment
import com.loicche.todo.list.TaskListFragment.Companion.TASK_KEY
import java.util.UUID

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val task = intent.getSerializableExtra(TASK_KEY) as Task?

        enableEdgeToEdge()
        setContent {
            TODO_LoicChevalierTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Detail(
                        modifier = Modifier.padding(innerPadding),
                        initialTask = task,
                        onValidate = {task ->
                            intent.putExtra(TASK_KEY, task)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Detail(modifier: Modifier = Modifier, initialTask: Task?, onValidate: (Task) -> Unit) {
    var task by remember { mutableStateOf(initialTask ?: Task(id = UUID.randomUUID().toString(), title = "New Task !")) }
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Task Detail",
            modifier = modifier,
            style = MaterialTheme.typography.headlineLarge
        )
        OutlinedTextField(
            task.title,
            {newTitle -> task = task.copy(title = newTitle)},
            modifier
        )
        OutlinedTextField(
            task.description,
            {newDescription -> task = task.copy(description = newDescription)},
            modifier
        )
        Button(
            onClick = { onValidate(task)},
            enabled = true,
            modifier = modifier,
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TODO_LoicChevalierTheme {
        Detail(initialTask = null, onValidate = {})
    }
}