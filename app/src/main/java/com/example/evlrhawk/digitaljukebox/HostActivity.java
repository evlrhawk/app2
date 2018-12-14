package com.example.evlrhawk.digitaljukebox;

import android.os.Bundle;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;

public class HostActivity {

    private Button btnPull, send, play;

    // to be used when making our queue in FireBase
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    }
}
