package edu.washington.derek.radialkeyboard;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;

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
    private int currentLayout;

    // Holds the inputs in order
    Queue<String> outputs;

    // boolean showing if the input is on
    // This will be always on for symbols and numbers but if defauled to off during text
    private boolean creatingString;

    // Current Sentence
    private StringBuffer sb;

    private ApplicationState() {
        // Check if the instance has been initialized, if it already exists do nothing
        if (getInstance() == null) {
            // Set the layout display to be the default (1 is the english Alphabet)
            // 0 = alphabet
            // 1 = symbols
            // 2 = numbers
            currentLayout = 2;

            // Initialize the String Buffer
            sb = new StringBuffer();

            // Default shift to off
            shiftOn = false;

            // Import the layout schemes from file into a JSON Array
            creatingString = false;

            // Initialize the output queue
            outputs = new LinkedList<String>();

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

    public boolean getShiftStatus() {
        return shiftOn;
    }

    // Turn shift off or on
    public void toggleShift() {
        shiftOn = !shiftOn;
    }

    // Add a character from the string buffer
    public void addCharacter(char inputChar) {
        sb.append(inputChar);
    }

    // Remove a character from the string buffer
    public void deleteCharacter() {
        sb.setLength(Math.max(sb.length() - 1, 0));
    }

    // Submits the current string as the final sentence
    public void submitString() {
        outputs.add(sb.toString());
        sb.setLength(0);
    }

    // Get the sentence made by the string buffer
    public String getSentence() {
        return sb.toString();
    }
}
