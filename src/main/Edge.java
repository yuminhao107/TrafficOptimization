package main;

public class Edge {
	private Node source, end;
	private double weight;

	public Edge(Node source, Node end) {
		this.source = source;
		this.end = end;
	}

	public Edge(Node source, Node end, double weight) {
		this.source = source;
		this.end = end;
		this.weight = weight;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public void setEnd(Node end) {
		this.end = end;
	}

	public Node source() {
		return this.source;
	}

	public Node end() {
		return this.end;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double weight() {
		return this.weight;
	}
}
