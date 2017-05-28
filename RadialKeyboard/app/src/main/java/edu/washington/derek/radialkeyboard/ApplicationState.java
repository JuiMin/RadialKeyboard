package edu.washington.derek.radialkeyboard;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by derek on 5/27/17.
 */

class ApplicationState extends Application {
    private static final ApplicationState ourInstance = new ApplicationState();

    static ApplicationState getInstance() {
        return ourInstance;
    }

    // Fields for the Application State
    private JSONArray keyboardLayouts;

    // boolean showing whether shift is on
    private boolean shiftOn;

    // This value is the index for the currently displayed layout
    // 0 =
    private int currentLayout;

    private ApplicationState() {
        // Check if the instance has been initialized, if it already exists do nothing
        if (getInstance() == null) {
            // Set the layout display to be the default (1 is the english Alphabet)
            currentLayout = 1;

            // Default shift to off
            shiftOn = false;

            // Import the layout schemes from file into a JSON Array


        }
    }

    // Return the current layout index
    public int getCurrentLayout() {
        return currentLayout;
    }

    // Set the current layout index to the passed in value
    public void setCurrentLayout(int index) {
        currentLayout = index;
    }

    public boolean getShiftOn() {
        return shiftOn;
    }

    // Turn shift off or on
    public void toggleShift() {
        shiftOn = !shiftOn;
    }
}
