package main;

import java.util.ArrayList;

public class Path {
	private ArrayList<Node> nodes;
	int id;
	
	public Path(int id) {
		nodes=new ArrayList<Node>();
		this.id = id;
	}
	
	public void add(Node node) {
		nodes.add(node);
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
	
	public Path copy() {
		Path path=new Path(this.id);
		for (int i=0;i<nodes.size()-1;i++) {
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
}
