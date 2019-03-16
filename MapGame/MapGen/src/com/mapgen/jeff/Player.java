package com.mapgen.jeff;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
	
	public Vector2 position;
	public Vector2 velocity;
	public Sprite sprite;
	
	public Rectangle bottomRect;
	public Rectangle leftRect;
	public Rectangle rightRect;
		
	public float gravity = 0.05f;
	public static final float MAX_FALLING_VELOCITY = -1.0f;
	
	public Player() {
		velocity = new Vector2();
		position = new Vector2(1,1);
		
		sprite = new Sprite();
		sprite.setSize(1, 1.8f);
		
		bottomRect = new Rectangle();
		leftRect = new Rectangle();		
		rightRect = new Rectangle();
	}
	
	public void updatePlayer() {
		
		velocity.y -= gravity;
		if(velocity.y < MAX_FALLING_VELOCITY) {
			velocity.y = MAX_FALLING_VELOCITY;
		}
		position.add(velocity);
		sprite.setPosition(position.x, position.y);
		
		bottomRect.set(sprite.getX()+((sprite.getWidth()*0.5f)/2), sprite.getY(), sprite.getWidth()*0.5f, sprite.getHeight()*0.25f);
		leftRect.set(sprite.getX(), sprite.getY()+sprite.getHeight()*0.2f, sprite.getWidth()*0.25f, sprite.getHeight()*0.6f);
		rightRect.set(sprite.getX()+sprite.getWidth()*0.75f, sprite.getY()+sprite.getHeight()*0.2f, sprite.getWidth()*0.25f, sprite.getHeight()*0.6f);
		
	}
	
	public void renderPlayer(SpriteBatch spriteBatch) {
		sprite.draw(spriteBatch);
	}
	
}
