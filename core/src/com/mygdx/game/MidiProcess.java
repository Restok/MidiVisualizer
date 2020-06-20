package com.mygdx.game;

import java.util.ArrayList;

import javax.sound.midi.MidiDevice;

public interface MidiProcess {
    void onNoteReceived(String midiMsg);
    void deviceAdded(String manufacturer);

}
