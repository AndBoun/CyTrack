package com.example.CyTrack.Utilities;

import android.app.Activity;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Utility class for status bar related operations.
 */
public class StatusBarUtil {

    /**
     * Sets the status bar color and ensures the status bar icons are visible.
     *
     * @param activity   The activity where the status bar color will be set.
     * @param colorResId The resource ID of the color to set the status bar.
     */
    public static void setStatusBarColor(Activity activity, int colorResId) {
        // Change the status bar color
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(colorResId));

        // Ensure the status bar icons are visible
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(activity.getWindow(), activity.getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);
    }
}
