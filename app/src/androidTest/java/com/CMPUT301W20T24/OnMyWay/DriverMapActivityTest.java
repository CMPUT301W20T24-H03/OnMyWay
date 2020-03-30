package com.CMPUT301W20T24.OnMyWay;

import com.google.android.gms.maps.MapView;
import com.robotium.solo.Solo;



import android.app.Activity;
import android.location.Location;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests the UI of Rider Cost Activity
 * @author Payas, Neel
 */
public class DriverMapActivityTest
{

    private Solo solo;

    @Rule
    public ActivityTestRule<DriverMapActivity> rule = new ActivityTestRule<>(DriverMapActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();

    }

    @Test
    public void assertActivity(){
        solo.assertCurrentActivity("Wrong Activity!", DriverMapActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
