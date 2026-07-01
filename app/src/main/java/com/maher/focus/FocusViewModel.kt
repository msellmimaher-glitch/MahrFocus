package com.maher.focus

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maher.focus.data.AdminDocumentEntity
import com.maher.focus.data.AdminDocumentStatus
import com.maher.focus.data.AppDatabase
import com.maher.focus.data.BrainDumpItemEntity
import com.maher.focus.data.EveningReviewEntity
import com.maher.focus.data.FocusCategory
import com.maher.focus.data.GoalEntity
import com.maher.focus.data.GoalStepEntity
import com.maher.focus.data.RoutineEntity
import com.maher.focus.data.RoutineFrequency
import com.maher.focus.data.TaskEntity
import com.maher.focus.data.TaskPriority
import com.maher.focus.data.TaskStatus
import com.maher.focus.repository.FocusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class FocusViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FocusRepository(AppDatabase.getDatabase(application).focusDao())

    val categories = repository.categories.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val children = repository.children.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val tasks = repository.tasks.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val brainDumpItems = repository.brainDumpItems.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val goalsWithSteps = repository.goalsWithSteps.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val routines = repository.routines.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val eveningReviews = repository.eveningReviews.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val adminDocuments = repository.adminDocuments.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _hardDayMode = MutableStateFlow(false)
    val hardDayMode: StateFlow<Boolean> = _hardDayMode

    init {
        viewModelScope.launch { repository.seedIfNeeded() }
    }

    fun setHardDayMode(enabled: Boolean) {
        _hardDayMode.value = enabled
    }

    fun addTask(
        title: String,
        category: FocusCategory,
        priority: TaskPriority,
        dueDateText: String?,
        note: String,
        isRecurring: Boolean,
        routineFrequency: RoutineFrequency?,
        childId: Long? = null
    ) {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return
        viewModelScope.launch {
            repository.addTask(
                TaskEntity(
                    title = cleanTitle,
                    categoryKey = category.key,
                    priorityKey = priority.key,
                    dueDateEpochDay = parseDateToEpochDay(dueDateText),
                    note = note.trim(),
                    isRecurring = isRecurring,
                    routineFrequencyKey = routineFrequency?.key,
                    childId = childId
                )
            )
        }
    }

    fun completeTask(task: TaskEntity, completed: Boolean) {
        viewModelScope.launch {
            repository.updateTask(
                task.copy(
                    statusKey = if (completed) TaskStatus.DONE.key else TaskStatus.ACTIVE.key,
                    completedAtMillis = if (completed) System.currentTimeMillis() else null
                )
            )
        }
    }

    fun moveTaskToWaiting(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task.copy(statusKey = TaskStatus.WAITING.key, priorityKey = TaskPriority.WAITING.key))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch { repository.deleteTask(task.id) }
    }

    fun addBrainDump(text: String) {
        val cleanText = text.trim()
        if (cleanText.isBlank()) return
        viewModelScope.launch { repository.addBrainDumpItem(cleanText) }
    }

    fun convertBrainDumpToTask(item: BrainDumpItemEntity) {
        if (item.convertedTaskId != null) return
        viewModelScope.launch {
            val taskId = repository.addTask(
                TaskEntity(
                    title = item.text.take(80),
                    categoryKey = FocusCategory.BRAIN_DUMP.key,
                    priorityKey = TaskPriority.WHEN_ENERGY.key,
                    note = item.text
                )
            )
            repository.updateBrainDumpItem(item.copy(convertedTaskId = taskId))
        }
    }

    fun deleteBrainDump(item: BrainDumpItemEntity) {
        viewModelScope.launch { repository.deleteBrainDumpItem(item.id) }
    }

    fun addGoal(title: String, domain: FocusCategory) {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return
        viewModelScope.launch { repository.addGoal(GoalEntity(title = cleanTitle, domainKey = domain.key)) }
    }

    fun addGoalStep(goalId: Long, title: String) {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return
        viewModelScope.launch { repository.addGoalStep(GoalStepEntity(goalId = goalId, title = cleanTitle)) }
    }

    fun setGoalStepDone(step: GoalStepEntity, done: Boolean) {
        viewModelScope.launch { repository.updateGoalStep(step.copy(isDone = done)) }
    }

    fun addRoutine(title: String, category: FocusCategory, priority: TaskPriority, frequency: RoutineFrequency) {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return
        viewModelScope.launch {
            repository.addRoutine(
                RoutineEntity(
                    title = cleanTitle,
                    categoryKey = category.key,
                    priorityKey = priority.key,
                    frequencyKey = frequency.key
                )
            )
        }
    }

    fun toggleRoutine(routine: RoutineEntity) {
        viewModelScope.launch { repository.updateRoutine(routine.copy(isActive = !routine.isActive)) }
    }

    fun saveEveningReview(completed: String, blocked: String, tomorrowPriority: String) {
        viewModelScope.launch {
            repository.saveEveningReview(
                EveningReviewEntity(
                    dateEpochDay = LocalDate.now().toEpochDay(),
                    completedText = completed.trim(),
                    blockedText = blocked.trim(),
                    tomorrowPriorityText = tomorrowPriority.trim()
                )
            )
        }
    }

    fun addAdminDocument(title: String, status: AdminDocumentStatus, dueDateText: String?, note: String) {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return
        viewModelScope.launch {
            repository.addAdminDocument(
                AdminDocumentEntity(
                    title = cleanTitle,
                    statusKey = status.key,
                    dueDateEpochDay = parseDateToEpochDay(dueDateText),
                    note = note.trim()
                )
            )
        }
    }

    fun updateAdminDocumentStatus(document: AdminDocumentEntity, status: AdminDocumentStatus) {
        viewModelScope.launch { repository.updateAdminDocument(document.copy(statusKey = status.key)) }
    }

    private fun parseDateToEpochDay(text: String?): Long? {
        val clean = text?.trim().orEmpty()
        if (clean.isBlank()) return null
        return runCatching { LocalDate.parse(clean).toEpochDay() }.getOrNull()
    }
}
