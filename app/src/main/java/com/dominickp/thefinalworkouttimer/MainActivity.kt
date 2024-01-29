//
package com.dominickp.thefinalworkouttimer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominickp.thefinalworkouttimer.ui.theme.TheFinalWorkoutTimerTheme
import kotlinx.coroutines.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheFinalWorkoutTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkoutTimerApp() // Use your custom Composable here
                }
            }
        }
    }

    class WorkoutViewModel : ViewModel() {
        var workoutTime by mutableStateOf(0)
        var restTime by mutableStateOf(0)
        var numIntervals by mutableStateOf(0)
        var currentInterval by mutableStateOf(0)
        var timerDisplay by mutableStateOf("Workout Timer")
        var backgroundColor by mutableStateOf(Color.White)
        private var timerJob: Job? = null

        fun startTimer() {
            timerJob = viewModelScope.launch {
                for (interval in 1..numIntervals) {
                    currentInterval = interval
                    // Start workout phase
                    backgroundColor = Color.Green
                    for (time in workoutTime downTo 1) {
                        timerDisplay = "Work: $time"
                        delay(1000)
                    }
                    // Play sound for rest phase (implement sound logic here)

                    // Start rest phase
                    backgroundColor = Color.Blue
                    for (time in restTime downTo 1) {
                        timerDisplay = "Rest: $time"
                        delay(1000)
                    }
                    // Play sound for next interval (implement sound logic here)
                }
                // All intervals completed
                timerDisplay = "DONE!!!"
                backgroundColor = Color.White
                // Play sound for completion (implement sound logic here)
            }
        }

        fun stopTimer() {
            timerJob?.cancel()
            timerDisplay = "Workout Timer"
            backgroundColor = Color.White
            currentInterval = 0
        }
    }

    @Composable
    fun WorkoutTimerApp(viewModel: WorkoutViewModel = viewModel()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(viewModel.backgroundColor)
        ) {
            Text(text = viewModel.timerDisplay, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))
            InputFields(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Interval: ${viewModel.currentInterval}")
            Spacer(modifier = Modifier.height(16.dp))
            ControlButtons(viewModel)
        }
    }

    @Composable
    fun InputFields(viewModel: WorkoutViewModel) {
        TextField(
            value = viewModel.workoutTime.toString(),
            onValueChange = { viewModel.workoutTime = it.toIntOrNull() ?: 0 },
            label = { Text("Work Time (seconds)") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = viewModel.restTime.toString(),
            onValueChange = { viewModel.restTime = it.toIntOrNull() ?: 0 },
            label = { Text("Rest Time (seconds)") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = viewModel.numIntervals.toString(),
            onValueChange = { viewModel.numIntervals = it.toIntOrNull() ?: 0 },
            label = { Text("Number of Intervals") }
        )
    }

    @Composable
    fun ControlButtons(viewModel: WorkoutViewModel) {
        Row {
            Button(onClick = { viewModel.startTimer() }) {
                Text("Start")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.stopTimer() }) {
                Text("Stop")
            }
        }
    }
}

//fun main() {
//    // Start your Compose application here
//    // This requires an Android environment to run
//}

