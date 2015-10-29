package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;


/**
 * A login screen that offers login via email/password.
 */
public class JoinActivity extends Activity {
    public static final String ROOM_NAME = "Room_name";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private TextView roomNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        // Set up the login form.
        roomNameView = (TextView) findViewById(R.id.room_name);

        roomNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    attemptJoin(textView);
                }
                return true;
            }
        });

        final String[] recentRoom = {"room1", "room2", "room3", "room4", "project", "lab"};

        findViewById(R.id.room1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             suggestJoin(recentRoom[0]);
            }
        });

        findViewById(R.id.room2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestJoin(recentRoom[1]);
             }
        });

        findViewById(R.id.room3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestJoin(recentRoom[2]);
            }
        });

        findViewById(R.id.room4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestJoin(recentRoom[3]);
            }
        });

    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    public void suggestJoin(String room) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ROOM_NAME, room);
        startActivity(intent);
    }

    public void attemptJoin(View view) {
        // Reset errors.
        roomNameView.setError(null);

        // Store values at the time of the login attempt.
        String room_name = roomNameView.getText().toString();

        boolean cancel = false;

        // Check for a valid email address.
        if (TextUtils.isEmpty(room_name)) {
            roomNameView.setError(getString(R.string.error_field_required));

            cancel = true;
        } else if (!isEmailValid(room_name)) {
            roomNameView.setError(getString(R.string.error_invalid_room_name));
            cancel = true;
        }

        if (cancel) {
            roomNameView.setText("");
            roomNameView.requestFocus();
        } else {
            // Start main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(ROOM_NAME, room_name);
            startActivity(intent);
        }
    }

    private boolean isEmailValid(String room_name) {
        // http://stackoverflow.com/questions/8248277
        // Make sure alphanumeric characters
        return !room_name.matches("^.*[^a-zA-Z0-9 ].*$");
    }
}

