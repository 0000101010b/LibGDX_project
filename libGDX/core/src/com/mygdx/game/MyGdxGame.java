package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class MyGdxGame extends Game {

	public static final  int V_Width =400;
	public static final int V_Height=208;
	public static final float PPM=100;


	public static final short DEFAULT_BIT=1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT=4;
	public static final short COIN_BIT =8;
	public static final short DESTROYED_BIT=16;


	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
