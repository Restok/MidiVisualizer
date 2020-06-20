package com.mygdx.game;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

import java.io.IOException;

public class AndroidLauncher extends AndroidApplication {
	@TargetApi(Build.VERSION_CODES.M)
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		AndroidMidiReceiver androidMidiReceiver = new AndroidMidiReceiver(this);

		MyGdxGame myGdxGame = new MyGdxGame();

		androidMidiReceiver.coreProcess = myGdxGame;
		myGdxGame.midiProcess = androidMidiReceiver;
		androidMidiReceiver.deviceAdded(androidMidiReceiver.manufacturer);
		byte[] buffer = new byte[32];
		int numBytes = 0;
		int channel = 3; // MIDI channels 1-16 are encoded as 0-15.
		buffer[numBytes++] = (byte)(0x90 + (channel - 1)); // note on
		buffer[numBytes++] = (byte)60; // pitch is middle C
		buffer[numBytes++] = (byte)127; // max velocity
		int offset = 0;
		// post is non-blocking
//		try {
//			androidMidiReceiver.midiFramer.send(buffer, offset, numBytes);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		initialize((myGdxGame), config);
	}
}
