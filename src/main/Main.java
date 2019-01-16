package main;

import java.util.ArrayList;
import java.util.Arrays;

import igeo.*;
import processing.core.PApplet;

public class Main extends PApplet {
	
	// vars for input
	String dataPath = "finalData.txt";
	Node[] sources, ends;
	double[][] weights;
	
	//
	double mergeMaxDist=1;
	
	//
	Field field;
	
	// vars for display
	int[][] displayPaths= {
			//put path to log here
//			{1,2}
			};
	boolean[][] isDisplay=null;

	public void setup() {
		this.inputData(dataPath);
		this.mergePoints(mergeMaxDist);
		field=new Field();
		field.buildField(sources, ends,weights);
		for (Node node:field.getNodes()) {
			System.out.println(node);
		}
		System.out.println("build field done.");
		iniDisplay();
		for (int i=0;i<10;i++)
			oneStep(sources,ends);

		
//		System.out.println(String.format("spfa done. %s nodes between %s and %s ",
//				field.findShortestPath(sources[0], ends[0]).size(),
//				sources[0], 
//				ends[0]
//				));
//		System.out.println(String.format("spfa done. found %s paths between %s and %s ",
//				field.findAllShortestPath(sources[0], ends[0]).size(),
//				sources[0], 
//				ends[0]
//				));
//		System.out.println(ends[0].s_Spfa_Next_List());

		System.out.println("This is end of setup().");
	}

	public void draw() {

	}
	
	public void oneStep(Node[] sources, Node[] ends) {
		ArrayList<Path> paths=findAllPath4OneStep(sources, ends);
		System.out.println("All paths founded: "+paths.size());
		
		updateNodeResistence(paths);
		int count=0;
		for (Node node:field.getNodes()) {
			if (node.resistence==Constant.crossResistence)count++;
		}
		System.out.println(String.format("%s of %s nodes are crossed.", count,field.getNodes().size()));
	}
	
	public ArrayList<Path> findAllPath4OneStep(Node[] sources, Node[] ends){
		ArrayList<Path> allPaths=new ArrayList<Path>();
		for(int i=0;i<sources.length;i++) 
			for (int j=0;j<ends.length;j++){
				if (weights[i][j]==0)
					continue;
				ArrayList<Path> paths=field.findAllShortestPath(sources[i], ends[j]);
				for (Path path:paths) {
					path.weight=weights[i][j]/paths.size();
					if (isDisplay[i][j])
						path.isDisplay=true;
					allPaths.add(path);
			}
		}
		return allPaths;
	}
	
	public void updateNodeResistence(ArrayList<Path> paths) {
		for (Node node:field.getNodes()) {
			node.onPaths=new ArrayList<Path>();
		}
		for (Path path:paths) {
//			System.out.println(" path length£» "+path.nodes().size());
			for (int i=1;i<path.nodes().size()-1;i++) {
				path.nodes().get(i).onPaths.add(path);
			}
		}
		for (Node node:field.getNodes()) {
			double max=0;
			ArrayList<Node> neighbours=new ArrayList<Node>();
			//mark nodes from different height
			for (Node n:node.neighbors()) {
				if (Math.abs(n.pos().z()-node.pos().z())>Constant.tolerance) 
					n.order=-1;
				else 
					neighbours.add(n);
			}
			//sort for clockwise and mark the order
			clockWiseSort(neighbours,node);
			for (int i=0;i<neighbours.size();i++) {
				neighbours.get(i).order=i;
			}
			for (Path iPath:node.onPaths) 
				for (Path jPath:node.onPaths) {
					Node pi=iPath.pre(node);
					Node ni=iPath.next(node);
					Node pj=jPath.pre(node);
					Node nj=jPath.next(node);
					double re=resistence(neighbours.size()-1,pi.order,ni.order,pj.order,nj.order);
					if (re>max)
						max=re;
				}
			node.resistence=max;
			}
		
		
	}
	
	public double resistence(int max,int a1,int a2,int b1,int b2) {
		if (a1==b1 && a2==b2) return 0d;
		if (a1 == b1)
			return Constant.divideResistence;
		if (a2==b2)
			return Constant.mergeResistence;
		if (a1==-1) return 0d;
		if (a2==-1) return 0d;
		if (b1==-1) return 0d;
		if (b2==-1) return 0d;
		a2=(a2-a1+max)%max;
		b1=(b1-a1+max)%max;
		b2=(b2-a1+max)%max;
		a1=0;
		if (b1<a2 && (a2<b2 || b2==a1))
			return Constant.crossResistence;
		if (b1>=a2 && a1<b2 && b2<a2 )
			return Constant.crossResistence;
		return 0d;
	}
	
	boolean compare(IVec p1, IVec p2, IVec p3) {
		double tem=p2.dif(p1).cross(p3.dif(p1)).z();
		if (tem>0) return true;
		if (tem<0) return false;
		return p3.dist(p1)>p2.dist(p1);
	}
	
	private void clockWiseSort(ArrayList<Node> nodes,Node center) {
		int len=nodes.size();
		for (int i = 0; i < len - 1; i++)
			for (int j = 0; j < len - 1 - i; j++)
				if (compare(center.pos(),nodes.get(j+1).pos(),nodes.get(j).pos())) {
					Node tem=nodes.get(j);
					nodes.set(j,nodes.get(j+1));
					nodes.set(j+1,tem);
				}
	}
	
	private void iniDisplay() {
		isDisplay=new boolean[sources.length][ends.length];
		if (displayPaths.length==0) {
			for(int i=0;i<isDisplay.length;i++)
				for (int j=0;j<isDisplay[0].length;j++)
					isDisplay[i][j]=true;
		}
		for(int i=0;i<displayPaths.length;i++) {
			isDisplay[displayPaths[i][0]][displayPaths[i][1]]=true;
		}
	}
	
	private void inputData(String filePath) {
		System.out.println("Open file: " + filePath);
		String lines[] = loadStrings(filePath);
		String[] words = PApplet.splitTokens(lines[0]);
		int sourceNum = Integer.parseInt(words[0]);
		int endNum = Integer.parseInt(words[1]);
		sources = new Node[sourceNum];
		ends = new Node[endNum];
		weights = new double[sourceNum][endNum];
		for (int i = 0; i < sourceNum; i++) {
			words = PApplet.splitTokens(lines[2 + i]);
			IVec pos = new IVec(Double.parseDouble(words[1]), Double.parseDouble(words[2]),
					Double.parseDouble(words[3]));
			sources[i] = new Node(words[0],pos);
		}
		for (int i = 0; i < endNum; i++) {
			words = PApplet.splitTokens(lines[2 + sourceNum + i]);
			IVec pos = new IVec(Double.parseDouble(words[1]), Double.parseDouble(words[2]),
					Double.parseDouble(words[3]));
			ends[i] = new Node(words[0],pos);
		}
		for (int i = 0; i < sourceNum; i++) {
			words = PApplet.splitTokens(lines[3 + sourceNum + endNum + i]);
			for (int j = 0; j < endNum; j++) {
				weights[i][j] = Double.parseDouble(words[j]);
			}
		}
	}
	
	private void mergePoints(double maxDist) {
		int len=sources.length+ends.length;
		double[] num=new double[len];
		
		for (int i=0;i<sources.length;i++) {
			num[i]=sources[i].pos().x();
		}
		for (int i=sources.length;i<len;i++) {
			num[i]=ends[i-sources.length].pos().x();
		}
		
		num=mergeNum(num,maxDist);
		
		for (int i=0;i<sources.length;i++) {
			sources[i].pos().x(num[i]);
		}
		for (int i=sources.length;i<len;i++) {
			ends[i-sources.length].pos().x(num[i]);
		}
		
		for (int i=0;i<sources.length;i++) {
			num[i]=sources[i].pos().y();
		}
		for (int i=sources.length;i<len;i++) {
			num[i]=ends[i-sources.length].pos().y();
		}
		
		num=mergeNum(num,maxDist);
		
		for (int i=0;i<sources.length;i++) {
			sources[i].pos().y(num[i]);
		}
		for (int i=sources.length;i<len;i++) {
			ends[i-sources.length].pos().y(num[i]);
		}
	}
	
	public static double[] mergeNum(double[] nums,double maxDelta) {
		nums=nums.clone();
		int len=nums.length;
		int[] pos=new int[len];
		for (int i=0;i<len;i++)
			pos[i]=i;
		
		//sort
		for (int i = 0; i < len - 1; i++)
			for (int j = 0; j < len - 1 - i; j++)
				if (nums[j] > nums[j + 1]) {
					double tem=nums[j];
					nums[j]=nums[j+1];
					nums[j+1]=tem;
					int temPos=pos[j];
					pos[j]=pos[j+1];
					pos[j+1]=temPos;
				}
		//merge
		int leftPos=0;
		int rightPos;
		while (leftPos<len-1) {
			rightPos=leftPos;
			while ( nums[rightPos+1]-nums[rightPos]<maxDelta) {
				rightPos++;
				if (rightPos==len-1)
					break;
			}
			for (int i=leftPos;i<=rightPos;i++) {
				nums[i]=nums[(leftPos+rightPos)/2];
			}
			
			leftPos=rightPos+1;
		}

		//anti-sort
		double[] result=new double[len];
		for (int i=0;i<len;i++) {
			result[pos[i]]=nums[i];
		}
		
		return result;
	}
	
	private void printPoints() {
		for(int i=0;i<sources.length;i++) {
			System.out.println(sources[i]);
		}
		for(int i=0;i<sources.length;i++) {
			System.out.println(ends[i]);
		}
	}

}
