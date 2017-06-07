package edu.washington.derek.radialkeyboard;

import android.app.Application;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * Created by derek on 5/27/17.
 */

class ApplicationState extends Application {
    private static final ApplicationState ourInstance = new ApplicationState();

    private static final int TRIALS = 45;

    // Set up constants for the ticks
    private static final long TICKS_AT_EPOCH = 621355968000000000L;
    private static final long TICKS_PER_MILLISECOND = 10000;

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

    // String[] for potential words
    private String[] phrases;

    // Holds the inputs in order
    Queue<String> outputs;

    // Current Sentence
    private StringBuffer sb;

    // Starting phrase for the testing
    private int startingPhrase;

    // current phrase index
    private int currentPhraseIndex;

    // current trial entries counter
    private Queue<String> entries;

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

            // Initialize the phrases
            phrases = new String[500];

            // Starting phrase
            startingPhrase = new Random().nextInt(455 - 0 + 1);

            // Current Phrase
            currentPhraseIndex = 0;

            // set entries to zero
            entries = new LinkedList<String>();
        }
    }

    public Queue<String> getEntries() {
        return entries;
    }

    public void addEntry(char value) {
        // Get date in ticks
        long tick = System.currentTimeMillis()*TICKS_PER_MILLISECOND + TICKS_AT_EPOCH;
        // Get date in seconds
        long seconds = tick/ 100000;
        String sec = Long.toString(seconds / 100) + "." + (seconds % 100);
        entries.add("<Entry char=\"" + value + "\" value=\"" + Character.getNumericValue(value) + "\" ticks=\"" + tick + "\" seconds=\"" + sec + "\" />");
    }

    public void incrementCurrentPhrase() {
        ++currentPhraseIndex;
    }

    public int getCurrentPhraseIndex() {
        return currentPhraseIndex;
    }

    // Set the phrases with the input stream from the file
    public void setPhrases(Set<String> input) {
        int index = 0;
        for (String item: input) {
            phrases[index] = item;
            ++index;
        }
    }

    // Get the list of phrases
    public String getCurrentPhrase() {
        return phrases[currentPhraseIndex + startingPhrase];
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

    public Queue<String> allTrials() {
        return outputs;
    }

    // Add this to write to file queue
    public void enqueueString(String x) {
        outputs.add(x);
    }

    // Get the sentence made by the string buffer
    public String getSentence() {
        return sb.toString();
    }
}
