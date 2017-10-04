package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Tools.B2WorldCreator;

import sun.rmi.runtime.Log;

/**
 * Created by BenTh on 03/10/2017.
 */

public class PlayScreen implements Screen {

    private MyGdxGame game;
    private TextureAtlas atlas;

    //Texture texture;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map varaibles
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;

    public PlayScreen(MyGdxGame game){
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        //texture = new Texture("badlogic.jpg");
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(MyGdxGame.V_Width/ MyGdxGame.PPM,MyGdxGame.V_Height/ MyGdxGame.PPM,gamecam);
        hud =new Hud(game.batch);

        mapLoader=new TmxMapLoader();
        map=mapLoader.load("Level1.tmx");
        renderer= new OrthogonalTiledMapRenderer(map,1/MyGdxGame.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2 ,gamePort.getWorldHeight()/2,0);



        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world,map);
        player = new Player(world,this);

    }
    public  TextureAtlas getAtlas(){
        return  atlas;
    }

    @Override
    public void show() {

    }
    private  boolean keyPressed =false;
    public void handleInput(float dt) {
        /*
        if(Gdx.input.isTouched())
            gamecam.position.x += 100 * dt;
        */

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (!keyPressed) {
                keyPressed = true;
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            }
        } else {
            keyPressed = false;
        }

        /*
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            Gdx.app.log("Debug", "Back pressed!");
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }*/
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

    }

    public void update(float dt)
    {
        handleInput(dt);
        world.step(1/60f,6,2);
        player.update(dt);
        gamecam.position.x=player.b2body.getPosition().x;
        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear((GL20.GL_COLOR_BUFFER_BIT));

        renderer.render();

        b2dr.render(world,gamecam.combined);


        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        /*  game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        game.batch.draw(texture,0,0);
        game.batch.end();*/

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}
