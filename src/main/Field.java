package main;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import igeo.IVec;

public class Field {
	// private Node[][] nodes;
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	
	//vars
	private double tolerance=0.001d;

	private IVec origin = new IVec(0, 0, 0);

	public Field() {

	}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        

	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public ArrayList<Edge> getEdges() {
		return edges;
	} 
	
	public void buildField(Node[] sources,Node[] ends,double[][] weights) {
		for (int i=0;i<sources.length;i++) {
			sources[i]=addNode(sources[i]);
		}
		for (int i=0;i<ends.length;i++) {
			ends[i]=addNode(ends[i]);
		}
		
		Stack<Edge> stack=new Stack<Edge>();
		
		// push 12 edges of each box into the stack
		for (int i=0;i<sources.length;i++) {
			for (int j=0;j<ends.length;j++) {
				if (weights[i][j]>0) {
					
					IVec p1=sources[i].pos();
					double x1=p1.x();
					double y1=p1.y();
					double z1=p1.z();
					
					IVec p2=ends[j].pos();
					double x2=p2.x();
					double y2=p2.y();
					double z2=p2.z();
					
					Node a=sources[i]; //addNode(new Node(new IVec(x1,y1,z1)));
					Node b=addNode(new Node(new IVec(x1,y1,z2)));
					Node c=addNode(new Node(new IVec(x1,y2,z1)));
					Node d=addNode(new Node(new IVec(x1,y2,z2)));
					Node e=addNode(new Node(new IVec(x2,y1,z1)));
					Node f=addNode(new Node(new IVec(x2,y1,z2)));
					Node g=addNode(new Node(new IVec(x2,y2,z1)));
					Node h=ends[j]; //addNode(new Node(new IVec(x2,y2,z2)));
					
					stack.push(new Edge(a,b));
					stack.push(new Edge(a,c));
					stack.push(new Edge(a,e));
					stack.push(new Edge(b,d));
					stack.push(new Edge(b,f));
					stack.push(new Edge(c,d));
					stack.push(new Edge(c,g));
					stack.push(new Edge(d,h));
					stack.push(new Edge(e,f));
					stack.push(new Edge(e,g));
					stack.push(new Edge(f,h));
					stack.push(new Edge(g,h));
				}
			}
		}
		
		//merge duplicate edges in the stack
		while (!stack.isEmpty()) {
			Edge edge=stack.pop();
			ArrayList<Node> nodesOnEdge=new ArrayList<Node>();
			for (Node node:nodes) {
				if (node.equals(edge.source()))
					continue;
				if (node.equals(edge.end()))
					continue;
				if (node.isOnEdge(edge)) {
					node.shadow=node.shadow(edge);
					nodesOnEdge.add(node);
				}
			}
			if (nodesOnEdge.size()==0) {
				addDoubleEdge(edge.source(),edge.end());
			}else {
				Comparator<Node> c = new Comparator<Node>() {
					@Override
					public int compare(Node o1, Node o2) {
						// TODO Auto-generated method stub
						if(o1.shadow<o2.shadow)
							return 1;
						else return -1;
					}
				};
				Collections.sort(nodesOnEdge, c);
				addDoubleEdge(edge.source(),nodesOnEdge.get(0));
				addDoubleEdge(nodesOnEdge.get(nodesOnEdge.size()-1),edge.end());
				for (int i=0;i<nodesOnEdge.size()-1;i++) {
					addDoubleEdge(nodesOnEdge.get(i),nodesOnEdge.get(i+1));
				}
				
			}
			

		}
		
		// add each edge to this.edges
		for (Node node:nodes) {
			edges.addAll(node.neighborEdges());
		}
		// find pair edge
		for (Edge edge:edges) {
			edge.pair=edge.findPair();
		}
	}
	
	private void addDoubleEdge(Node node1,Node node2) {
		node1.addNeighbor(node2);
		node2.addNeighbor(node1);
	}
	
	private Node addNode(Node newNode) {
		for (Node node:nodes) {
			if (node.dist(newNode)<this.tolerance) {
				if (node.hasId()) {
					node.addAlias(newNode.id());
				}
				return node;
			}
		}
		nodes.add(newNode);
		return newNode;
	}


	public ArrayList<Node> findShortestPath(Node source, Node end) {
		SPFA(source);
		if (end.spfa_Next==null) {
			return null;
		}
		ArrayList<Node> path = new ArrayList<Node>();
		Node pos = end;
		path.add(pos);
		while (!pos.equals(source)) {
			pos = pos.spfa_Next;
			path.add(pos);
		}
		//reverse the path
		for(int i=0;i<path.size()/2-1;i++) {
			Node tem=path.get(i);
			path.set(i, path.get(path.size()-1-i));
			path.set(path.size()-1-i, tem);
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
	
	private ArrayList<Node> dedupe(ArrayList<Node> list){
		ArrayList<Node> newList=new ArrayList<Node>();
		for (Node iNode:list) {
			boolean found=false;
			for (Node jNode:newList) {
				if (iNode.equals(jNode)) {
					found=true;
					break;
				}
			}
			if (!found)
				newList.add(iNode);
		}
		return newList;
	}
	
	
	/**
	 * @param source
	 * @param end
	 * @return a list contains all the shortest path from source to end.
	 * source and end should not be null and should be different.
	 */
	public ArrayList<Path> findAllShortestPath(Node source, Node end,double weight) {
		SPFA2(source);
		ArrayList<Path> paths=new ArrayList<Path>();
		for (Node node:nodes) {
//			System.out.print(node.spfa_Next_List.size());
			node.spfa_Next_List=this.dedupe(node.spfa_Next_List);
//			System.out.println(" "+node.spfa_Next_List.size());
		}
		Stack<Path> stack=new Stack<Path>();
		for (Node node:end.spfa_Next_List) {
			Path path=new Path();
			path.add(end);
			path.add(node);
			stack.push(path);
		}
		while (!stack.isEmpty()) {
			Path path=stack.pop();
			for (Node node:path.end().spfa_Next_List) {
				Path newPath=path.copy();
				newPath.add(node);
				if (node.equals(source)) {
					paths.add(newPath);
				}else {
					stack.push(newPath);
				}
			}
		}
		for (Path path:paths) {
			path.weight=weight/paths.size();
		}
		return paths;
	}
	
	private void SPFA2(Node source) {
		for (Node node : nodes) {
			node.spfa_Dist = Double.MAX_VALUE;
			node.spfa_Next_List = new ArrayList<Node>();
		}

		Queue<Node> que = new LinkedList<Node>();
		source.spfa_Dist = 0;
		que.offer(source);
		while (!que.isEmpty()) {
			Node node = que.poll();
			for (Edge edge : node.neighborEdges()) {
				Node end=edge.end();
				// cal dist
				double dist = edge.weight() + node.spfa_Dist + end.resistence;
				if (Math.abs(dist-end.spfa_Dist)<Constant.tolerance) {
					end.spfa_Next_List.add(node);
				}
				else if (dist < end.spfa_Dist) {
					end.spfa_Dist = dist;
					que.add(end);
					end.spfa_Next_List = new ArrayList<Node>();
					end.spfa_Next_List.add(node);
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
