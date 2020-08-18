package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class MyConeLight extends ConeLight implements Pool.Poolable {
    /**
     * Creates light shaped as a circle's sector with given radius, direction and arc angle
     *
     * @param rayHandler      not {@code null} instance of RayHandler
     * @param rays            number of rays - more rays make light to look more realistic
     *                        but will decrease performance, can't be less than MIN_RAYS
     * @param color           color, set to {@code null} to use the default color
     * @param distance        distance of cone light
     * @param x               axis position
     * @param y               axis position
     * @param directionDegree direction of cone light
     * @param coneDegree
     */
    public MyConeLight(RayHandler rayHandler, int rays, Color color, float distance, float x, float y, float directionDegree, float coneDegree) {
        super(rayHandler, rays, color, distance, x, y, directionDegree, coneDegree);
    }



    @Override
    public void reset() {

    }
}
