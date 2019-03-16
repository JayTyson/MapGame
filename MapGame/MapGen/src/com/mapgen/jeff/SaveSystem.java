package com.mapgen.jeff;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SaveSystem {
	
	public static void saveSideColliders(ArrayList<Rect> rectSideObjects,int rectSideNumArray) {
		if(rectSideObjects.size() <= 0) {
			System.out.println("Sorry you did not put any side colliders");
			return;
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("sideCollidersX.col");
			DataOutputStream dos = new DataOutputStream(fos);
			for(int i = 0;i < rectSideObjects.size();i++) {
				dos.writeInt(rectSideObjects.get(i).x);
			}
			dos.close();
			dos = null;
			
			FileOutputStream fos1 = new FileOutputStream("sideCollidersY.col");
			DataOutputStream dos1 = new DataOutputStream(fos1);
			for(int i = 0;i < rectSideObjects.size();i++) {
				dos1.writeInt(rectSideObjects.get(i).y);
			}
			dos1.close();
			dos1 = null;
			
			FileOutputStream fos2c = new FileOutputStream("sideCollidersW.col");
			DataOutputStream dos2c = new DataOutputStream(fos2c);
			for(int i = 0;i < rectSideObjects.size();i++) {
				dos2c.writeInt(rectSideObjects.get(i).width);
			}
			dos2c.close();
			dos2c = null;
			
			FileOutputStream fos3 = new FileOutputStream("sideCollidersH.col");
			DataOutputStream dos3 = new DataOutputStream(fos3);
			for(int i = 0;i < rectSideObjects.size();i++) {
				dos3.writeInt(rectSideObjects.get(i).height);
			}
			
			dos3.close();
			dos3 = null;
			
			FileOutputStream fos2 = new FileOutputStream("side-num-array.num");
			DataOutputStream dos2 = new DataOutputStream(fos2);
			dos2.writeInt(rectSideNumArray);
			dos2.close();
			dos2 = null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println("Wall colliders saved!!!");
	}
	
	public static void reloadSideColliders(ArrayList<Rect> rectSideObjects) {
			
		try {
			FileInputStream fos = new FileInputStream("sideCollidersX.col");
			DataInputStream dos = new DataInputStream(fos);
			
			FileInputStream fos1 = new FileInputStream("sideCollidersY.col");
			DataInputStream dos1 = new DataInputStream(fos1);
			
			FileInputStream fos2c = new FileInputStream("sideCollidersW.col");
			DataInputStream dos2c = new DataInputStream(fos2c);
			
			
			FileInputStream fos3 = new FileInputStream("sideCollidersH.col");
			DataInputStream dos3 = new DataInputStream(fos3);
			
			
			FileInputStream fos2 = new FileInputStream("side-num-array.num");
			DataInputStream dos2 = new DataInputStream(fos2);
			
			int numArray = dos2.readInt();
			for(int i = 0;i < numArray;i++) {
				rectSideObjects.add(new Rect(dos.readInt(),dos1.readInt(),dos2c.readInt(),dos3.readInt()));
				//System.out.println(dos.readInt() + " " + dos1.readInt() + " " +dos2c.readInt()+ " " +dos3.readInt());
			}
			
			
			
			//System.out.println("Yeah Colliders restored");
			//System.out.println(rectObjects.size());
			dos.close();
			dos1.close();
			dos2.close();
			dos2c.close();
			dos3.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void saveColliders(ArrayList<Rect> rectObjects,int rectNumArray) {
		if(rectObjects.size() <= 0) {
			System.out.println("Sorry you did not put any colliders");
			return;
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("collidersX.col");
			DataOutputStream dos = new DataOutputStream(fos);
			for(int i = 0;i < rectObjects.size();i++) {
				dos.writeInt(rectObjects.get(i).x);
			}
			dos.close();
			
			FileOutputStream fos1 = new FileOutputStream("collidersY.col");
			DataOutputStream dos1 = new DataOutputStream(fos1);
			for(int i = 0;i < rectObjects.size();i++) {
				dos1.writeInt(rectObjects.get(i).y);
			}
			dos1.close();
			
			FileOutputStream fos2c = new FileOutputStream("collidersW.col");
			DataOutputStream dos2c = new DataOutputStream(fos2c);
			for(int i = 0;i < rectObjects.size();i++) {
				dos2c.writeInt(rectObjects.get(i).width);
			}
			dos2c.close();
			
			FileOutputStream fos3 = new FileOutputStream("collidersH.col");
			DataOutputStream dos3 = new DataOutputStream(fos3);
			for(int i = 0;i < rectObjects.size();i++) {
				dos3.writeInt(rectObjects.get(i).height);
			}
			
			dos3.close();
			
			FileOutputStream fos2 = new FileOutputStream("num-array.num");
			DataOutputStream dos2 = new DataOutputStream(fos2);
			dos2.writeInt(rectNumArray);
			dos2.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println("Ground colliders saved!!!");
	}
	
	public static void reloadColliders(ArrayList<Rect> rectObjects) {
		
		try {
			FileInputStream fos = new FileInputStream("collidersX.col");
			DataInputStream dos = new DataInputStream(fos);
			
			FileInputStream fos1 = new FileInputStream("collidersY.col");
			DataInputStream dos1 = new DataInputStream(fos1);
			
			FileInputStream fos2c = new FileInputStream("collidersW.col");
			DataInputStream dos2c = new DataInputStream(fos2c);
			
			
			FileInputStream fos3 = new FileInputStream("collidersH.col");
			DataInputStream dos3 = new DataInputStream(fos3);
			
			
			FileInputStream fos2 = new FileInputStream("num-array.num");
			DataInputStream dos2 = new DataInputStream(fos2);
			
			int numArray = dos2.readInt();
			for(int i = 0;i < numArray;i++) {
				rectObjects.add(new Rect(dos.readInt(),dos1.readInt(),dos2c.readInt(),dos3.readInt()));
			}
			
			
			
			//System.out.println("Yeah Colliders restored");
			//System.out.println(rectObjects.size());
			dos.close();
			dos1.close();
			dos2.close();
			dos2c.close();
			dos3.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void saveLevel(ArrayList<LevelTile> levelTiles) {
		if(levelTiles.size() <= 0) {
			System.out.println("Sorry cannot save empty level");
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream("level.lvl");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			
			for(int i = 0;i < levelTiles.size();i++) {
				oos.writeObject(levelTiles.get(i));
			}
			System.out.println("Yeah level saved");
			System.out.println(levelTiles.size());
			
			oos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void reloadLevel(ArrayList<LevelTile> levelTiles) {
		try {
			//FileHandle f = Gdx.files.internal(levelName);
			//f.path;
			FileInputStream fis = new FileInputStream("level.lvl");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			LevelTile go = (LevelTile)ois.readObject();
			int numOfArray = go.numArray;
			levelTiles.add(go);
			
			for(int i = 0;i < numOfArray-1;i++) {
				levelTiles.add((LevelTile)ois.readObject());
			}
			//System.out.println(gameObjects.size());

			ois.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
