package main;

import igeo.IBox;
import igeo.IG;
import igeo.IVec;
import processing.core.PApplet;

public class TestIGeo extends PApplet {
	public void setup() {
//		size(800, 600, IG.GL);
		IBox box = new IBox(0, 0, 0, 100, 100, 100);
		box.hsb(0.5, 1, 1);
		
		IVec a=new IVec(1,1,1);
		IVec b=new IVec(1,1,1);
//		IVec c=a.dif(b);
//		b.len(10d);
		println(a.cross(b).len());
		println(a.dot(b));
//		println(c);
	}

	public void draw() {

	}
}
