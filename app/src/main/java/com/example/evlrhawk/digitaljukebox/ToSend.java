package com.example.evlrhawk.digitaljukebox;

/**
 * This class defines the object that we will use
 * to send to firebase
 *
 * @author Christopher Wilson
 */
public class ToSend {
    private String toSend;

    public String getToSend() {
        return toSend;
    }

    public void setToSend(String toSend) {
        this.toSend = toSend;
    }

    public ToSend(String toSend){
        this.toSend = toSend;
    }

    public ToSend(){ toSend = ""; }
}
