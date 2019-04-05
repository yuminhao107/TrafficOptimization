package main;

import java.util.ArrayList;

import igeo.IVec;

public class Node {
	// Attributes for Node
	private String name=null;
	private ArrayList<String> alias=new ArrayList<String>();
	private ArrayList<Edge> neighborEdges = new ArrayList<Edge>();
	private IVec position;
	
	private static int nextGuid=0;
	public int guid;

	public int sourceId = -1;
	public int endId = -1;

	// Attributes for spfa
	public double spfa_Dist;
	public Node spfa_Next;
	public ArrayList<Node> spfa_Next_List;
	
	// Attributes for buildField
	public double shadow=0d;
	
	// Attributes for conflict detect
	public int order;
	
	// Attributes for optimize
	public double resistence=0d;
	public ArrayList<Path> onPaths;

	// Method for Node
	public Node(String id) {
		this.guid=++Node.nextGuid;
		this.name = id;
	}

	public Node(String id, double x, double y, double z) {
		this.guid=++Node.nextGuid;
		this.name = id;
		this.position = new IVec(x, y, z);
	}

	public Node(String id, IVec pos) {
		this.guid=++Node.nextGuid;
		this.name = id;
		this.position = pos;
	}
	
	public Node(IVec pos) {
		this.guid=++Node.nextGuid;
		this.position = pos;
		
	}

	public String id() {
		return name;
	}
	
	public boolean hasId() {
		return this.name!=null;
	}

	public IVec pos() {
		return this.position;
	}
	
	public Edge findEdge(Node node) {
		for (Edge edge:neighborEdges) {
			if (edge.end().equals(node)) {
				return edge;
			}
		}
		return null;
	}

	public void addNeighbor(Node node) {
		if (node.equals(this))
			return;
		for (Edge edge:this.neighborEdges) {
			if (edge.end().equals(node))
				return;
		}
		double weight=Constant.dist2Resistence(this.pos().dist(node.pos()));
		neighborEdges.add(new Edge(this, node, weight));
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
		String text=String.format("%s %s (guid:%s)", name,this.pos(),guid);
//		text+="-"+this.neighborEdges.size()+" out edges-";
		return text;
	}
	
	public String s_Spfa_Next_List() {
		String text=String.format("The spfa_next_list for %s is ", this);
		text+=this.spfa_Next_List.size();
//		for (Node node:this.spfa_Next_List) {
//			text+=node+" ";
//		}
		return text;
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
