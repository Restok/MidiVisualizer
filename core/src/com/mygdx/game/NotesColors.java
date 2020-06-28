package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotesColors {
    public List<Float> colorValues = new ArrayList<>();
    public String[] notes = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public HashMap<String, float[]> noteValueMap = new HashMap<>();
//    public static final int CR = 243; public static final int CG = 83; public static final int CB = 43;
private static final int CR = 251;
    private static final int CG = 154;
    private static final int CB = 0;
    private static final int CSR = 250;
    private static final int CSG = 190;
    private static final int CSB = 0;
    private static final int DR = 254;
    private static final int DG = 254;
    private static final int DB = 48;
    private static final int DSR = 209;
    private static final int DSG = 235;
    private static final int DSB = 39;
    private static final int ER = 102;
    private static final int EG = 178;
    private static final int EB = 47;
    private static final int FR = 0;
    private static final int FG = 147;
    private static final int FB = 207;
    private static final int FSR = 0;
    private static final int FSG = 69;
    private static final int FSB = 254;
    private static final int GR = 59;
    private static final int GG = 0;
    private static final int GB = 165;
    private static final int GSR = 135;
    private static final int GSG = 0;
    private static final int GSB = 177;
    private static final int AR = 168;
    private static final int AG = 18;
    private static final int AB = 73;
    private static final int ASR = 254;
    private static final int ASG = 35;
    private static final int ASB = 10;
    private static final int BR = 253;
    private static final int BG = 82;
    private static final int BB = 2;

    public NotesColors() {
        colorValues.add((float)CR);
        colorValues.add((float)CG);
        colorValues.add((float)CB);
        colorValues.add((float)CSR);
        colorValues.add((float)CSG);
        colorValues.add((float)CSB);
        colorValues.add((float)DR);
        colorValues.add((float)DG);
        colorValues.add((float)DB);
        colorValues.add((float)DSR);
        colorValues.add((float)DSG);
        colorValues.add((float)DSB);
        colorValues.add((float)ER);
        colorValues.add((float)EG);
        colorValues.add((float)EB);
        colorValues.add((float)FR);
        colorValues.add((float)FG);
        colorValues.add((float)FB);
        colorValues.add((float)FSR);
        colorValues.add((float)FSG);
        colorValues.add((float)FSB);
        colorValues.add((float)GR);
        colorValues.add((float)GG);
        colorValues.add((float)GB);
        colorValues.add((float)GSR);
        colorValues.add((float)GSG);
        colorValues.add((float)GSB);
        colorValues.add((float)AR);
        colorValues.add((float)AG);
        colorValues.add((float)AB);
        colorValues.add((float)ASR);
        colorValues.add((float)ASG);
        colorValues.add((float)ASB);
        colorValues.add((float)BR);
        colorValues.add((float)BG);
        colorValues.add((float)BB);
        int count = 0;
        for(String noteName : notes){
            float[] values = new float[3];
            for(int x = 0; x< 3; x++){
                values[x] = colorValues.get(count);
                count+=1;
            }
            noteValueMap.put(noteName, values);
        }
    }
}

