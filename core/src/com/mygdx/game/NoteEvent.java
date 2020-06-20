package com.mygdx.game;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import sun.rmi.runtime.Log;

public class NoteEvent {

	public String MidiMsg;

	public String noteType;

	public String noteName;
	public int octave;
	public int velocity;
	public int noteValue;
	public NoteEvent(String midiMsg) {
		this.MidiMsg = midiMsg;
		String[] originalSplit = MidiMsg.split(": ");
		String[] split2 = originalSplit[2].split(Pattern.quote("("));
		noteType = split2[0];

		String keyInfo = split2[1].replace(")", "");
		String[] splitInfo = keyInfo.split(", ");
		if(Integer.parseInt(splitInfo[2]) == 0){
			if(noteType.equals("Control")){
				noteType = "ControlOff";
			}
			else{
				noteType = "NoteOff";
			}
		}
		else{
			velocity = Integer.parseInt(splitInfo[2]);
		}
		if(noteType.equals("NoteOn") || noteType.equals("NoteOff")){
			int midiVal = Integer.parseInt(splitInfo[1])-21;
			noteValue = midiVal;
			int valModulo = midiVal%12;
			octave = (int) Math.floor(midiVal/12);
			switch(valModulo){
				case 0:
					noteName = "A";
					break;
				case 1:
					noteName = "A#";
					break;
				case 2:
					noteName = "B";
					break;
				case 3:
					noteName = "C";
					break;
				case 4:
					noteName = "C#";
					break;
				case 5:
					noteName = "D";
					break;
				case 6:
					noteName = "D#";
					break;
				case 7:
					noteName = "E";
					break;
				case 8:
					noteName = "F";
					break;
				case 9:
					noteName = "F#";
					break;
				case 10:
					noteName = "G";
					break;
				case 11:
					noteName = "G#";
					break;
				default:
					noteName = "";
			}

		}

	}

	public String getMidiMsg() {
		return MidiMsg;
	}

	public void setMidiMsg(String midiMsg) {
		MidiMsg = midiMsg;
	}

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public int getOctave() {
		return octave;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
}

