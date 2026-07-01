package com.maher.focus.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maher.focus.data.FocusCategory
import com.maher.focus.data.TaskEntity
import com.maher.focus.data.TaskPriority
import com.maher.focus.data.TaskStatus
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TodayScreen(
    tasks: List<TaskEntity>,
    hardDayMode: Boolean,
    onHardDayChange: (Boolean) -> Unit,
    onTaskChecked: (TaskEntity, Boolean) -> Unit,
    onTaskWaiting: (TaskEntity) -> Unit,
    onTaskDelete: (TaskEntity) -> Unit
) {
    val today = LocalDate.now().toEpochDay()
    val activeTasks = tasks.filter { it.statusKey != TaskStatus.DONE.key }
    val completedToday = tasks.count { task ->
        task.completedAtMillis?.let { millis ->
            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay() == today
        } ?: false
    }
    val dueToday = activeTasks.filter { it.priorityKey == TaskPriority.DUE_TODAY.key || it.dueDateEpochDay == today }
    val importantWeek = activeTasks.filter {
        it.priorityKey == TaskPriority.IMPORTANT_WEEK.key && (it.dueDateEpochDay == null || it.dueDateEpochDay <= today + 7)
    }
    val waiting = activeTasks.filter {
        it.priorityKey == TaskPriority.WAITING.key || it.statusKey == TaskStatus.WAITING.key || it.statusKey == TaskStatus.BLOCKED.key
    }
    val dayTotal = dueToday.size + importantWeek.size
    val progress = if (dayTotal == 0) 0f else (completedToday.toFloat() / (completedToday + dayTotal).coerceAtLeast(1)).coerceIn(0f, 1f)

    val hardDayTasks = buildHardDayList(activeTasks)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        ScreenTitle(
            title = "Aujourd’hui",
            subtitle = "Le but n’est pas de tout faire. Le but est de choisir sans te disperser."
        )

        CalmCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Mode journée difficile", fontWeight = FontWeight.SemiBold)
                    Text("Réduit la journée à 3 tâches maximum.", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = hardDayMode, onCheckedChange = onHardDayChange)
            }
        }

        CalmCard {
            Text("Progression du jour", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            Text("$completedToday tâche(s) terminée(s) aujourd’hui", style = MaterialTheme.typography.bodySmall)
        }

        if (hardDayMode) {
            TaskSection(
                title = "Seulement ces 3 tâches",
                tasks = hardDayTasks,
                onTaskChecked = onTaskChecked,
                onTaskWaiting = onTaskWaiting,
                onTaskDelete = onTaskDelete,
                emptyMessage = "Aucune tâche disponible. Ajoute une tâche simple pour démarrer."
            )
            Button(onClick = { onHardDayChange(false) }, modifier = Modifier.fillMaxWidth()) {
                Text("Afficher la journée normale")
            }
        } else {
            TaskSection(
                title = "Obligatoire aujourd’hui",
                tasks = dueToday,
                onTaskChecked = onTaskChecked,
                onTaskWaiting = onTaskWaiting,
                onTaskDelete = onTaskDelete,
                emptyMessage = "Rien d’obligatoire aujourd’hui. Ne remplis pas le vide inutilement."
            )
            TaskSection(
                title = "Important cette semaine",
                tasks = importantWeek,
                onTaskChecked = onTaskChecked,
                onTaskWaiting = onTaskWaiting,
                onTaskDelete = onTaskDelete,
                emptyMessage = "Aucune tâche importante cette semaine."
            )
            TaskSection(
                title = "En attente / bloqué",
                tasks = waiting,
                onTaskChecked = onTaskChecked,
                onTaskWaiting = onTaskWaiting,
                onTaskDelete = onTaskDelete,
                emptyMessage = "Aucune tâche en attente."
            )
        }
    }
}

@Composable
private fun TaskSection(
    title: String,
    tasks: List<TaskEntity>,
    onTaskChecked: (TaskEntity, Boolean) -> Unit,
    onTaskWaiting: (TaskEntity) -> Unit,
    onTaskDelete: (TaskEntity) -> Unit,
    emptyMessage: String
) {
    Spacer(Modifier.height(14.dp))
    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    if (tasks.isEmpty()) {
        EmptyState(emptyMessage)
    } else {
        tasks.forEach { task ->
            TaskCard(
                task = task,
                onCheckedChange = { checked -> onTaskChecked(task, checked) },
                onWaiting = { onTaskWaiting(task) },
                onDelete = { onTaskDelete(task) }
            )
        }
    }
}

private fun buildHardDayList(tasks: List<TaskEntity>): List<TaskEntity> {
    fun firstFrom(categories: List<FocusCategory>) = tasks.firstOrNull { task -> categories.any { it.key == task.categoryKey } }
    val work = firstFrom(listOf(FocusCategory.WORK, FocusCategory.UNIVERSITY))
    val family = firstFrom(listOf(FocusCategory.FAMILY))
    val personal = firstFrom(
        listOf(
            FocusCategory.HEALTH,
            FocusCategory.IMMIGRATION,
            FocusCategory.FINANCES,
            FocusCategory.SPIRITUALITY,
            FocusCategory.FRENCH,
            FocusCategory.HOME,
            FocusCategory.BRAIN_DUMP
        )
    )
    return listOfNotNull(work, family, personal).distinctBy { it.id }.take(3)
}
