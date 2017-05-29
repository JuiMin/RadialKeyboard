package edu.washington.derek.radialkeyboard;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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

        // Set the button listeners

        Button topLeft = (Button)findViewById(R.id.top_left_button);
        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This button should toggle the shift
                state.toggleShift();
                if (state.getShiftStatus()) {
                    input.setText("Shift On");
                } else {
                    input.setText("Shift Off");
                }
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
                state.addCharacter(' ');
                input.setText(state.getSentence());
                input.setSelection(state.getSentence().length());
            }
        });

        // Buttons for the current keyboard
        Button button_one = (Button)findViewById(R.id.button_one);
        button_one.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        Log.i(TAG, "Keyboard Main Activity successfully created");
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
