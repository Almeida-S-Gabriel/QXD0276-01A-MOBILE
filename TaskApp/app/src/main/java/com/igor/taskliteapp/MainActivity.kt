package com.igor.taskliteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.igor.taskliteapp.ui.screens.TaskListIScreen
import com.igor.taskliteapp.ui.theme.TaskLiteAppTheme
import com.igor.taskliteapp.viewmodel.TaskViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskLiteAppTheme {
                val taskViewModel: TaskViewModel = viewModel()
                TaskListIScreen(viewModel = taskViewModel)
            }
        }
    }
}