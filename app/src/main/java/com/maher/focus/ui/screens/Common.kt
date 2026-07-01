package com.maher.focus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maher.focus.data.FocusCategory
import com.maher.focus.data.RoutineFrequency
import com.maher.focus.data.TaskEntity
import com.maher.focus.data.TaskPriority
import com.maher.focus.data.TaskStatus
import java.time.LocalDate

@Composable
fun ScreenTitle(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        if (!subtitle.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun CalmCard(modifier: Modifier = Modifier, content: @Composable Column.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        content = { Column(Modifier.padding(16.dp), content = content) }
    )
}

@Composable
fun TaskCard(
    task: TaskEntity,
    onCheckedChange: (Boolean) -> Unit,
    onWaiting: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    val category = FocusCategory.fromKey(task.categoryKey)
    val priority = TaskPriority.fromKey(task.priorityKey)
    val status = TaskStatus.fromKey(task.statusKey)
    val isDone = status == TaskStatus.DONE

    CalmCard {
        Row(verticalAlignment = Alignment.Top) {
            Checkbox(checked = isDone, onCheckedChange = onCheckedChange)
            Column(Modifier.weight(1f)) {
                Text(task.title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("${category.label} • ${priority.label}", style = MaterialTheme.typography.bodySmall)
                if (task.dueDateEpochDay != null) {
                    Text("Date limite : ${LocalDate.ofEpochDay(task.dueDateEpochDay)}", style = MaterialTheme.typography.bodySmall)
                }
                if (task.note.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(task.note, style = MaterialTheme.typography.bodyMedium)
                }
                if (task.isRecurring) {
                    Spacer(Modifier.height(4.dp))
                    Text("Récurrente", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        if (onWaiting != null || onDelete != null) {
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (onWaiting != null) OutlinedButton(onClick = onWaiting) { Text("En attente") }
                if (onDelete != null) OutlinedButton(onClick = onDelete) { Text("Supprimer") }
            }
        }
    }
}

@Composable
fun EmptyState(text: String) {
    CalmCard {
        Text(text, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun CategoryDropdown(selected: FocusCategory, onSelected: (FocusCategory) -> Unit) {
    SimpleDropdown(
        label = "Catégorie",
        selectedLabel = selected.label,
        options = FocusCategory.entries,
        optionLabel = { it.label },
        onSelected = onSelected
    )
}

@Composable
fun PriorityDropdown(selected: TaskPriority, onSelected: (TaskPriority) -> Unit) {
    SimpleDropdown(
        label = "Priorité",
        selectedLabel = selected.label,
        options = TaskPriority.entries,
        optionLabel = { it.label },
        onSelected = onSelected
    )
}

@Composable
fun RoutineFrequencyDropdown(selected: RoutineFrequency, onSelected: (RoutineFrequency) -> Unit) {
    SimpleDropdown(
        label = "Récurrence",
        selectedLabel = selected.label,
        options = RoutineFrequency.entries,
        optionLabel = { it.label },
        onSelected = onSelected
    )
}

@Composable
fun <T> SimpleDropdown(
    label: String,
    selectedLabel: String,
    options: List<T>,
    optionLabel: (T) -> String,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedLabel)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
