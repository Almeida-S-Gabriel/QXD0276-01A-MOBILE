package com.example.tasksmanagerapp.models

enum class TaskPriority{BAIXA,MEDIA,ALTA}
enum class TaskCategory{TRABALHO,CASA,ESTUDOS}

data class Task(
    val name: String,
    val isComplete: Boolean = false,
    val category: TaskCategory = TaskCategory.CASA,
    val priority: TaskPriority = TaskPriority.MEDIA
)

