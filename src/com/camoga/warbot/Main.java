package com.camoga.warbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

public class Main {
	
	static BizkaiaWar war;
	
	public Main() throws Exception {
		war = new BizkaiaWar();
//		war.setupTwitter();
//		war.status( null);
		boolean load = loadData();
		System.out.println(load);
		war.start(load);
		saveData();
	}
	
	public static void main(String[] args) throws Exception {
		try {
			new Main();
		} catch(Exception e) {
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			war.tw.sendDirectMessage(854781980L, w.toString());
			e.printStackTrace();
		}
	}
	
	public boolean loadData() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream("C:/Users/usuario/Desktop/JAVA/warbot/SAVE.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			war.possessions = (HashMap) ois.readObject();
			war.iteration = ois.readInt();
			war.randsteps = ois.readInt();
			ois.close();
			fis.close();
		} catch(FileNotFoundException e) {
			return false;
		}
		return true;
	}
	
	public void saveData() throws IOException {
		FileOutputStream fos = new FileOutputStream("C:/Users/usuario/Desktop/JAVA/warbot/SAVE.dat");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(war.possessions);
		oos.writeInt(war.iteration);
		oos.writeInt(war.randsteps);
		oos.close();
		fos.close();
		System.out.println();
		FileUtils.copyFile(
				new File("C:/Users/usuario/Desktop/JAVA/warbot/SAVE.dat"), 
				new File("C:/Users/usuario/Desktop/JAVA/warbot/SAVE"+war.iteration+".dat"));
	}
}
