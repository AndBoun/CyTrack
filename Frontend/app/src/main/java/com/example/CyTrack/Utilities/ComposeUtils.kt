package com.example.CyTrack.Utilities

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.CyTrack.R


/**
 * Utility class for Compose-related functions.
 */
class ComposeUtils {

    /**
     * Companion object to hold static methods and properties.
     */
    companion object {
        /**
         * Returns a custom FontFamily using Google Fonts.
         *
         * @param fontName The name of the font.
         * @param fontWeight The weight of the font.
         * @param fontStyle The style of the font.
         * @return A FontFamily object with the specified font properties.
         */
        fun getCustomFontFamily(
            fontName: String,
            fontWeight: FontWeight,
            fontStyle: FontStyle
        ): FontFamily {
            val provider = GoogleFont.Provider(
                providerAuthority = "com.google.android.gms.fonts",
                providerPackage = "com.google.android.gms",
                certificates = R.array.com_google_android_gms_fonts_certs
            )
            val googleFont = GoogleFont(fontName)

            return FontFamily(
                Font(
                    googleFont = googleFont,
                    fontProvider = provider,
                    weight = fontWeight,
                    style = fontStyle
                )
            )
        }

        fun bitmapToPainter(bitmap: Bitmap): Painter {
            Log.d("ComposeUtils", "bitmapToPainter: $bitmap")
            return BitmapPainter(bitmap.asImageBitmap())
        }
    }
}