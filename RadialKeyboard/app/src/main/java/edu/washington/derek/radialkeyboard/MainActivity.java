package edu.washington.derek.radialkeyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "Keyboard Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the Edit Text so that we can update the text field
        EditText input = (EditText)findViewById(R.id.input_area);


        // Set the button listeners

        Button topLeft = (Button)findViewById(R.id.top_left_button);
        topLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Update the

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
