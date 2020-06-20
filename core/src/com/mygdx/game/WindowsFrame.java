package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by julienvillegas on 07/12/2017.
 */

public class WindowsFrame {

    private Body body;
    private World world;

    public WindowsFrame(World aWorld, float width, float heigth) {


        world = aWorld;
        BodyDef bd = new BodyDef();
        bd.position.set(-width*5,-heigth/2);
        bd.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bd);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(width*10, 0.1f);
        body.setUserData(this);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = groundBox;
        body.createFixture(fixtureDef);


    }


}
