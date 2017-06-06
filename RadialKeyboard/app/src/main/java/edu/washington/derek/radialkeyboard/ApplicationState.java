package edu.washington.derek.radialkeyboard;

import android.app.Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    // boolean showing whether shift is on
    private boolean shiftOn;

    // This value is the index for the currently displayed layout
    // 0 = alphabet
    // 1 = symbols
    // 2 = numbers
    private int currentLayout;

    // Selected User

    // Holds the inputs in order
    Queue<String> outputs;

    // Current Sentence
    private StringBuffer sb;

    // Current Letter
    private String currentCharacter;

    private ApplicationState() {
        // Check if the instance has been initialized, if it already exists do nothing
        if (getInstance() == null) {
            // Set the layout display to be the default (1 is the english Alphabet)
            currentLayout = 0;

            // set current letter to empty string
            currentCharacter = "";

            // Initialize the String Buffer
            sb = new StringBuffer();

            // Default shift to off
            shiftOn = false;

            // Initialize the output queue
            outputs = new LinkedList<String>();

        }
    }

    public void writeFile() {
        String filename = "output.xml";
        String starter = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n";
        String textText = "<TextTest version=\"2.7.2\" trials=\"45\" ticks=\"636317330047081832\" seconds=\"63631733004.71\" date=\"Tuesday, May 30, 2017 9:23:24 AM\">\n";
        File dir = new File("/sdcard/My Documents");
        File file = new File(dir, filename);
        try {
            FileOutputStream f = new FileOutputStream(file);
            f.write(starter.getBytes());
            f.write(textText.getBytes());
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Return the current layout index
    public int getCurrentLayout() {
        return currentLayout;
    }

    // Set the current layout index to the passed in value
    // WE need to make the swipe thing
    public void setCurrentLayout(int index) {
        currentLayout = index;
    }

    // Return the shift value
    public boolean getShiftStatus() {
        return shiftOn;
    }

    // Turn shift off or on
    public void toggleShift() {
        shiftOn = !shiftOn;
    }

    // Add a character from the string buffer
    public void addCharacter() {
        sb.append(currentCharacter);
    }

    public void setCurrentCharacter(String current) {
        currentCharacter = current;
    }

    public String getCurrentCharacter() { return currentCharacter; }

    // Remove a character from the string buffer
    public void deleteCharacter() {
        sb.setLength(Math.max(sb.length() - 1, 0));
    }

    // Submits the current string as the final sentence
    public void submitString() {
        outputs.add(sb.toString());
        currentCharacter = "";
        sb.setLength(0);
    }

    // Get the sentence made by the string buffer
    public String getSentence() {
        return sb.toString();
    }
}
