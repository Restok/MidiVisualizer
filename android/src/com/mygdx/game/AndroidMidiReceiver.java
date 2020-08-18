package com.mygdx.game;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.media.midi.MidiReceiver;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import static android.content.ContentValues.TAG;


@TargetApi(Build.VERSION_CODES.M)
public class AndroidMidiReceiver implements MidiProcess{


    @Override
    public void onNoteReceived(String midiMsg) {
        coreProcess.onNoteReceived(midiMsg);
    }

    @Override
    public void deviceAdded(String manufacturer) {
        coreProcess.deviceAdded(manufacturer);
    }


    MidiManager m;
    Context context;
    MidiDeviceInfo info;
    MidiDevice oMidiDevice;
    MidiOutputPort mOutputPort;
    MidiFramer midiFramer;
    FinalReceiver finalReceiver;
    MidiProcess coreProcess;
    String manufacturer = "bruh";


    @TargetApi(Build.VERSION_CODES.M)
    public AndroidMidiReceiver(Context context) {
        this.context = context;
        m = (MidiManager) context.getSystemService(Context.MIDI_SERVICE);
        finalReceiver = new FinalReceiver(this);
        midiFramer = new MidiFramer(finalReceiver);
        MidiDeviceInfo[] infos = m.getDevices();
        info = infos[0];
        Bundle properties = info.getProperties();
        manufacturer = properties.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);

        m.openDevice(info, new MidiManager.OnDeviceOpenedListener() {

            @Override
            public void onDeviceOpened(MidiDevice device) {
                if (device == null) {
                    Log.e("MIDIOPEN", "could not open " + info);
                } else {
                    oMidiDevice = device;
                    mOutputPort = device.openOutputPort(0);
                    if (mOutputPort == null) {
                        Log.e("MIDIOPEN",
                                "could not open output port for " + info);
                        return;
                    }
                    mOutputPort.connect(midiFramer);


                }
            }
        }, null);
        m.registerDeviceCallback(new MidiManager.DeviceCallback() {
            public void onDeviceAdded( MidiDeviceInfo in ) {
                info = in;
                Bundle properties = info.getProperties();
                manufacturer = properties.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);
                deviceAdded(manufacturer);
                m.openDevice(info, new MidiManager.OnDeviceOpenedListener() {

                    @Override
                    public void onDeviceOpened(MidiDevice device) {
                        if (device == null) {
                            Log.e("MIDIOPEN", "could not open " + info);
                        } else {
                            oMidiDevice = device;
                            mOutputPort = device.openOutputPort(0);
                            mOutputPort.connect(midiFramer);
                            if (mOutputPort == null) {
                                Log.e("MIDIOPEN",
                                        "could not open output port for " + info);
                                return;
                            }
                        }
                    }
                }, null);
            }
            public void onDeviceRemoved( MidiDeviceInfo info ) {

            }
        }, null);




    }

}
