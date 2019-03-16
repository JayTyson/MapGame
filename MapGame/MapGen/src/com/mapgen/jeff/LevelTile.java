package com.mapgen.jeff;

import java.io.Serializable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class LevelTile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6772614544845933550L;
	public int x,y;
	public int numArray;
	
	public LevelTile(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	public void renderObjects(ShapeRenderer shape) {
		shape.setColor(Color.RED);
		shape.begin(ShapeType.Filled);
		shape.rect(x, y, 1, 1, 0, 0, 0);
		shape.end();
		
		shape.setColor(Color.BLACK);
		shape.begin(ShapeType.Line);
		//shape.rect(x, y, 1, 1, 0, 0, 0);
		shape.end();
			
	}

}
