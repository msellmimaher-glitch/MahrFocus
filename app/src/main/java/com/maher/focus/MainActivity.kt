package com.maher.focus

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maher.focus.notifications.NotificationScheduler
import com.maher.focus.ui.navigation.MaherFocusNavHost
import com.maher.focus.ui.theme.MaherFocusTheme

class MainActivity : ComponentActivity() {
    private val viewModel: FocusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationScheduler.createNotificationChannel(this)
        NotificationScheduler.scheduleAllEnabledReminders(this)

        setContent {
            MaherFocusTheme {
                val tasks by viewModel.tasks.collectAsStateWithLifecycle()
                val hardDayMode by viewModel.hardDayMode.collectAsStateWithLifecycle()
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                MaherFocusNavHost(
                    viewModel = viewModel,
                    tasks = tasks,
                    hardDayMode = hardDayMode
                )
            }
        }
    }
}
