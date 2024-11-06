package com.example.CyTrack.Utilities;

import android.app.Activity;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class StatusBarUtil {
    public static void setStatusBarColor(Activity activity, int colorResId) {
        // Change the status bar color
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(colorResId));

        // Ensure the status bar icons are visible
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(activity.getWindow(), activity.getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);
    }
}
