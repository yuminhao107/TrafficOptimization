package main;

import java.util.ArrayList;

import igeo.IBox;
import igeo.IG;
import igeo.IVec;
import processing.core.PApplet;

public class TestIGeo extends PApplet {
	public void setup() {
//		size(800, 600, IG.GL);
//		IBox box = new IBox(0, 0, 0, 100, 100, 100);
//		box.hsb(0.5, 1, 1);
//		
//		IVec a=new IVec(1,1,1);
//		IVec b=new IVec(1,1,1);
//		IVec c=a.dif(b);
//		b.len(10d);
//		println(a.cross(b).len());
//		println(a.dot(b));
//		println(c);
		ArrayList<String> strs=new ArrayList<String>();
		strs.add("abc");
		strs.add("bcd");
		IVec a=new IVec(0,0,0);
		IVec b=new IVec(1,0,0);
		IVec c=new IVec(0,1,0);
//		System.out.println(this.isNotClockWise(a, b, c));
		ArrayList<String> strs2=(ArrayList<String>) strs.clone();
		strs2.set(1, "cde");
		System.out.println(strs.get(1));
		System.out.println(strs2.get(1));
		
		Node n1=new Node("a",0,0,0);
		Node n2=new Node("b",1,0,0);
		Node n3=new Node("c",0,1,0);
		Node center=new Node("center",0.2,0.2,0);
		Node[] nodes= {n1,n3,n2};
		clockWiseSort(nodes,center);
		System.out.println(" "+nodes[0]+nodes[1]+nodes[2]);
		System.out.println(this.resistence(10, 7, 1, 2, 6));
	}

	public void draw() {

	}
	
	//if p3> p2 return true
	boolean compare(IVec p1, IVec p2, IVec p3) {
		double tem=p2.dif(p1).cross(p3.dif(p1)).z();
		if (tem>0) return true;
		if (tem<0) return false;
		return p3.dist(p1)>p2.dist(p1);
	}
	
	private void clockWiseSort(Node[] nodes,Node center) {
		int len=nodes.length;
		for (int i = 0; i < len - 1; i++)
			for (int j = 0; j < len - 1 - i; j++)
				if (compare(center.pos(),nodes[j+1].pos(),nodes[j].pos())) {
					Node tem=nodes[j];
					nodes[j]=nodes[j+1];
					nodes[j+1]=tem;
				}
	}
	
	public double resistence(int max,int a1,int a2,int b1,int b2) {
		if (a1 == b1)
			return Constant.divideResistance;
		if (a2==b2)
			return Constant.mergeResistance;
		if (a1==-1) return 0d;
		if (a2==-1) return 0d;
		if (b1==-1) return 0d;
		if (b2==-1) return 0d;
		a2=(a2-a1+max)%max;
		b1=(b1-a1+max)%max;
		b2=(b2-a1+max)%max;
		a1=0;
		if (b1<a2 && (a2<b2 || b2==a1))
			return Constant.crossResistance;
		if (b1>=a2 && a1<b2 && b2<a2 )
			return Constant.crossResistance;
		return 0d;
	}
}
