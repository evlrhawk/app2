package com.example.evlrhawk.digitaljukebox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HostActivity extends AppCompatActivity {
    private final String TAG = "HostActivity";

    private Button btnPull, send, play;



    // to be used when making our queue in FireBase
    DatabaseReference databaseReference;
    private EditText URI;
    List<ToSend> sendList;
    List<String> keyList;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        // this gets the location of our firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("track");

        URI = (EditText) findViewById(R.id.txtHostSend);
        btnPull = (Button) findViewById(R.id.btnGuestSend);

        send = (Button) findViewById(R.id.btnHostSend);
        play = (Button) findViewById(R.id.btnHostPlay);

        listView = findViewById(R.id.list_view);
        sendList = new ArrayList<>();
        keyList = new ArrayList<>();

        // to call our addString button on click
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addString();
            }
        });

        btnPull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showData();
            }
        });
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

    /**
     * Shows the contents of the firebase database
     *
     * Written by Thomas Burr
     */
    public void showData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ToSendAdapter toSendAdapter;
                sendList.clear();
                toSendAdapter = new ToSendAdapter(HostActivity.this, sendList);
                listView.setAdapter(toSendAdapter);
                keyList.clear();
                for(DataSnapshot trackSnapshot : dataSnapshot.getChildren()) {
                    ToSend toSend = new ToSend();
                    if (toSend == null){
                        toSend.setToSend("1");
                        toSend = trackSnapshot.getValue(ToSend.class);
                        keyList.add(trackSnapshot.getKey());
                    }
                    else {
                        toSend = trackSnapshot.getValue(ToSend.class);
                        keyList.add(trackSnapshot.getKey());
                    }
                    sendList.add(toSend);
                }
                toSendAdapter = new ToSendAdapter(HostActivity.this, sendList);
                listView.setAdapter(toSendAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,"You done messed up Aaron!");
            }
        });
    }


}
