package com.mapgen.jeff;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MapGen";
		cfg.useGL20 = true;
		cfg.width = 800;//LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		cfg.height = 480;//LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		//cfg.fullscreen = true;
		
		//System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		new LwjglApplication(new MapGen(), cfg);
	}
}