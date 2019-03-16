package com.mapgen.jeff;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class Level extends InputAdapter {
	
	public int x,y,touchX,touchY;
	public int width,height,tileWidth,tileHeight;
	
	public Cell[][] grid;
	public ArrayList<LevelTile> levelTiles;
	public ArrayList<Rect> rectObjects;
	public ArrayList<Rect> rectSideObjects;
	public ArrayList<Cell> cellObjects;
	public ArrayList<Spear> spears;
	public ArrayList<MovingBlock> movingBlocks;
	public ArrayList<Edge> edges;
	public ArrayList<Ray> rays;
	
	public CameraHelper cameraHelper;
	
	public GroundCollider gc;
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	public Texture dirtTexture;
	
	private Vector3 temp;
	//private Color color;
	
	public Player player;
	public MovingBlock mb;
	
	public boolean isTouching;
	public boolean shoot;
	public boolean jump;
	public boolean addRect;
	
	private float timeCount;
	private float timeMax;
	private int rectNumArray;
	private int rectSideNumArray;
	private boolean leftPressed;
	private boolean rightPressed;
	
	class Edge {
		float sx,sy;
		float ex,ey;
	}
	
	class Ray {
		float sx,sy;
		float ex,ey;
	}
	
	
	
	public Level(int x,int y,int width,int height,OrthographicCamera camera) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		tileWidth = 1;
		tileHeight = 1;
		
		rectNumArray = 0;
		rectSideNumArray = 0;
		
		player = new Player();
		//mb = new MovingBlock();
		dirtTexture = new Texture(Gdx.files.internal("data/dirt.png"));
		
		touchX = 0;
		touchY = 0;
		
		timeMax = 1.f;
		
		grid = new Cell[width][height];
		
		levelTiles = new ArrayList<LevelTile>();
		rectObjects = new ArrayList<Rect>();
		rectSideObjects = new ArrayList<Rect>();
		cellObjects = new ArrayList<Cell>();
		spears = new ArrayList<Spear>();
		movingBlocks = new ArrayList<MovingBlock>();
		edges = new ArrayList<Edge>();
		rays = new ArrayList<Ray>();
		
		//System.out.println(rectSideObjects.size());
		
		this.camera = camera;
		temp = new Vector3();
		
		cameraHelper = new CameraHelper();
		cameraHelper.setTarget(player.sprite);
		
		for(int cx = 0;cx < width;cx++) {
			for(int cy = 0;cy < height;cy++) {
				grid[cx][cy] = new Cell(cx+x,cy+y,1,1);
			}
		}
		
		for(int cx = 0;cx < width;cx++) {
			for(int cy = 0;cy < height;cy++) {
				cellObjects.add(grid[cx][cy]);
			}
		}
		
		
		cameraHelper.setZoom(1.55f);
		
		
//	    SaveSystem.reloadLevel(levelTiles); forget about this for now:
		SaveSystem.reloadSideColliders(rectSideObjects);
		SaveSystem.reloadColliders(rectObjects);
		
		System.out.println(rectSideObjects.size());
		//System.out.println(rectObjects.size());
		
		calculateEdges(rectSideObjects);
		
		for(int i = 0;i < edges.size()-1;i++) {
			if(edges.get(i).ex == edges.get(i+1).ex && edges.get(i).ey == edges.get(i+1).ey) {
				//edges.remove(i+1);
			}
		}
		
		System.out.println(edges.size());
		
				
		gc = new GroundCollider(rectObjects.get(rectObjects.size()-1).x,rectObjects.get(rectObjects.size()-1).y+4,
				rectObjects.get(rectObjects.size()-1).width,rectObjects.get(rectObjects.size()-1).height+4);
		
		
		
		/*Edge e = new Edge();
		e.sx = rectSideObjects.get(0).x;
		e.sy = rectSideObjects.get(0).y;
		
		e.ex = e.sx;
		e.ey = rectSideObjects.get(0).y + rectSideObjects.get(0).height;
		edges.add(e);*/
		
		
		Gdx.input.setInputProcessor(this);
		
		/*XmlReader r = new XmlReader();
		try {
			Element root = r.parse(Gdx.files.internal("colliders.xml"));
			System.out.println(Integer.parseInt(root.getAttribute("width")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//////////////////////////////////////
		StringWriter writer = new StringWriter();
		XmlWriter xml = new XmlWriter(writer);
		try {
			xml.element("Properties");
			xml.attribute("width","20");
			xml.pop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileHandle handle = Gdx.files.external("colliders.xml");
		handle.writeString(writer.toString(), false);*/
		createRay();
		
	}
	
	public Level(int x,int y,int width,int height) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		
		grid = new Cell[width][height];
		cellObjects = new ArrayList<Cell>();
		
		for(int cx = 0;cx < width;cx++) {
			for(int cy = 0;cy < height;cy++) {
				grid[cx][cy] = new Cell(cx+x,cy+y,1,1);
			}
		}
		
		for(int cx = 0;cx < width;cx++) {
			for(int cy = 0;cy < height;cy++) {
				cellObjects.add(grid[cx][cy]);
			}
		}
				
	}
	
	private void createRay() {
		
		/*Edge edge = edges.get(0);
		
		float vx = edge.sx - player.sprite.getX();
		float vy = edge.sy - player.sprite.getY();
		
		float angle = (float) (MathUtils.atan2(vy, vx) * 180 / Math.PI);
		float length = (float) Math.sqrt(vx * vx + vy * vy);
		
		float ang = 0;*/
		
		
		//System.out.println(length);
		
		Ray ray = new Ray();
		rays.add(ray);
		
	}
	
	private void updateRay() {
		Edge edge = edges.get(2);
		
		float vx = edge.ex - player.sprite.getX();
		float vy = edge.ey - player.sprite.getY();
		
		float angle = (float) (MathUtils.atan2(vy, vx) * 180 / Math.PI);
		float length = (float) Math.sqrt(vx * vx + vy * vy);
		float len2 = length * 10;
		
		float min_t1 = len2 * len2;
		float interX = 0;
		float interY = 0;
		
		float px = player.sprite.getX() + player.sprite.getWidth()*0.5f;
		float py = player.sprite.getY() + player.sprite.getHeight()*0.5f;
		
		for(Ray ray: rays) {
			ray.sx = px;
			ray.sy = py;
			
			ray.ex = px + MathUtils.cosDeg(angle) * len2;
			ray.ey = py + MathUtils.sinDeg(angle) * len2;
		}
		//System.out.println(rays.size());
		
		Ray ray = rays.get(0);
		
		for(Edge es : edges) {
			
			float evx = es.ex - es.sx;
			float evy = es.ey - es.sy;
			if(Math.abs(evx - vx) > 0.0f && Math.abs(evy - vy) > 0.0f) {
				
				float t2 = (vx * (es.sy - py) + (vy *(px - es.sx))) / (evx * vy - evy * vx);
				float t1 = (es.sx + evx * t2 - px) / vx;
				
				if(t1 > 0 && t2 >= 0 && t2 <= 1.0f) {
					if(t1 < min_t1) {
						min_t1 = t1;
						interX = px + vx * t1;
						interY = py + vy * t1;
						ray.ex = interX;
						ray.ey = interY;
					}
				}
			}
		}
	}
	
	private void lineCollision() {
		Edge edge = edges.get(0);
		Ray ray = rays.get(0);
		
		float evx = edge.ex - edge.sx;
		float evy = edge.ey - edge.sy;
	}
	
	private void calculateEdges(ArrayList<Rect> sideObjects) {
		for(Rect rect : sideObjects) {
			//Create western edge
			Edge we = new Edge();
			we.sx = rect.x;
			we.sy = rect.y;
			
			we.ex = rect.x;
			we.ey = rect.y + rect.height;
			edges.add(we);
			
			//Create eastern edge
			Edge ee = new Edge();
			ee.sx = rect.x + rect.width;
			ee.sy = rect.y;
			
			ee.ex = rect.x + rect.width;
			ee.ey = rect.y + rect.height;
			edges.add(ee);
			
			//Create northern edge
			Edge ne = new Edge();
			ne.sx = rect.x;
			ne.sy = rect.y + rect.height;
			
			ne.ex = rect.x + rect.width;
			ne.ey = rect.y + rect.height;
			edges.add(ne);
			
			//Create southern edge
			Edge se = new Edge();
			se.sx = rect.x;
			se.sy = rect.y;
			
			se.ex = rect.x + rect.width;
			se.ey = rect.y;
			edges.add(se);
		}
	}
	
	
	public void updateMap(float delta) {
		
		updateCameraControlls();
		cameraHelper.applyTo(camera);
		updateControlls(delta);		
		updateSideRectCollision(delta);	
		
		for(MovingBlock mb : movingBlocks) {
			mb.update();
		}
		//System.out.println(movingBlocks.get(0).sprite.getX());
		
		if(player.position.y < 0) {
			player.position.set(4, 10);
		}
		
		if(isBottomRectColliding() || isBottomRectCollidingWithSideRect()) {
			if(leftPressed == false) {
				if(player.velocity.x < 0) {
					player.velocity.x = 0;
				}
			}
		}
		
		if(isBottomRectColliding() || isBottomRectCollidingWithSideRect()) {
			if(rightPressed == false) {
				if(player.velocity.x > 0) {
					player.velocity.x = 0;
				}
			}
		}
		
		if(jump) {
			if(isBottomRectColliding() || isBottomRectCollidingWithSideRect()) {
				player.velocity.y = 0.95f;
			} 
			jump = false;
		} 
		
		for(int i = 0;i < spears.size();i++) {
			if(Intersector.intersectRectangles(player.bottomRect, spears.get(i).spearRec, spears.get(i).spearRec)) {
				player.velocity.y = 1.5f;
				player.velocity.x = 0;
			}
		}
		
		for(int i = 0;i < spears.size();i++) {
			//if(spears.get(i).isAlive) {
			spears.get(i).updateSpear(delta);
			//}
		}
		
		for(int i = 0;i < rectSideObjects.size();i++) {
			for(int j = 0;j < spears.size();j++) {
				if(isSideRectSpearColliding(i,j)) {
					spears.get(j).position.x = rectSideObjects.get(i).x - spears.get(j).sprite.getWidth();
					spears.get(j).velocity.x = 0;
				}
			}
		}
		
		player.updatePlayer();
		cameraHelper.update(player.velocity);
		
		updateRay();	
	}
	
	private void updateCameraControlls() {
		if(Gdx.input.isKeyPressed(Keys.PAGE_DOWN)) {
			cameraHelper.position.x += 0.25f;
		}
		if(Gdx.input.isKeyPressed(Keys.PAGE_UP)) {
			cameraHelper.position.x -= 0.25f;
		}
		
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			cameraHelper.position.y += 0.25f;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			cameraHelper.position.y -= 0.25f;
		}
		
		if(Gdx.input.isKeyPressed(Keys.Z)) {
			cameraHelper.addZoom(0.05f);
		}
		
		if(Gdx.input.isKeyPressed(Keys.R)) {
			cameraHelper.resetZoom(1.25f);
		}
	}
	
	public void updateControlls(float delta) {
		if(Gdx.input.justTouched() && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			int x,y;
			temp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(temp);
			
			x = (int) temp.x;
			y = (int) temp.y;
			
			if(addRect) {
				rectObjects.add(new Rect(x,y,tileWidth,tileHeight));
				rectNumArray = rectObjects.size();
				//addRect = false;
			}
			System.out.println(rectObjects.size());
		}
		
		if(Gdx.input.justTouched() && Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) {
			int x,y;
			temp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(temp);
			
			x = (int) temp.x;
			y = (int) temp.y;
			
			if(addRect) {
				rectSideObjects.add(new Rect(x,y,tileWidth,tileHeight));
				rectSideNumArray = rectSideObjects.size();
				//addRect = false;
				System.out.println(rectSideNumArray);
			}
			calculateEdges(rectSideObjects);
			System.out.println(rectObjects.size());
			
		}
		
		if(shoot) {
			spears.add(shootSpear());
			shoot = false;
		}
		timeCount += delta;
		if(timeCount > timeMax) {
			movingBlocks.add(spawnMovingBlock());
			//System.out.println(movingBlocks.size());
			timeCount = 0;
		}
		
		int size = movingBlocks.size();
	}
	
	private Spear shootSpear() {
		Spear spear = new Spear(player.sprite.getX(),player.sprite.getY()+(player.sprite.getHeight()));
		spear.sprite.setPosition(player.sprite.getX(), player.sprite.getY());
		spear.sprite.setSize(7, 0.1f);
		spear.velocity.x = 1.8f;
		return spear;
	}
	
	private MovingBlock spawnMovingBlock() {
		MovingBlock movingBlock = new MovingBlock();
		movingBlock.isAlive = true;
		movingBlock.position.set(95f, 35f);
		movingBlock.velocity.set(-0.35f, 0f);
		return movingBlock;
		
	}
	
	
	private boolean isLeftRectColliding() {
		for(int i = 0;i < rectSideObjects.size();i++) {
			if(Intersector.overlaps(rectSideObjects.get(i).rect,player.leftRect)) {
				if(isBottomRectColliding()) {
					player.position.x = rectSideObjects.get(i).x + rectSideObjects.get(i).width;
				} else {
					player.position.x = rectSideObjects.get(i).x + (rectSideObjects.get(i).width-0.05f);
				}
				player.velocity.x = 0;
				player.velocity.y = 0;
				return true;
			}
		}
		return false;
	}
	
	private boolean isRightRectColliding() {
		for(int i = 0;i < rectSideObjects.size();i++) {
			if(Intersector.overlaps(rectSideObjects.get(i).rect,player.rightRect)) {
				if(isBottomRectColliding()) {
					player.position.x = rectSideObjects.get(i).x - rectSideObjects.get(i).width;
				} else {
					player.position.x = rectSideObjects.get(i).x - (rectSideObjects.get(i).width-0.05f);
				}
				player.velocity.x = 0;
				player.velocity.y = 0;
				return true;
			}
		}
		return false;
	}
	
	public void updateSideRectCollision(float delta) {
		if(isLeftRectColliding()) {
			if(jump) {
				if(!isBottomRectColliding()) {
					player.velocity.x = 0.3f;
					player.velocity.y = 0.85f;
				}
			}
			jump = false;
		}
		
		if(isRightRectColliding()) {
			if(jump) {
				if(!isBottomRectColliding()) {
					player.velocity.x = -0.3f;
					player.velocity.y = 0.85f;
				}
			}
			jump = false;
		}
	}
	
	public boolean isBottomRectColliding() {
		if(isBottomRectCollidingWithGroundCollider()) {
			//return false;
		}
		for(int i = 0;i < rectObjects.size();i++) {
			
			if(Intersector.overlaps(rectObjects.get(i).rect,player.bottomRect)) {
				player.position.y = rectObjects.get(i).y + rectObjects.get(i).height;
				player.velocity.y = 0;
				return true;
			}
		}
		return false;
	}
	
	private boolean isBottomRectCollidingWithSideRect() {
		for(int i = 0;i < rectSideObjects.size();i++) {
			if(Intersector.overlaps(rectSideObjects.get(i).rect, player.bottomRect)) {
				player.position.y = rectSideObjects.get(i).y + rectSideObjects.get(i).height;
				player.velocity.y = 0;
				return true;
			}
		}
		return false;
	}
	
	private boolean isBottomRectCollidingWithGroundCollider() {
		if(Intersector.intersectRectangles(player.bottomRect, gc.rect, gc.rect)) {
			System.out.println("Colliding");
			return true;
		}
		return false;
	}
	
	
	private boolean isSideRectSpearColliding(int sideRectIndex,int spearIndex) {
		if(Intersector.intersectRectangles(rectSideObjects.get(sideRectIndex).rect, spears.get(spearIndex).spearRec, spears.get(spearIndex).spearRec)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX,int screenY,int pointer,int button) {
		
		int x,y;
		temp.set(screenX, screenY, 0);
		camera.unproject(temp);
		
		x = (int) temp.x;
		y = (int) temp.y;
		
		if(button == 0 && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			levelTiles.add(new LevelTile(x,y));
			System.out.println("Added: " + levelTiles.size());
		}	
		
		///remove tile objects when right clicked start code
		///remove textures
		if(button == 1) {
			for(int i = 0;i < levelTiles.size()-1;i++) {
				if(x == levelTiles.get(i).x && y == levelTiles.get(i).y) {
					levelTiles.remove(i);
				}
			}
			
			System.out.println("Removed: " + levelTiles.size());
			if(x == levelTiles.get(levelTiles.size()-1).x && y == levelTiles.get(levelTiles.size()-1).y) {
				System.out.println("Last Object can't be removed");
			}
		}
		
		////remove ground coliiders when left clicked
		if(button == 0) {
			for(int i = 0;i < rectObjects.size()-1;i++) {
				if(x == rectObjects.get(i).x && y == rectObjects.get(i).y) {
					rectObjects.remove(i);	
					rectNumArray = rectObjects.size();
				}
			}	
		}
		
	////remove side coliiders when left clicked
		if(button == 2) {
			for(int i = 0;i < rectSideObjects.size()-1;i++) {
				if(x == rectSideObjects.get(i).x && y == rectSideObjects.get(i).y) {
					rectSideObjects.remove(i);	
					rectSideNumArray = rectSideObjects.size();
				}
			}
		}
		
		///remove objects when clicked end code
		
		rectNumArray = rectObjects.size();
		
		for(int i = 0;i < levelTiles.size()-1;i++) {
			levelTiles.get(i).numArray = levelTiles.size();
		}	
		
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX,int screenY,int pointer,int button) {
		int x,y;
		
		temp.set(screenX, screenY, 0);
		camera.unproject(temp);
		
		x = (int) temp.x;
		y = (int) temp.y;
		
		return false;
	}
	
	@Override
	public boolean keyDown(int key) {
		if(key == Keys.S) {
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				//SaveSystem.saveLevel(levelTiles);
			}
		} else if(key == Keys.R) {
			//SaveSystem.reloadLevel(levelTiles);
		} 
		
		if(key == Keys.S) {
			if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
				SaveSystem.saveColliders(rectObjects,this.rectNumArray);
			}
		} else if(key == Keys.D) {
			SaveSystem.reloadColliders(rectObjects);
		}
		
		if(key == Keys.S) {
			if(Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) {
				SaveSystem.saveSideColliders(rectSideObjects,this.rectSideNumArray);
			}
		} else if(key == Keys.F) {
			SaveSystem.reloadSideColliders(rectSideObjects);
		} 
		
		//Player move controls
		if(key == Keys.LEFT) {
			//if(isBottomRectColliding() || isBottomRectCollidingWithSideRect()) {
			player.velocity.x = -0.25f;
			leftPressed = true;
			//}
		} else if(key == Keys.RIGHT) {
			//if(isBottomRectColliding() || isBottomRectCollidingWithSideRect()) {
			player.velocity.x = 0.25f;
			rightPressed = true;
			//}
		}
		
		if(key == Keys.S) {
			shoot = true;
		}
		
		if(key == Keys.SPACE) {
			jump = true;
		}
		
		if(key == Keys.C) {
			Cell.visible = true;
		} else if(key == Keys.V){
			Cell.visible = false;
		}
		return false;
	}
	
	@Override
	public boolean keyUp(int key) {
		
		//Player move controls
		if(key == Keys.LEFT || key == Keys.RIGHT) {
			if(isBottomRectColliding()) {
				player.velocity.x = 0;
			}
		} 
		
		if(key == Keys.S) {
			//shoot = false;
		}
		if(key == Keys.SPACE) {
			//jump = false;
		}
		
		if(key == Keys.LEFT)
		{
			leftPressed = false;
		}
		
		if(key == Keys.RIGHT)
		{
			rightPressed = false;
		}
		return false;
	}
	
	public static class Cell {
		public int x,y,width,height;
		public static boolean visible = true;
		
		public Cell(int x,int y,int width,int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public void renderCell(ShapeRenderer shape,Color color) {
			if(visible) {
				shape.rect(x, y, width, height, 0, 0, 0);
			} else if(!visible) {
				shape.rect(0, 0, 0, 0, 0, 0, 0);
			}
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
	}
	

}
