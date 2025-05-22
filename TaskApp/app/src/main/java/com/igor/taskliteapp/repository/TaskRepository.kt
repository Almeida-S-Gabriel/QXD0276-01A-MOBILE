package com.igor.taskliteapp.repository

import com.igor.taskliteapp.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskRepository {
    private val _tasks = MutableStateFlow(
        listOf(
            Task(1, "meu"),
            Task(2, "pinto")
        )
    )

    val tasks : StateFlow<List<Task>> = _tasks

    fun toggleTask(id: Int){
        _tasks.value = _tasks.value.map {
            if (it.id == id) it.copy(isDone = !it.isDone) else it
        }
    }

    fun addTask(title: String){
        val newTask = Task(id = _tasks.value.size + 1, title=title)
        _tasks.value = _tasks.value + newTask
    }
}