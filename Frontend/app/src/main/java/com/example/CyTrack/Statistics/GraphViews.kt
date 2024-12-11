package com.example.CyTrack.Statistics

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.CyTrack.R
import com.example.CyTrack.Utilities.User
import com.example.CyTrack.Workouts.WorkoutObject
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat


class GraphViews {
    /**
     * Companion object to hold static methods and properties.
     */
    companion object {
        @JvmStatic
        fun showCaloriesGraph(
            List: MutableList<MealEntry>,
            context: Activity
        ) {
            // Inflate the dialog layout
            val inflater = LayoutInflater.from(context)
            val dialogView: View = inflater.inflate(R.layout.graphsample, null)
            // on below line we are initializing our graph view.
            // on below line we are adding data to our graph view.
            var graphView = dialogView.findViewById<GraphView>(R.id.idGraphView)
            graphView.viewport.isScalable = true
            graphView.viewport.isScrollable = true
            graphView.viewport.isXAxisBoundsManual = true
            graphView.viewport.isYAxisBoundsManual = true


            var dataPoints = List.mapIndexed { index, MealEntry ->
                DataPoint(index.toDouble(), MealEntry.calories.toDouble())
            }.toTypedArray()

            val series = LineGraphSeries(dataPoints)

            graphView.viewport.setMinX(series.lowestValueX)  // optional offset for padding
            graphView.viewport.setMaxX(series.highestValueX + 1)
            graphView.viewport.setMinY(series.lowestValueY - 1)
            graphView.viewport.setMaxY(series.highestValueY + 1)

            val color = Color.Red
            series.color = color.toArgb()

            // on below line we are setting
            // our title text size.
            graphView.setTitleTextSize(18f)

            // on below line we are adding
            // data series to our graph view.
            graphView.addSeries(series)

            // Set up the X and Y axis labels using GridLabelRenderer
            val gridLabelRenderer = graphView.gridLabelRenderer

            // Set the title of the X axis
            gridLabelRenderer.verticalAxisTitle = "Calories"

            // Set the title of the Y axis
            gridLabelRenderer.horizontalAxisTitle  = "Index"

            // Optionally, configure other settings such as label text size and padding
            gridLabelRenderer.isHorizontalLabelsVisible = true
            gridLabelRenderer.isVerticalLabelsVisible = true

            // Create the AlertDialog
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
                .setTitle("Calories Graph")
                .setPositiveButton(
                    "Close"
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                .create()
                .show()
        }

        @JvmStatic
        fun showCaloriesBurnedGraph(
            List: MutableList<WorkoutObject>,
            context: Activity
        ) {
            // Inflate the dialog layout
            val inflater = LayoutInflater.from(context)
            val dialogView: View = inflater.inflate(R.layout.graphsample, null)
            // on below line we are initializing our graph view.
            // on below line we are adding data to our graph view.
            var graphView = dialogView.findViewById<GraphView>(R.id.idGraphView)
            graphView.viewport.isScalable = true
            graphView.viewport.isScrollable = true
            graphView.viewport.isXAxisBoundsManual = true
            graphView.viewport.isYAxisBoundsManual = true


            var dataPoints = List.mapIndexed { index, WorkoutObject ->
                DataPoint(index.toDouble(), WorkoutObject.caloriesBurned.toDouble())
            }.toTypedArray()

            val series = LineGraphSeries(dataPoints)

            graphView.viewport.setMinX(series.lowestValueX)  // optional offset for padding
            graphView.viewport.setMaxX(series.highestValueX + 1)
            graphView.viewport.setMinY(series.lowestValueY - 1)
            graphView.viewport.setMaxY(series.highestValueY + 1)

            val color = Color.Red
            series.color = color.toArgb()

            // on below line we are setting
            // our title text size.
            graphView.setTitleTextSize(18f)

            // on below line we are adding
            // data series to our graph view.
            graphView.addSeries(series)

            // Set up the X and Y axis labels using GridLabelRenderer
            val gridLabelRenderer = graphView.gridLabelRenderer

            // Set the title of the X axis
            gridLabelRenderer.verticalAxisTitle = "Calories Burned"

            // Set the title of the Y axis
            gridLabelRenderer.horizontalAxisTitle  = "Index"

            // Optionally, configure other settings such as label text size and padding
            gridLabelRenderer.isHorizontalLabelsVisible = true
            gridLabelRenderer.isVerticalLabelsVisible = true

            // Create the AlertDialog
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
                .setTitle("Calories Graph")
                .setPositiveButton(
                    "Close"
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                .create()
                .show()
        }

        @JvmStatic
        fun orderMealsByDate(
            mealList: MutableList<MealEntry>,
        ) {
            mealList.sortBy { it.date }
        }

        @JvmStatic
        fun orderWorkoutsByDate(
            workoutList: MutableList<WorkoutObject>,
        ) {
            workoutList.sortBy { it.date }
        }

    }
}