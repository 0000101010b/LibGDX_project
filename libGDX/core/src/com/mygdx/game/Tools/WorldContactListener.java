package com.mygdx.game.Tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Sprites.InteractiveTileObject;

/**
 * Created by BenTh on 05/10/2017.
 */

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.log("Begin Contact", "");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "head" ||fixB.getUserData() == "head")
        {
            Fixture head =fixA.getUserData() =="head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB: fixA;

            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
