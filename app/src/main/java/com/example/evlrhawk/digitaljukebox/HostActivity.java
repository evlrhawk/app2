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
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.List;

public class HostActivity extends AppCompatActivity {
    private final String TAG = "HostActivity";

    // spotify developer information
    private static final String CLIENT_ID = "27ead52d8b6d426a85b5a01cd63b388c";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "com.example.evlrhawk.digitaljukebox://callback";

    private Button btnPull, send, play;

    // to be used when making our queue in FireBase
    DatabaseReference databaseReference;
    // the string that will be sent to FireBase
    private EditText URI;
    // to make a ListView of what is in FireBase
    List<ToSend> sendList;
    List<String> keyList;
    private ListView listView;
    private SpotifyAppRemote mSpotifyAppRemote;
    private EditText string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        // this gets the location of our FireBase
        databaseReference = FirebaseDatabase.getInstance().getReference("track");
        // The text that will be sent to FireBase
        URI = (EditText) findViewById(R.id.txtHostSend);
        // A button to pull data from Firebase
        btnPull = (Button) findViewById(R.id.btnGuestSend);
        // Button to
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

        // shows the data when pull button is clicked
        btnPull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showData();
            }
        });

        /**
         * Plays our playlist
         *
         * @author Tyler Elikington, Anthony Lasley
         */
        Button playBtn = (Button) findViewById(R.id.btnHostPlay);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View playView) {
                // sends a uri through the PlayerApi to be played
                mSpotifyAppRemote.getPlayerApi().play("spotify:album:3JfSxDfmwS5OeHPwLSkrfr");

                // Subscribe to PlayerState
                mSpotifyAppRemote.getPlayerApi()
                        .subscribeToPlayerState()
                        .setEventCallback(new Subscription.EventCallback<PlayerState>() {
                            public void onEvent(PlayerState playerState) {
                                final Track track = playerState.track;
                                if (track != null) {
                                    Log.d("HostActivity", track.name + " by " + track.artist.name);
                                }
                            }
                        });
            }
        });
    }

    /**
     * Adds a string to our firebase database
     * String should be a URI to function properly
     *
     * @author Christopher Wilson
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
     * @author Thomas Burr
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


    /**
     * Connects to Spotify
     *
     * @author Tyler Elikington, Anthony Lasley
     */
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
                        Log.d("HostActivity", "Connected! Yay!");
                    }
                    public void onFailure(Throwable throwable) {
                        // Something went wrong when attempting to connect! Handle errors here
                        Log.e("HostActivity", throwable.getMessage(), throwable);
                    }
                });
    }

    // disconnects from spotify onStop
    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

}
