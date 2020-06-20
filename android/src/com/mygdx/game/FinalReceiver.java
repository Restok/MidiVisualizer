package com.mygdx.game;

import android.annotation.TargetApi;
import android.media.midi.MidiReceiver;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

@TargetApi(Build.VERSION_CODES.M)
public class FinalReceiver extends MidiReceiver{
    private static final long NANOS_PER_MILLISECOND = 1000000L;
    private static final long NANOS_PER_SECOND = NANOS_PER_MILLISECOND * 1000L;
    private long mStartTime;
    private long mLastTimeStamp = 0;
    MidiProcess midiProcess;

    public FinalReceiver(MidiProcess mp){
        this.midiProcess = mp;
        mStartTime = System.nanoTime();

    }
    @Override
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (timestamp == 0) {
            sb.append(String.format("-----0----: "));
        } else {
            long monoTime = timestamp - mStartTime;
            long delayTimeNanos = timestamp - System.nanoTime();
            int delayTimeMillis = (int)(delayTimeNanos / NANOS_PER_MILLISECOND);
            double seconds = (double) monoTime / NANOS_PER_SECOND;
            // Mark timestamps that are out of order.
            sb.append((timestamp < mLastTimeStamp) ? "*" : " ");
            mLastTimeStamp = timestamp;
            sb.append(String.format("%10.3f (%2d): ", seconds, delayTimeMillis));
        }
        sb.append(MidiPrinter.formatBytes(msg, offset, count));
        sb.append(": ");
        sb.append(MidiPrinter.formatMessage(msg, offset, count));
        String text = sb.toString();
        midiProcess.onNoteReceived(text);
    }
}
