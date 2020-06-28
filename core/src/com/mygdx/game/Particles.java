package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

public class Particles  {
    public float elapsedTime = 0f;
    public Color color;
    public float x;
    public float y;
    public int count;
    public String noteID;
    public Boolean enabled = true;
    public float r;
    public float g;
    public float b;
    public float a = 1;
    public Particles(float elapsedTime, Color color, float x, int y, int count) {
        this.elapsedTime = elapsedTime;
        this.color = color;
        this.x = x;
        this.y = y;
        this.count = count;
    }

    public Particles(float elapsedTime, Color color, float x, float y, int count) {
        this.elapsedTime = elapsedTime;
        this.color = color;
        this.x = x;
        this.y = y;
        this.count = count;
    }



}
