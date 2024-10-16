package com.example.CyTrack

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class aTemp : ComponentActivity() {

    fun setComposeViewContent(context: Context, composeView: ComposeView) {
        composeView.setContent {
            val workoutList = ArrayList<WorkoutObject>()
            workoutList.add(WorkoutObject("Running", "30 minutes", "300 calories", "10/10/24", 5))
            workoutList.add(WorkoutObject("Running", "30 minutes", "300 calories", "10/10/24", 5))
            workoutList.add(WorkoutObject("Running", "30 minutes", "300 calories", "10/10/24", 5))
            WorkoutList(workoutList)
        }
    }
}

@Composable
fun WorkoutCard(workout: WorkoutObject) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp,
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 15.dp)
            .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp))
            .fillMaxWidth() // Make the Surface fill the width
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit or Delete Workout") },
        text = { Text(text = "Would you like to edit or delete this workout?") },
        confirmButton = {
            Button(onClick = { onEdit(workout) }) {
                Text("Edit")
            }
        },
        dismissButton = {
            Button(onClick = { onDelete(workout) }) {
                Text("Delete")
            }
        }
    )
}

@Composable
fun WorkoutList(workoutList: ArrayList<WorkoutObject>) {
    LazyColumn {
        items(workoutList) { workout ->
            WorkoutCard(workout)
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
        WorkoutList(workoutList)
    }


}

