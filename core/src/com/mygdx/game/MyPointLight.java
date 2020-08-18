package com.mygdx.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

public class MyPointLight extends PointLight implements Pool.Poolable {
    public boolean alive = false;
    public MyPointLight(RayHandler rayHandler, int rays) {
        super(rayHandler, rays);
    }

    public MyPointLight(RayHandler rayHandler, int rays, Color color, float distance, float x, float y) {
        super(rayHandler, rays, color, distance, x, y);
    }
    @Override
    public void reset() {

    }
}
