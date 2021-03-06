package com.example.evlrhawk.digitaljukebox;


import android.content.Intent;
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
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // spotify developer information
    private static final String CLIENT_ID = "27ead52d8b6d426a85b5a01cd63b388c";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "com.example.evlrhawk.digitaljukebox://callback";


    /**
     * Starts the app with the Spotify Login
     * It has two buttons- Host and Guest
     *      Host - takes to Host Activity
     *      Guest - take to Guest Activity
     *
     * @author Tyler Elikington, Anthony Lasley
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // authenticates user through spotify
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        /**
         * Starts the Host Activity
         * @params an onClickListener
         */
        Button hostBtn = (Button) findViewById(R.id.hostBtn);
        // starts host activity if button is pressed
        hostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View hostView){
                startActivity(new Intent(MainActivity.this, HostActivity.class));
            }
        });

        /**
         * Starts the Guest Activity
         * @params an onClickListener
         */
        Button guestBtn = (Button) findViewById(R.id.guestBtn);
        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View guestView){
                startActivity(new Intent(MainActivity.this, GuestActivity.class));
            }
        });

    }


    /**
     * Based on whether the Spotify login was successful
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    break;
                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

}
