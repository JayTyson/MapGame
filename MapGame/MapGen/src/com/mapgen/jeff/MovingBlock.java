package com.mapgen.jeff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class MovingBlock {
	
	public Vector2 position;
	public Vector2 velocity;
	public Texture texture;
	public Sprite sprite;
	
	public boolean isAlive;
	
	public MovingBlock() {
		
		velocity = new Vector2();
		position = new Vector2();
		
		//texture = new Texture(Gdx.files.internal(""));
		sprite = new Sprite();
		sprite.setSize(5f, 1f);
		sprite.setPosition(position.x, position.y);
	}
	
	public void update() {
		position.add(velocity);
		sprite.setPosition(position.x, position.y);
	}

}
