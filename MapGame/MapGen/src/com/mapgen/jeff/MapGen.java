package com.mapgen.jeff;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.mapgen.jeff.Level.Edge;

public class MapGen implements ApplicationListener,ActionListener {
	
	private OrthographicCamera camera;
	private static SpriteBatch batch;
	private ShapeRenderer shape;
	
	JFrame frame;
	JButton jb;
	JTextField jtf1;
	JTextField jtf2;
	
	Level level1;
	Level level2;
	Level level3;
	
	private static final int SCREENWIDTH_VIEWPORT = 40;
	private static final int SCREENHEIGHT_VIEWPORT = 23;
	
	private int rows;
	private int cols;
	
	
	@Override
	public void create() {		
//		int width = Gdx.graphics.getWidth();
//		int height = Gdx.graphics.getHeight();
		
		frame = new JFrame();
		jb = new JButton("Click me");
		jtf1 = new JTextField();
		jtf2 = new JTextField();
		
		jtf1.setText("Width");
		jtf2.setText("Height");
		
		jb.addActionListener(this);
		
		frame.getContentPane().add(BorderLayout.CENTER,jb);
		frame.getContentPane().add(BorderLayout.EAST,jtf1);
		frame.getContentPane().add(BorderLayout.WEST,jtf2);
		frame.setSize(200, 200);
		frame.setVisible(true);
		
		camera = new OrthographicCamera(SCREENWIDTH_VIEWPORT,SCREENHEIGHT_VIEWPORT);
		
		rows = 29; //23 height
		cols = 45; //40 width
		
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		
		level1 = new Level(0,-5,cols,rows,camera);
		level2 = new Level(level1.width,0,level1.width,50);
		level3 = new Level(level2.width,level2.height,level2.width,level2.height/2);
	
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0.8f);
		
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {		
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		level1.updateMap(Gdx.graphics.getDeltaTime());		
		shape.setProjectionMatrix(camera.combined);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
				
		for(int i = 0;i < level1.levelTiles.size();i++) {
			batch.draw(level1.dirtTexture,level1.levelTiles.get(i).x,level1.levelTiles.get(i).y,1,1);
		}
		
		batch.end();
		
		shape.begin(ShapeType.Filled);
		shape.setColor(Color.YELLOW);
		shape.rect(level1.player.position.x, level1.player.position.y, level1.player.sprite.getWidth(), 
				level1.player.sprite.getHeight());
		shape.end();
		
		shape.begin(ShapeType.Line);
		shape.setColor(Color.YELLOW);
//		shape.rect(level1.player.position.x, level1.player.position.y, level1.player.sprite.getWidth(), 
//				level1.player.sprite.getHeight());
		
		for(int i = 0;i < level1.spears.size();i++) {
			shape.rect(level1.spears.get(i).position.x, level1.spears.get(i).position.y, level1.spears.get(i).sprite.getWidth(), 
					level1.spears.get(i).sprite.getHeight());
		}
		
		/*shape.line(level1.player.sprite.getX(), level1.player.sprite.getY(), 
				level1.rectSideObjects.get(0).x, level1.rectSideObjects.get(0).y);*/
		
		for(int i = 0;i < level1.rectSideObjects.size();i++) {
			/*shape.line(level1.player.sprite.getX(), level1.player.sprite.getY(), 
					level1.rectSideObjects.get(i).x, level1.rectSideObjects.get(i).y);*/
		}
		
		//shape.line(level1.edges.get(0).sx, level1.edges.get(0).sy, level1.edges.get(0).ex, level1.edges.get(0).ey);
		
		
		shape.setColor(Color.WHITE);
		
//		for(int i = 0;i < level1.spears.size();i++) {
//			shape.rect(level1.spears.get(i).spearRec.x, level1.spears.get(i).spearRec.y, level1.spears.get(i).spearRec.getWidth(), 
//					level1.spears.get(i).spearRec.getHeight());
//		}
		
		
		
		for(int i = 0;i < level1.cellObjects.size();i++) {
			level1.cellObjects.get(i).renderCell(shape, Color.WHITE);
		}
		
		for(int i = 0;i < level2.cellObjects.size();i++) {
			level2.cellObjects.get(i).renderCell(shape, Color.WHITE);
		}
		
		for(int i = 0;i < level3.cellObjects.size();i++) {
			level3.cellObjects.get(i).renderCell(shape, Color.WHITE);
		}
		
		shape.end();
		
		shape.begin(ShapeType.Filled);
		shape.setColor(Color.RED);
		for(int i = 0;i < level1.rectObjects.size();i++) {
			shape.rect(level1.rectObjects.get(i).x, level1.rectObjects.get(i).y, level1.rectObjects.get(i).rect.getWidth(), 
					level1.rectObjects.get(i).rect.getHeight());
		}
		for(MovingBlock mb : level1.movingBlocks) {
			shape.rect(mb.sprite.getX(), mb.sprite.getY(), mb.sprite.getWidth(), mb.sprite.getHeight());
		}
		shape.setColor(Color.GREEN);
		for(int i = 0;i < level1.rectSideObjects.size();i++) {
			shape.rect(level1.rectSideObjects.get(i).x, level1.rectSideObjects.get(i).y, level1.rectSideObjects.get(i).rect.getWidth(), 
					level1.rectSideObjects.get(i).rect.getHeight());
		}
		
		for(int i = 0;i < level1.rectObjects.size();i++) {
			level1.rectObjects.get(i).updateRect();
		}
		for(int i = 0;i < level1.rectSideObjects.size();i++) {
			level1.rectSideObjects.get(i).updateRect();
		}
		shape.end();
		
		shape.begin(ShapeType.Line);
		shape.setColor(Color.YELLOW);
		
		for(Edge e: level1.edges) {
			for(int j = 0;j < 2;j++) {
				float rx,ry;
				rx = (j==0?e.sx:e.ex) - level1.player.sprite.getX();
				ry = (j==0?e.sy:e.ey) - level1.player.sprite.getY();
				
			}
			/*shape.line(level1.player.sprite.getX(), level1.player.sprite.getY(), 
					level1.edges.get(i).sx, level1.edges.get(i).sy);
			shape.line(level1.player.sprite.getX(), level1.player.sprite.getY(), 
					level1.edges.get(i).ex, level1.edges.get(i).ey);*/
		}
		
		for(int i = 0;i < level1.edges.size()-1;i++) {
			//shape.line(level1.edges.get(i).sx, level1.edges.get(i).sy, level1.edges.get(i).ex, level1.edges.get(i).ey);
			
		}
		shape.line(level1.edges.get(0).sx, level1.edges.get(0).sy, level1.edges.get(0).ex, level1.edges.get(0).ey);
		shape.line(level1.edges.get(2).sx, level1.edges.get(2).sy, level1.edges.get(2).ex, level1.edges.get(2).ey);
		
		for(int i = 0;i < level1.rays.size();i++) {
			shape.line(level1.rays.get(i).sx, level1.rays.get(i).sy,
					level1.rays.get(i).ex, level1.rays.get(i).ey);
		}
		
		shape.end();
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		level1.tileWidth = Integer.parseInt(jtf1.getText());
		level1.tileHeight = Integer.parseInt(jtf2.getText());
		level1.addRect = true;
		
		System.out.println(level1.tileWidth + " " + level1.tileHeight);
	}
}
