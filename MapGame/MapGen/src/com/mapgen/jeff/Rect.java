package com.mapgen.jeff;

import com.badlogic.gdx.math.Rectangle;

public class Rect {
	
	public Rectangle rect;
	public int x,y,width,height;
	
	public Rect(int x,int y) {
		this.x = x;
		this.y = y;
		width = 1;
		height = 1;
	}
	
	public Rect(int x,int y,int width,int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		rect = new Rectangle();
	}
	
	public void updateRect() {
		rect.set(x, y, width, height);
	}

}
