package main;

import java.util.ArrayList;

import igeo.IVec;

public class Path {
	private ArrayList<Node> nodes;
	
	// for optimize
	public int id;
	public double weight;
	public boolean isDisplay;
	
	// method for path
	public Path() {
		nodes=new ArrayList<Node>();
	}
	
	public void add(Node node) {
		nodes.add(node);
	}
	
	public ArrayList<Node> nodes(){
		return this.nodes;
	}
	
	public Node source() {
		if (nodes.size()>0)
			return nodes.get(0);
		else
			return null;
	}
	
	public Node end() {
		if (nodes.size()>0)
			return nodes.get(nodes.size()-1);
		else
			return null;
	}
	
	public void reverse() {
		for(int i=0;i<nodes.size()/2;i++) {
			Node tem=nodes.get(i);
			nodes.set(i, nodes.get(nodes.size()-1-i));
			nodes.set(nodes.size()-1-i, tem);
		}
	}
	
	public Node pre(Node node) {
		for (int i=1;i<nodes.size();i++) {
			if (nodes.get(i).equals(node))
				return nodes.get(i-1);
		}
		return null;
	}
	
	public Node next(Node node) {
		for (int i=0;i<nodes.size()-1;i++) {
			if (nodes.get(i).equals(node))
				return nodes.get(i+1);
		}
		return null;
	}
	
	public Path copy() {
		Path path=new Path();
		for (int i=0;i<nodes.size();i++) {
			path.add(nodes.get(i));
		}
		return path;
	}
	
	public String toString() {
		String text="";
		for (int i=0;i<nodes.size();i++) {
			text+=nodes.get(i).guid+" ";
		}
		return String.format(
				"path from %s to %s:%s",
				this.source(),
				this.end(),
				text
				);
	}

	public int numOfPoints() {
		return nodes.size();
	}
	
	public int numOfTurns() {
		int sum=0;
		for (int i=0;i<nodes.size()-2;i++) {
			IVec a=nodes.get(i).pos();
			IVec b=nodes.get(i+1).pos();
			IVec c=nodes.get(i+2).pos();
			if (b.dif(a).cross(c.dif(a)).len()>Constant.tolerance)
				++sum;
		}
		return sum;
	}

	public double cost() {
		double sum = 0;
		for (int i = 0; i < this.nodes.size() - 1; i++) {
			Node now = this.nodes.get(i);
			Node next = this.nodes.get(i + 1);
			sum += now.findEdge(next).weight();
			if (i < this.nodes.size() - 2) {
				if (onSurface(next))
					sum += next.temResistence;
			}
		}
		return sum * this.weight;
	}

	private boolean onSurface(Node node) {
		Node pre = pre(node);
		if (pre == null)
			return false;
		Node next = next(node);
		if (next == null)
			return false;
		double z1 = pre.pos().z();
		double z2 = node.pos().z();
		double z3 = next.pos().z();
		return (Math.abs(z1 - z2) < Constant.tolerance && Math.abs(z2 - z3) < Constant.tolerance);
	}
}
