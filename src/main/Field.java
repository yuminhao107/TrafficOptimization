package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import igeo.IVec;

public class Field {
	// private Node[][] nodes;
	private ArrayList<Node> nodes = new ArrayList<Node>();

	private IVec origin = new IVec(0, 0, 0);

	public Field() {

	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	private ArrayList<Node> findShortestPath(Node source, Node end) {
		SPFA(source);
		ArrayList<Node> path = new ArrayList<Node>();
		Node pos = end;
		path.add(pos);
		while (pos != source) {
			pos = pos.spfa_Next;
			path.add(pos);
		}
		return path;
	}

	private void SPFA(Node source) {
		for (Node node : nodes) {
			node.spfa_Dist = Double.MAX_VALUE;
			node.spfa_Next = null;
		}

		Queue<Node> que = new LinkedList<Node>();
		source.spfa_Dist = 0;
		que.offer(source);
		while (!que.isEmpty()) {
			Node node = que.poll();
			for (Edge edge : node.neighborEdges()) {
				double dist = edge.weight() + node.spfa_Dist;
				if (dist < edge.end().spfa_Dist) {
					edge.end().spfa_Dist = dist;
					que.add(edge.end());
					edge.end().spfa_Next = node;
				}
			}
		}
	}

	public void setOrigin(IVec origin) {
		this.origin = origin.cp();
	}

	public Node nearestNode(IVec pos) {
		Node target = null;
		double min = Double.MAX_VALUE;
		for (Node node : nodes) {
			double dist = node.pos().dist(pos);
			if (dist < min) {
				min = dist;
				target = node;
			}
		}
		return target;
	}

	private boolean isClockWise(IVec p1, IVec p2, IVec p3) {
		return p2.dif(p1).cross(p3.dif(p1)).z() > 0d;
	}

}
