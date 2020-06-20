package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotesColors {
    public List<Float> colorValues = new ArrayList<>();
    public String[] notes = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public HashMap<String, float[]> noteValueMap = new HashMap<>();
    public static final int CR = 243; public static final int CG = 83; public static final int CB = 43;
    public static final int CSR = 188; public static final int CSG = 16; public static final int CSB = 122;
    public static final int DR = 249; public static final int DG = 166; public static final int DB = 32;
    public static final int DSR = 38; public static final int DSG = 94; public static final int DSB = 94;
    public static final int ER = 9; public static final int EG = 232; public static final int EB = 94;
    public static final int FR = 40; public static final int FG = 175; public static final int FB = 176;
    public static final int FSR = 146; public static final int FSG = 221; public static final int FSB = 222;
    public static final int GR = 242; public static final int GG = 95; public static final int GB = 92;
    public static final int GSR = 227; public static final int GSG = 79; public static final int GSB = 155;
    public static final int AR = 183; public static final int AG = 21; public static final int AB = 40;
    public static final int ASR = 75; public static final int ASG = 0; public static final int ASB = 204;
    public static final int BR = 157; public static final int BG = 255; public static final int BB = 92;

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

