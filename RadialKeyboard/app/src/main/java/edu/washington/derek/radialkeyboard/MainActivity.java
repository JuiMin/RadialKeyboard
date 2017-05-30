package edu.washington.derek.radialkeyboard;

import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static android.view.DragEvent.ACTION_DRAG_ENTERED;
import static android.view.DragEvent.ACTION_DRAG_STARTED;

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
            activateButtons(layouts.getJSONObject(selectedLayout));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void activateButtons(final JSONObject layout) {
        // Get Application State

        final ApplicationState state = ApplicationState.getInstance();

        // Get button references
        final Button button_one = (Button)findViewById(R.id.button_one);
        final Button button_two = (Button)findViewById(R.id.button_two);
        final Button button_three = (Button)findViewById(R.id.button_three);
        final Button button_four = (Button)findViewById(R.id.button_four);
        final Button button_five = (Button)findViewById(R.id.button_five);
        final Button button_six = (Button)findViewById(R.id.button_six);
        final Button button_seven = (Button)findViewById(R.id.button_seven);
        final Button button_eight = (Button)findViewById(R.id.button_eight);
        final Button button_nine = (Button)findViewById(R.id.button_nine);
        final Button button_center = (Button)findViewById(R.id.center_button);

        button_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert desired code here
                if (!state.getCurrentCharacter().equals(button_one.getText().toString())) {
                    state.setCurrentCharacter(button_one.getText().toString());
                }
                // If the state layout is less than two (not number) do nothing
                // If the state layout is two then you should submit to the state on click
                if (state.getCurrentLayout() ==  2) {
                    submitText();
                }
                Log.i(TAG, "you touched button one");
            }
        });

        button_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert desired code here
                if (!state.getCurrentCharacter().equals(button_two.getText().toString())) {
                    state.setCurrentCharacter(button_two.getText().toString());
                }
                // If the state layout is less than two (not number) do nothing
                // If the state layout is two then you should submit to the state on click
                if (state.getCurrentLayout() ==  2) {
                    submitText();
                }
                Log.i(TAG, "you touched button two");
            }
        });

        button_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert desired code here
                if (!state.getCurrentCharacter().equals(button_three.getText().toString())) {
                    state.setCurrentCharacter(button_three.getText().toString());
                }
                // If the state layout is less than two (not number) do nothing
                // If the state layout is two then you should submit to the state on click
                if (state.getCurrentLayout() ==  2) {
                    submitText();
                }
                Log.i(TAG, "you touched button three");
            }
        });

        button_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert desired code here
                if (!state.getCurrentCharacter().equals(button_four.getText().toString())) {
                    state.setCurrentCharacter(button_four.getText().toString());
                }
                // If the state layout is less than two (not number) do nothing
                // If the state layout is two then you should submit to the state on click
                if (state.getCurrentLayout() ==  2) {
                    submitText();
                }
                Log.i(TAG, "you touched button four");
            }
        });

        button_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert desired code here
                if (!state.getCurrentCharacter().equals(button_five.getText().toString())) {
                    state.setCurrentCharacter(button_five.getText().toString());
                }
                // If the state layout is less than two (not number) do nothing
                // If the state layout is two then you should submit to the state on click
                if (state.getCurrentLayout() ==  2) {
                    submitText();
                }
                Log.i(TAG, "you touched button five");
            }
        });

        button_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert desired code here
                if (!state.getCurrentCharacter().equals(button_six.getText().toString())) {
                    state.setCurrentCharacter(button_six.getText().toString());
                }
                // If the state layout is less than two (not number) do nothing
                // If the state layout is two then you should submit to the state on click
                if (state.getCurrentLayout() ==  2) {
                    submitText();
                }
                Log.i(TAG, "you touched button six");
            }
        });

        button_seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert desired code here
                if (!state.getCurrentCharacter().equals(button_seven.getText().toString())) {
                    state.setCurrentCharacter(button_seven.getText().toString());
                }
                // If the state layout is less than two (not number) do nothing
                // If the state layout is two then you should submit to the state on click
                if (state.getCurrentLayout() ==  2) {
                    submitText();
                }
                Log.i(TAG, "you touched button seven");
            }
        });

        button_eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert desired code here
                if (!state.getCurrentCharacter().equals(button_eight.getText().toString())) {
                    state.setCurrentCharacter(button_eight.getText().toString());
                }
                // If the state layout is less than two (not number) do nothing
                // If the state layout is two then you should submit to the state on click
                if (state.getCurrentLayout() ==  2) {
                    submitText();
                }
                Log.i(TAG, "you touched button eight");
            }
        });

        button_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert desired code here
                if (!state.getCurrentCharacter().equals(button_nine.getText().toString())) {
                    state.setCurrentCharacter(button_nine.getText().toString());
                }
                // If the state layout is less than two (not number) do nothing
                // If the state layout is two then you should submit to the state on click
                if (state.getCurrentLayout() ==  2) {
                    submitText();
                }
                Log.i(TAG, "you touched button nine");
            }
        });

        // Enable the touch listener
        button_center.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (state.getCurrentLayout() < 2) {
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        View hovered = findViewAtPosition(findViewById(R.id.activityLayout), (int) event.getRawX(), (int) event.getRawY());
                        if (hovered != null) {
                            Log.i(TAG, "HOVERED: " + findViewById(hovered.getId()));
                            hovered.performClick();
                        } else {
                            Log.i(TAG, "HOVERED: " + hovered);
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // On release the button should do its thing
                        Log.i(TAG, "Center button released");
                        state.addCharacter();
                        String sentence = state.getSentence();
                        EditText input = (EditText) findViewById(R.id.input_area);
                        input.setText(sentence);
                        input.setSelection(sentence.length());
                    }
                }
                return false;
            }
        });
        // Enable the click listener
        button_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.getCurrentLayout() == 2) {
                    if (!state.getCurrentCharacter().equals(button_center.getText().toString())) {
                        state.setCurrentCharacter(button_center.getText().toString());
                    }
                    submitText();
                }
                Log.i(TAG, "you touched button center");
            }
        });
    }

    public void submitText() {
        ApplicationState state = ApplicationState.getInstance();
        state.addCharacter();
        String sentence = state.getSentence();
        EditText input = (EditText)findViewById(R.id.input_area);
        input.setText(sentence);
        input.setSelection(sentence.length());
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

    // Returns the view that is currently being hovered
    private View findViewAtPosition(View parent, int x, int y) {
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)parent;
            for (int i=0; i<viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                View viewAtPosition = findViewAtPosition(child, x, y);
                if (viewAtPosition != null) {
                    return viewAtPosition;
                }
            }
            return null;
        } else {
            Rect rect = new Rect();
            parent.getGlobalVisibleRect(rect);
            if (rect.contains(x, y)) {
                return parent;
            } else {
                return null;
            }
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
