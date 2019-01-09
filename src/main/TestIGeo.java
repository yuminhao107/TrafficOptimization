package main;

import igeo.IBox;
import igeo.IG;
import processing.core.PApplet;

public class TestIGeo extends PApplet {
	public void setup() {
		size(800, 600, IG.GL);
		IBox box = new IBox(0, 0, 0, 100, 100, 100);
		box.hsb(0.5, 1, 1);
	}

	public void draw() {

	}
}
