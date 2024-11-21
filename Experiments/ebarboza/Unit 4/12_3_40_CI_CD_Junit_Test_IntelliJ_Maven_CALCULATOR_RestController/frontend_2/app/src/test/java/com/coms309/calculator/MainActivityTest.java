package com.coms309.calculator;

import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

public class MainActivityTest {

//    @RunWith(AndroidJUnit4.class)
    @RunWith(RobolectricTestRunner.class)
    @Config(sdk = 34) // Set this to match your compileSdkVersion
    private MainActivity mainActivity;

//    @Before
//    public void setUp() {
//        mainActivity = new MainActivity();
//    }
//
//    @Test
//    public void testAdd() {
//        double result = mainActivity.add(2, 3);
//        assertEquals(5.0, result, 0.0);
//    }
//
//    @Test
//    public void testMultiply() {
//        double result = mainActivity.multiply(2, 3);
//        assertEquals(6.0, result, 0.0);
//    }

    @Before
    public void setUp() {
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    @Test
    public void testAddition() {
        // Simulate UI interactions or directly call methods to verify behavior
        mainActivity.operateNumbers("add");
        TextView resultText = mainActivity.findViewById(R.id.result_text);
        assertEquals("Result: 5.0", resultText.getText().toString());
    }


}
