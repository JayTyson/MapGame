package com.mapgen.jeff;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Spear {
	
	public Vector2 position;
	public Vector2 velocity;
	public Sprite sprite;
	
	public boolean isAlive;
	private static final int RECT_OFFSET = 1;
	
	public Rectangle spearRec;
	
	public Spear(float x,float y) {
		position = new Vector2(x,y);
		velocity = new Vector2();
		sprite = new Sprite();
		
		spearRec = new Rectangle();
	}
	
	public void updateSpear(float deltaTime) {
		position.add(velocity);
		sprite.setPosition(position.x, position.y);
		spearRec.set(sprite.getX(), sprite.getY()-RECT_OFFSET, sprite.getWidth(), sprite.getHeight()+RECT_OFFSET);
	}
	
	public void renderSpear(SpriteBatch batch) {
		sprite.draw(batch);
	}

}
