package com.example.evlrhawk.digitaljukebox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;

public class HostActivity extends AppCompatActivity {

    private Button btnPull, send, play;

    // to be used when making our queue in FireBase
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        btnPull = (Button) findViewById(R.id.btnHostPull);
        send = (Button) findViewById(R.id.btnHostSend);
        play = (Button) findViewById(R.id.btnHostPlay);
    }
}
