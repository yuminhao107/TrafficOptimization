package main;

import java.util.ArrayList;

import igeo.IVec;

public class Node {
	// Attributes for Node
	private String id;
	private ArrayList<Edge> neighborEdges = new ArrayList<Edge>();
	private IVec position;

	// Attributes for spfa
	public double spfa_Dist;
	public Node spfa_Next;

	// Method for Node
	public Node(String id) {
		this.id = id;
	}

	public Node(String id, double x, double y, double z) {
		this(id);
		this.position = new IVec(x, y, z);
	}

	public Node(String id, IVec pos) {
		this(id);
		this.position = pos;
	}

	public String id() {
		return id;
	}

	public IVec pos() {
		return this.position;
	}

	public void addNeighbor(Node node) {
		neighborEdges.add(new Edge(this, node, this.pos().dist(node.pos())));
	}

	public void addNeighborEdge(Edge edge) {
		neighborEdges.add(edge);
	}

	public ArrayList<Node> neighbors() {
		ArrayList<Node> neighbors = new ArrayList<Node>();
		for (Edge edge : this.neighborEdges) {
			neighbors.add(edge.end());
		}
		return neighbors;
	}

	public ArrayList<Edge> neighborEdges() {
		return this.neighborEdges;
	}
	
	public String toString() {
		return this.id()+" "+this.pos().toString();
	}

}
