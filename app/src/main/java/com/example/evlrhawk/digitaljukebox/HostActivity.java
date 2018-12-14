package com.example.evlrhawk.digitaljukebox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HostActivity extends AppCompatActivity {

    private Button btnPull, send, play;

    // to be used when making our queue in FireBase
    DatabaseReference databaseReference;
    private EditText URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);


        databaseReference = FirebaseDatabase.getInstance().getReference("track");
//        URI = (EditText) findViewById(R.id.sendString);
        btnPull = (Button) findViewById(R.id.btnGuestPush);
        send = (Button) findViewById(R.id.btnHostSend);
        play = (Button) findViewById(R.id.btnHostPlay);
    }


    /**
     * Adds a string to our firebase database
     *
     * Written by Christopher Wilson
     */

    public void addString() {
        final String TAG = "From addString()";

        Log.i(TAG, "Call to addString()");
        // the string to be sent
        String fromApp = URI.getText().toString();

        // if our text entry has something in it to send
        if (!TextUtils.isEmpty(fromApp)) {
            Log.v(TAG, "Sent to \"full\" 'if' statement");
            // get the database reference id
            String id = databaseReference.push().getKey();
            // make a new object to send to the database
            ToSend toSend1 = new ToSend(fromApp);

            // send the value to the database
            databaseReference.child(id).setValue(toSend1);

            Log.i(TAG, "Sent to firebase");
            Toast.makeText(HostActivity.this, "String sent to database", Toast.LENGTH_SHORT).show();

        }
        // if we don't have anything in our text entry box
        else {
            Log.v(TAG, "Sent to \"blank\" 'if' statement");

            // tell them to put something in
            Toast.makeText(HostActivity.this, "Please type a string to send.", Toast.LENGTH_LONG).show();
        }
    }
}
