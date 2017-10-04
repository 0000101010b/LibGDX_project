package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

import static com.mygdx.game.Sprites.Player.State.JUMPING;

/**
 * Created by BenTh on 04/10/2017.
 */

public class Player extends Sprite{
    public enum State{FALING,JUMPING,STANDING,RUNNING};
    public State currentState;
    public State previousState;


    public World world;
    public Body b2body;
    private TextureRegion marioStand;

    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private float stateTimer;
    private boolean runningRight;


    public Player(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world=world;

        currentState=State.STANDING;
        previousState=State.STANDING;
        stateTimer=0;
        runningRight=true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=1; i<4;i++)
            frames.add(new TextureRegion(getTexture(),i*16,10,16,16));
        marioRun=new Animation(0.1f,frames);


        for(int i=4; i <6; i++)
            frames.add(new TextureRegion(getTexture(),i*16,10,16,16));
        marioJump= new Animation(0.1f,frames);


        definePlayer();
        marioStand = new TextureRegion(getTexture(),0,10,16,16);
        setBounds(0,0,16/MyGdxGame.PPM ,16/MyGdxGame.PPM);
        //setBounds(1,11,16,16);

        setRegion(marioStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth()/2,b2body.getPosition().y - getHeight()/2 );
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALING:
            case STANDING:
            default:
                region = marioStand;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer +dt:0;
        previousState= currentState;
        return region;


    }


    public State getState(){
        if(b2body.getLinearVelocity().y>0 || b2body.getLinearVelocity().y <0 && previousState == JUMPING )
            return JUMPING;
        else if(b2body.getLinearVelocity().y <0)
            return  State.FALING;
        else if (b2body.getLinearVelocity().x !=0 )
            return  State.RUNNING;
        else
            return  State.STANDING;
    }

    public void definePlayer(){
        BodyDef bdef =new BodyDef();
        bdef.position.set(32/ MyGdxGame.PPM,32/ MyGdxGame.PPM);
        bdef.type=BodyDef.BodyType.DynamicBody;
        b2body =world.createBody(bdef);

        FixtureDef fdef =new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5/ MyGdxGame.PPM);

        fdef.shape=shape;
        b2body.createFixture(fdef);
    }
}
