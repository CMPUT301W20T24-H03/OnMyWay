package com.CMPUT301W20T24.OnMyWay;

import com.robotium.solo.Solo;

import android.app.Activity;
import android.location.Location;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests the UI of Rider Cost Activity
 * @author Payas
 */
public class RiderCostActivityTest
{
    private Solo solo;

    @Rule
    public ActivityTestRule<RiderCost> rule = new ActivityTestRule<>(RiderCost.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * checks if a price estimate was calculated and if the
     * if the estimate is accurate according to the locations
     * @author Payas
     */
    @Test
    public void checkPrice(){
        solo.assertCurrentActivity("Wrong Activity!", RiderCost.class);
        // check if there is a price estimate given and if it is correct
        // for now, test with same hardcoded values as used in the Rider Cost activity
        Location startLocation = new Location("startLocation");
        Location endLocation = new Location("endLocation");
        startLocation.setLatitude(53.523218899999996);
        startLocation.setLongitude(-113.5263186);
        endLocation.setLatitude(53.5411916);
        endLocation.setLongitude(-113.29573549999999);
        // calculate mock cost
        float distance = startLocation.distanceTo(endLocation)/600 ;
        String cost = String.format("$%.2f", distance);
        TextView priceEstimate = (TextView) solo.getView(R.id.priceEstimate);
        // Validate the text in the TextView
        solo.sleep(2000);
        assertEquals(cost, priceEstimate.getText());
    }
    /**
     * checks if editing the price works and if the new value is displayed
     * in the price estimate field
     * @author Payas
     */
    @Test
    public void checkEditPrice(){
        solo.assertCurrentActivity("Wrong Activity!", RiderCost.class);
        // check if the edit price button actually edits the text view of price estimate
        solo.sleep(2000);
        Button editButton = (Button) solo.getView(
                R.id.edit_price_button);
        solo.clickOnView(editButton);
        solo.enterText((EditText) solo.getView(R.id.editText_price),"12");
        Button okButton = (Button) solo.getView(
                R.id.ok_button);
        solo.clickOnView(okButton);
        TextView priceEstimate = (TextView) solo.getView(R.id.priceEstimate);
        assertEquals(priceEstimate.getText().toString(), "$12");
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
