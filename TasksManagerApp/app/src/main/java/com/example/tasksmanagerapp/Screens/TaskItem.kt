package com.example.tasksmanagerapp.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.tasksmanagerapp.models.Task
import com.example.tasksmanagerapp.models.TaskPriority

@Composable
fun TaskItem (task: Task, onToggleCompletion : () -> Unit, onDelete : () -> Unit) {
    val scale by animateFloatAsState (if (task.isComplete ) 1.05f else 1f)
    val backgroundColor = when (task.priority ) {
        TaskPriority .BAIXA -> Color(0xFFC8E6C9 )
        TaskPriority .MEDIA -> Color(0xFFFFF59D )
        TaskPriority .ALTA -> Color(0xFFFFCDD2 )
    }
    AnimatedVisibility(visible = true) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                . padding(vertical = 4.dp)
                . background(backgroundColor , RoundedCornerShape(8.dp))
                . pointerInput(Unit) {
                    detectHorizontalDragGestures { _, _ ->
                        onDelete ()
                    }
                }
                .padding(16.dp),
            verticalAlignment = Alignment .CenterVertically
        ) {
            Checkbox (checked = task.isComplete , onCheckedChange = { onToggleCompletion () })
            Text(
                text = task.name,
                modifier = Modifier .weight(1f),
                color = if (task.isComplete ) Color .Gray else Color .Black
            )
        }
    }
}