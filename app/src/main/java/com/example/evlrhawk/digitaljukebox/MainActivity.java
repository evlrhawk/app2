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

    private static final String CLIENT_ID = "27ead52d8b6d426a85b5a01cd63b388c";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "com.example.evlrhawk.digitaljukebox://callback";
    private SpotifyAppRemote mSpotifyAppRemote;
//    private static final String TAG = "Failed Here";
    private EditText string;
    private Button send, btnGuest;
//    private ListView listView;
//    List<ToSend> sendList;
//    List<String> keyList;

    // to be used when making our queue in FireBase
//    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        listView = findViewById(R.id.list_view);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        // get the database reference
//        databaseReference = FirebaseDatabase.getInstance().getReference("track");
//        sendList = new ArrayList<>();
//        keyList = new ArrayList<>();
        // string taken from text entry in app
//        string = (EditText) findViewById(R.id.sendString);
        // our button

//        // to call our addString button on click
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addString();
//            }
//        });
//        // Attach a listener to read the data at our posts reference

//        btnPull.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showData();
//            }
//        });

        //should make a button to go to the view

        Button hostBtn = (Button) findViewById(R.id.hostBtn);
        hostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View hostView){

                startActivity(new Intent(MainActivity.this, HostActivity.class));
            }
        });

        Button guestBtn = (Button) findViewById(R.id.guestBtn);
        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View guestView){
                startActivity(new Intent(MainActivity.this, GuestActivity.class));
            }
        });

    }

//    public void showData(){
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ToSendAdapter toSendAdapter;
//                sendList.clear();
//                toSendAdapter = new ToSendAdapter(MainActivity.this, sendList);
//                listView.setAdapter(toSendAdapter);
//                keyList.clear();
//                for(DataSnapshot trackSnapshot : dataSnapshot.getChildren()) {
//                    ToSend toSend = new ToSend();
//                    if (toSend == null){
//                        toSend.setToSend("1");
//                        toSend = trackSnapshot.getValue(ToSend.class);
//                        keyList.add(trackSnapshot.getKey());
//                    }
//                    else {
//                        toSend = trackSnapshot.getValue(ToSend.class);
//                        keyList.add(trackSnapshot.getKey());
//                    }
//                    sendList.add(toSend);
//                }
//                toSendAdapter = new ToSendAdapter(MainActivity.this, sendList);
//                listView.setAdapter(toSendAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG,"You done messed up Aaron!");
//            }
//        });
//    }

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

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                        // Now you can start interacting with App Remote
                        connected();
                    }
                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }
    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
    private void connected() {
        // Play a playlist
//        mSpotifyAppRemote.getPlayerApi().play("spotify:user:spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
//        mSpotifyAppRemote.getPlayerApi().play("spotify:user:sofigomezc:playlist:1EoaGONaSh0XVkuljYXvdq");
        mSpotifyAppRemote.getPlayerApi().play("spotify:album:3JfSxDfmwS5OeHPwLSkrfr");
        // Subscribe to PlayerState
        String songReq = string.getText().toString();


        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(new Subscription.EventCallback<PlayerState>() {
                    public void onEvent(PlayerState playerState) {
                        final Track track = playerState.track;
                        if (track != null) {
                            Log.d("MainActivity", track.name + " by " + track.artist.name);
                        }
                    }
                });
    }

}
