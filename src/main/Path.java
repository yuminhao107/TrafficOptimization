package main;

import java.util.ArrayList;

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
		for(int i=0;i<nodes.size()/2-1;i++) {
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
		return String.format(
				"path from %s to %s",
				this.source(),
				this.end()
				);
	}

	public int numOfPoints() {
		return nodes.size();
	}
}
