package main;

import java.util.ArrayList;

import igeo.IVec;

public class Node {
	// Attributes for Node
	private String id=null;
	private ArrayList<String> alias=new ArrayList<String>();
	private ArrayList<Edge> neighborEdges = new ArrayList<Edge>();
	private IVec position;

	// Attributes for spfa
	public double spfa_Dist;
	public Node spfa_Next;
	
	// Attributes for buildField
	public double shadow=0d;

	// Method for Node
	public Node(String id) {
		this.id = id;
	}

	public Node(String id, double x, double y, double z) {
		this.id = id;
		this.position = new IVec(x, y, z);
	}

	public Node(String id, IVec pos) {
		this.id = id;
		this.position = pos;
	}
	
	public Node(IVec pos) {
		this.position = pos;
	}

	public String id() {
		return id;
	}
	
	public boolean hasId() {
		return this.id!=null;
	}

	public IVec pos() {
		return this.position;
	}

	public void addNeighbor(Node node) {
		for (Edge edge:this.neighborEdges) {
			if (edge.end().equals(node))
				return;
		}
		neighborEdges.add(new Edge(this, node, this.pos().dist(node.pos())));
	}
	
	public void delNeighbor(Node node) {
		for (int i=0;i<neighborEdges.size();i++) {
			if (neighborEdges.get(i).end().equals(node)) {
				neighborEdges.remove(i);
				break;
			}
		}
	}  

//	public void addNeighborEdge(Edge edge) {
//		neighborEdges.add(edge);
//	}

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
		String name=this.id();
		for (String str:alias) {
			name+="-"+str;
		}
		return name+" "+this.pos().toString();
	}
	
	public double dist(Node node) {
		return this.pos().dist(node.pos());
	}
	
	public double dist(Edge edge) {
		IVec p=this.pos();
		IVec a=edge.source().pos();
		IVec b=edge.end().pos();
		double tem=p.dif(a).dot(b.dif(a));
		if (tem<0)
			return p.dist(a);
		double dist=a.dist(b);
		double sq=Math.pow(dist,2);
		if (tem>sq)
			return p.dist(b);
		return b.dif(a).len(tem/dist).sum(a).dist(p);
	}
	
	public double shadow(Edge edge) {
		IVec p=this.pos();
		IVec a=edge.source().pos();
		IVec b=edge.end().pos();
		double tem=p.dif(a).dot(b.dif(a));
		double dist=a.dist(b);
		return tem/dist;
	}
	
	public boolean isOnEdge(Edge edge) {
		IVec p=this.pos();
		IVec a=edge.source().pos();
		IVec b=edge.end().pos();
		double tem=p.dif(a).cross(b.dif(a)).len();
		if (tem<Constant.tolerance) {
			double x1=a.x();
			double x2=b.x();
			double t;
			if (x1>x2) {
				t=x1;
				x1=x2;
				x2=t;
			}
			if (x1>p.x()||p.x()>x2) return false;
				
			double y1=a.y();
			double y2=b.y();
			if (y1>y2) {
				t=y1;
				y1=y2;
				y2=t;
			}
			if (y1>p.y()||p.y()>y2) return false;
			
			double z1=a.z();
			double z2=b.z();
			if (z1>z2) {
				t=z1;
				z1=z2;
				z2=t;
			}
			if (z1>p.z()||p.z()>z2) return false;
			
			return true;
		}else {
			return false;
		}
	}
	
	public void addAlias(String str) {
		if (str!=null)
		alias.add(str);
	}

}
