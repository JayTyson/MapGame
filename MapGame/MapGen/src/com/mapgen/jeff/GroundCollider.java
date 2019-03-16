package com.mapgen.jeff;

import com.badlogic.gdx.math.Rectangle;

public class GroundCollider {
	
	public int x,y,width,height;
	public Rectangle rect;
	
	public GroundCollider(int x,int y,int width,int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		rect = new Rectangle();
		rect.set(x, y, width, height);
	}
	
	public void updateGroundColliders(float delta) {
		rect.set(x, y, width, height);
	}
}
