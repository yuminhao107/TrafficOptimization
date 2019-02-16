package main;

public class Edge {
	private Node source, end;
	public Edge pair;
	// for spfa
	private double weight;

	// for optimize
	public double flow = 0d;
	
	// for display
	public boolean showed;

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
	
	public Edge findPair() {
		for (Edge edge:end.neighborEdges()) {
			if (edge.end().equals(source)) {
				return edge;
			}
		}
		System.out.println("Error. A edge has no pair edge.");
		return null;
	}
}
