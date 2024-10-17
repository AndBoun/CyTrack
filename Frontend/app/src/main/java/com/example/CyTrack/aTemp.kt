package com.example.CyTrack

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class aTemp : ComponentActivity() {

    private val workoutList = mutableStateListOf<WorkoutObject>()



    fun addWorkout(composeView: ComposeView, workout: WorkoutObject) {
        workoutList.add(workout)
        composeView.setContent {
            WorkoutList(workoutList, onCardClickable = {workout -> showEditDeleteDialog(composeView, workout)})
        }
    }


    private fun showEditDeleteDialog(composeView: ComposeView, workout: WorkoutObject) {
        composeView.setContent {
            WorkoutList(workoutList, onCardClickable = {workout -> showEditDeleteDialog(composeView, workout)})
            EditDeleteDialog(
                workout = workout,
                onDismiss = { /* Handle dismiss */ },
                onEdit = { /* Handle edit */ },
                onDelete = { workoutList.remove(workout) }
            )
        }
    }
}

@Composable
fun WorkoutCard(workout: WorkoutObject, onDelete: (WorkoutObject) -> Unit, onClick: (WorkoutObject) -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp,
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 15.dp)
            .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp))
            .fillMaxWidth() // Make the Surface fill the width
            .clickable(onClick = { onClick(workout) }) // Added clickable modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) { // Added padding to the Column
            Text(
                text = workout.exerciseType,
                color = Color(0xFFC8102E),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = workout.time)
                Spacer(modifier = Modifier.width(20.dp))

                Text(text = workout.caloriesBurned)
                Spacer(modifier = Modifier.width(20.dp))

                Text(text = workout.duration)
            }
        }
    }
}

@Composable
fun EditDeleteDialog(
    workout: WorkoutObject,
    onDismiss: () -> Unit,
    onEdit: (WorkoutObject) -> Unit,
    onDelete: (WorkoutObject) -> Unit
) {
    var exerciseType by remember { mutableStateOf(workout.exerciseType) }
    var duration by remember { mutableStateOf(workout.duration) }
    var calories by remember { mutableStateOf(workout.caloriesBurned) }
    var time by remember { mutableStateOf(workout.time) }

    AlertDialog(

        onDismissRequest = onDismiss,
        title = { Text(text = "Modify Workout") },
        text = {
            Column {
                TextField(
                    value = exerciseType,
                    onValueChange = { exerciseType = it },
                    label = { Text("Exercise Type") }
                )
                TextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration") }
                )
                TextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calories Burned") }
                )
                TextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
//                onEdit(workout.copy(
//                    exerciseType = exerciseType,
//                    duration = duration,
//                    caloriesBurned = calories,
//                    time = time
//                )
//                )
            }) {
                Text("Edit")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDelete(workout) }) {
                Text("Delete")
            }
        }
    )
}

@Composable
fun WorkoutList(workoutList: MutableList<WorkoutObject>, onCardClickable: (WorkoutObject) -> Unit) {
    LazyColumn {
        items(workoutList) { workout ->
            WorkoutCard(workout, onDelete = { workoutList.remove(workout) }, onClick = onCardClickable)
        }
    }
}

@Preview
@Composable
fun PreviewWorkoutCard() {
    Surface(shadowElevation = 0.dp) {
        val workoutList = ArrayList<WorkoutObject>()
        workoutList.add(WorkoutObject("Running", "30 minutes", "300 calories", "10/10/24", 5))
        workoutList.add(WorkoutObject("Running", "30 minutes", "300 calories", "10/10/24", 5))
        workoutList.add(WorkoutObject("Running", "30 minutes", "300 calories", "10/10/24", 5))
        WorkoutList(workoutList, onCardClickable = {})
    }
}

@Preview
@Composable
fun PreviewEditDeleteDialog() {
    EditDeleteDialog(
        workout = WorkoutObject("Running", "30 minutes", "300 calories", "10/10/24", 5),
        onDismiss = {},
        onEdit = {},
        onDelete = {}
    )
}

