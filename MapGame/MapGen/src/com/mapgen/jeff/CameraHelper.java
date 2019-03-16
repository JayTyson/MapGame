package com.mapgen.jeff;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {

    public Vector2 position;
    private Sprite sprite;
    private float zoom;
    private float angle;

    public CameraHelper() {
        position = new Vector2();
        zoom = 1f;
    }

    public void update(Vector2 velocity) {
    	if(sprite.getX() < 10) {
        	position.y = 2f;
        	//position.x = 5.f;
        } 
        if(sprite.getX() < 8 || !hasTarget()) {
            return;
        }
        
        
        position.x = sprite.getX() + (position.x - sprite.getX()) * 0.9f;
        position.y = sprite.getY() + (position.y - sprite.getY()) * 0.6f;
        
        if(velocity.x < 0 && velocity.x != 0) {
        	position.x -= 0.8f;
        } else if(velocity.x > 0 && velocity.x != 0){
        	position.x += 0.8f;
        }
        position.y += 0.4f;
    }

    public void shakeCamera(OrthographicCamera camera) {
        camera.translate(2 * 0.5f * MathUtils.random(-0.14f,0.14f),2 * 0.5f * MathUtils.random(-0.14f,0.14f));
        camera.rotate(2 * 0.5f * MathUtils.random(-0.025f,0.025f));
        camera.update();
    }

    public void setTarget(Sprite sprite) {
        this.sprite = sprite;
    }

    public boolean hasTarget() {
        return sprite != null;
    }

    public boolean hasTarget(Sprite sprite) {
        return hasTarget() && this.sprite.equals(sprite);
    }

    public void addZoom(float value) {
        this.zoom += value;

        //System.out.println("Zoom: " + this.zoom);
        //System.out.println("Zoom Value: " + value);
    }
    
    public void setZoom(float value) {
        this.zoom = value;

        //System.out.println("Zoom: " + this.zoom);
        //System.out.println("Zoom Value: " + value);
    }

    public void resetZoom(float value) {
        this.zoom = value;
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.x = this.position.x;
        camera.position.y = this.position.y;
        //camera.position.y = (camera.viewportHeight/2)+0.3f;
        camera.zoom = this.zoom;
        camera.update();
    }

}
