package edu.washington.derek.radialkeyboard;

import android.app.Application;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "Keyboard Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the Edit Text so that we can update the text field
        final EditText input = (EditText)findViewById(R.id.input_area);

        // Should remove the keyboard without getting rid of the cursor
        input.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });

        // On create, get an instance of the state
        // Extends application should run on application start so this information should be fine
        final ApplicationState state = ApplicationState.getInstance();

        // Set the button listeners for the corner buttons
        // This button is always going to be the shift button
        Button topLeft = (Button)findViewById(R.id.top_left_button);
        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This button should toggle the shift
                state.toggleShift();

                // Update the buttons to reflect that the shift is now on
                buttonUpdate();
            }
        });

        // This button will always be the enter button
        Button topRight = (Button)findViewById(R.id.top_right_button);
        topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enter/Submit the sentence
                state.submitString();
                // The string buffer should have been reset so we can update the edit text
                input.setText(state.getSentence());
                input.setSelection(state.getSentence().length());
            }
        });

        // This button will always be backspace
        Button bottomLeft = (Button)findViewById(R.id.bottom_left_button);
        bottomLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Backspace
                state.deleteCharacter();
                input.setText(state.getSentence());
                input.setSelection(state.getSentence().length());
            }
        });

        // This button will always be spacebar
        Button bottomRight = (Button)findViewById(R.id.bottom_right_button);
        bottomRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add a spacebar to the string buffer
                state.setCurrentCharacter(" ");
                state.addCharacter();
                input.setText(state.getSentence());
                input.setSelection(state.getSentence().length());
            }
        });

        // Update the buttons with the current layout
        buttonUpdate();

        // Set the functionality for each button


        Log.i(TAG, "Keyboard Main Activity successfully created");
    }

    // Load JSON
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("layouts.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    // Update the buttons
    private void buttonUpdate() {
        try {
            // Get an instance of the state
            ApplicationState state = ApplicationState.getInstance();

            // Get reference to the edit text
            EditText input = (EditText)findViewById(R.id.input_area);

            // Load the JSON from file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // If the JSON Loaded Correctly, populate the buttons
            JSONObject layouts = obj.getJSONObject("layouts");

            // Test the state for current layout selection
            String selectedLayout = "";
            int current = state.getCurrentLayout();

            // Get the json value for the layout selection
            if (current == 1) {
                selectedLayout = "symbols";
            } else if (current == 2) {
                selectedLayout = "numbers";
            } else {
                selectedLayout = "alphabet";
            }

            // Rerender the buttons
            setButtons(layouts.getJSONObject(selectedLayout), state.getShiftStatus());
            activateButtons(state.getShiftStatus());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void activateButtons(boolean shiftOn) {
        if (shiftOn) {
            int x = 5;
        }
    }

    // Set the text for the buttons
    private void setButtons(JSONObject layout, boolean shiftOn) throws JSONException {

        // Get Buttons
        Button button_one = (Button)findViewById(R.id.button_one);
        Button button_two = (Button)findViewById(R.id.button_two);
        Button button_three = (Button)findViewById(R.id.button_three);
        Button button_four = (Button)findViewById(R.id.button_four);
        Button button_five = (Button)findViewById(R.id.button_five);
        Button button_six = (Button)findViewById(R.id.button_six);
        Button button_seven = (Button)findViewById(R.id.button_seven);
        Button button_eight = (Button)findViewById(R.id.button_eight);
        Button button_nine = (Button)findViewById(R.id.button_nine);
        Button button_center = (Button)findViewById(R.id.center_button);

        // Set the button text
        if (shiftOn) {
            button_one.setText(layout.getJSONArray("button_one").get(0).toString().toUpperCase());
            button_two.setText(layout.getJSONArray("button_two").get(0).toString().toUpperCase());
            button_three.setText(layout.getJSONArray("button_three").get(0).toString().toUpperCase());
            button_four.setText(layout.getJSONArray("button_four").get(0).toString().toUpperCase());
            button_five.setText(layout.getJSONArray("button_five").get(0).toString().toUpperCase());
            button_six.setText(layout.getJSONArray("button_six").get(0).toString().toUpperCase());
            button_seven.setText(layout.getJSONArray("button_seven").get(0).toString().toUpperCase());
            button_eight.setText(layout.getJSONArray("button_eight").get(0).toString().toUpperCase());
            button_nine.setText(layout.getJSONArray("button_nine").get(0).toString().toUpperCase());
            button_center.setText(layout.getJSONArray("button_center").get(0).toString().toUpperCase());
        } else {
            button_one.setText(layout.getJSONArray("button_one").get(0).toString().toLowerCase());
            button_two.setText(layout.getJSONArray("button_two").get(0).toString().toLowerCase());
            button_three.setText(layout.getJSONArray("button_three").get(0).toString().toLowerCase());
            button_four.setText(layout.getJSONArray("button_four").get(0).toString().toLowerCase());
            button_five.setText(layout.getJSONArray("button_five").get(0).toString().toLowerCase());
            button_six.setText(layout.getJSONArray("button_six").get(0).toString().toLowerCase());
            button_seven.setText(layout.getJSONArray("button_seven").get(0).toString().toLowerCase());
            button_eight.setText(layout.getJSONArray("button_eight").get(0).toString().toLowerCase());
            button_nine.setText(layout.getJSONArray("button_nine").get(0).toString().toLowerCase());
            button_center.setText(layout.getJSONArray("button_center").get(0).toString().toLowerCase());
        }


        // Get the secondary buttons
        TextView button_one_left = (TextView)findViewById(R.id.button_one_left);
        TextView button_one_right = (TextView)findViewById(R.id.button_one_right);
        TextView button_two_left = (TextView)findViewById(R.id.button_two_left);
        TextView button_two_right = (TextView)findViewById(R.id.button_two_right);
        TextView button_three_left = (TextView)findViewById(R.id.button_three_left);
        TextView button_three_right = (TextView)findViewById(R.id.button_three_right);
        TextView button_four_left = (TextView)findViewById(R.id.button_four_left);
        TextView button_four_right = (TextView)findViewById(R.id.button_four_right);
        TextView button_five_left = (TextView)findViewById(R.id.button_five_left);
        TextView button_five_right = (TextView)findViewById(R.id.button_five_right);
        TextView button_six_left = (TextView)findViewById(R.id.button_six_left);
        TextView button_six_right = (TextView)findViewById(R.id.button_six_right);
        TextView button_seven_left = (TextView)findViewById(R.id.button_seven_left);
        TextView button_seven_right = (TextView)findViewById(R.id.button_seven_right);
        TextView button_eight_left = (TextView)findViewById(R.id.button_eight_left);
        TextView button_eight_right = (TextView)findViewById(R.id.button_eight_right);
        TextView button_nine_left = (TextView)findViewById(R.id.button_nine_left);
        TextView button_nine_right = (TextView)findViewById(R.id.button_nine_right);

        // Set secondary button visibility and text
        if ((boolean)layout.get("small_button_visible")) {
            // Set the smaller text buttons to visible and set the text
            button_one_left.setVisibility(View.VISIBLE);
            button_one_right.setVisibility(View.VISIBLE);
            button_two_left.setVisibility(View.VISIBLE);
            button_two_right.setVisibility(View.VISIBLE);
            button_three_left.setVisibility(View.VISIBLE);
            button_three_right.setVisibility(View.VISIBLE);
            button_four_left.setVisibility(View.VISIBLE);
            button_four_right.setVisibility(View.VISIBLE);
            button_five_left.setVisibility(View.VISIBLE);
            button_five_right.setVisibility(View.VISIBLE);
            button_six_left.setVisibility(View.VISIBLE);
            button_six_right.setVisibility(View.VISIBLE);
            button_seven_left.setVisibility(View.VISIBLE);
            button_seven_right.setVisibility(View.VISIBLE);
            button_eight_left.setVisibility(View.VISIBLE);
            button_eight_right.setVisibility(View.VISIBLE);
            button_nine_left.setVisibility(View.VISIBLE);
            button_nine_right.setVisibility(View.VISIBLE);

            // Set the text for the smaller buttons
            if (shiftOn) {
                button_one_left.setText(layout.getJSONArray("button_one").get(1).toString().toUpperCase());
                button_one_right.setText(layout.getJSONArray("button_one").get(2).toString().toUpperCase());
                button_two_left.setText(layout.getJSONArray("button_two").get(1).toString().toUpperCase());
                button_two_right.setText(layout.getJSONArray("button_two").get(2).toString().toUpperCase());
                button_three_left.setText(layout.getJSONArray("button_three").get(1).toString().toUpperCase());
                button_three_right.setText(layout.getJSONArray("button_three").get(2).toString().toUpperCase());
                button_four_left.setText(layout.getJSONArray("button_four").get(1).toString().toUpperCase());
                button_four_right.setText(layout.getJSONArray("button_four").get(2).toString().toUpperCase());
                button_five_left.setText(layout.getJSONArray("button_five").get(1).toString().toUpperCase());
                button_five_right.setText(layout.getJSONArray("button_five").get(2).toString().toUpperCase());
                button_six_left.setText(layout.getJSONArray("button_six").get(1).toString().toUpperCase());
                button_six_right.setText(layout.getJSONArray("button_six").get(2).toString().toUpperCase());
                button_seven_left.setText(layout.getJSONArray("button_seven").get(1).toString().toUpperCase());
                button_seven_right.setText(layout.getJSONArray("button_seven").get(2).toString().toUpperCase());
                button_eight_left.setText(layout.getJSONArray("button_eight").get(1).toString().toUpperCase());
                button_eight_right.setText(layout.getJSONArray("button_eight").get(2).toString().toUpperCase());
                button_nine_left.setText(layout.getJSONArray("button_nine").get(1).toString().toUpperCase());
                button_nine_right.setText(layout.getJSONArray("button_nine").get(2).toString().toUpperCase());
            } else {
                button_one_left.setText(layout.getJSONArray("button_one").get(1).toString());
                button_one_right.setText(layout.getJSONArray("button_one").get(2).toString());
                button_two_left.setText(layout.getJSONArray("button_two").get(1).toString());
                button_two_right.setText(layout.getJSONArray("button_two").get(2).toString());
                button_three_left.setText(layout.getJSONArray("button_three").get(1).toString());
                button_three_right.setText(layout.getJSONArray("button_three").get(2).toString());
                button_four_left.setText(layout.getJSONArray("button_four").get(1).toString());
                button_four_right.setText(layout.getJSONArray("button_four").get(2).toString());
                button_five_left.setText(layout.getJSONArray("button_five").get(1).toString());
                button_five_right.setText(layout.getJSONArray("button_five").get(2).toString());
                button_six_left.setText(layout.getJSONArray("button_six").get(1).toString());
                button_six_right.setText(layout.getJSONArray("button_six").get(2).toString());
                button_seven_left.setText(layout.getJSONArray("button_seven").get(1).toString());
                button_seven_right.setText(layout.getJSONArray("button_seven").get(2).toString());
                button_eight_left.setText(layout.getJSONArray("button_eight").get(1).toString());
                button_eight_right.setText(layout.getJSONArray("button_eight").get(2).toString());
                button_nine_left.setText(layout.getJSONArray("button_nine").get(1).toString());
                button_nine_right.setText(layout.getJSONArray("button_nine").get(2).toString());
            }

        } else {
            // Set the smaller buttons to invisible
            button_one_left.setVisibility(View.GONE);
            button_one_right.setVisibility(View.GONE);
            button_two_left.setVisibility(View.GONE);
            button_two_right.setVisibility(View.GONE);
            button_three_left.setVisibility(View.GONE);
            button_three_right.setVisibility(View.GONE);
            button_four_left.setVisibility(View.GONE);
            button_four_right.setVisibility(View.GONE);
            button_five_left.setVisibility(View.GONE);
            button_five_right.setVisibility(View.GONE);
            button_six_left.setVisibility(View.GONE);
            button_six_right.setVisibility(View.GONE);
            button_seven_left.setVisibility(View.GONE);
            button_seven_right.setVisibility(View.GONE);
            button_eight_left.setVisibility(View.GONE);
            button_eight_right.setVisibility(View.GONE);
            button_nine_left.setVisibility(View.GONE);
            button_nine_right.setVisibility(View.GONE);
        }
    }

    // On touch event for the activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(TAG,"Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d(TAG,"Action was UP");
                // System.exit(0);
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(TAG,"Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    // Life Cycle Methods

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Keyboard Main Activity has been destroyed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Keyboard Main Activity has started");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Keyboard Main Activity has restarted");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Keyboard Main Activity has resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Keyboard Main Activity has paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "Keyboard Main Activity has stopped");
    }



}
