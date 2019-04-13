package main;

import java.util.ArrayList;
import java.util.Arrays;

import igeo.*;
import processing.core.PApplet;

public class Main extends PApplet {
	
	// vars for input
	String dataPath = "../data/finalData2.txt";
	Node[] sources, ends;
	double[][] weights;
	
	ArrayList<Path> historypaths = new ArrayList<Path>();

	//
	Field field;
	
	// vars for display
	int[][] displayPaths= {
			//put path to log here
//			{ 0, 2 }
			};
	boolean[][] isDisplay=null;
	int[][] pathCount=null;
	int stepCount=0;
	int textSize = 6;
	int stepNum = 1000;
	
	int maxStep=1000;
	int lamdaDivide=100;

	boolean notPressed = true;

	public void setup() {
//		size(800, 600, IG.GL);
		this.inputData(dataPath);
		this.mergePoints(Constant.mergeMaxDist);
		field=new Field();
		field.buildField(sources, ends,weights);
		for (Node node:field.getNodes()) {
			System.out.println(node);
//			for (Edge edge:node.neighborEdges()) {
//				System.out.println(edge);
//			}
		}

		System.out.println("build field done.");
		System.out.println(String.format("%s nodes. %s edges.", field.getNodes().size(),field.getEdges().size()));
		iniDisplay();

		System.out.println("This is end of setup().");
//		oneStep(sources, ends); m
//		showField();
		this.findLamda(sources, ends, weights, maxStep, lamdaDivide);
	}

	public void draw() {

	}
	
	public void keyPressed() {
		if (this.key == 'n') {
			oneStep(sources, ends, weights, stepNum);
		}
		if (this.key == 'm' && notPressed) {
//			notPressed = false;
			autoStep(sources, ends, weights, stepNum);
		}
	}
	
	public void findLamda(Node[] sources, Node[] ends, double[][] weights, int maxStep, int divide) {
		System.out.println();
		System.out.println("lamda, step, cost");
		double min=Double.MAX_VALUE;
		int minStep=-1;
		double minLamda=-1;
		for (int i=0;i<=divide;i++) {
			Constant.lamda=1d*i/divide;
			SData data=minCost(sources,ends,weights,maxStep);
			System.out.println(String.format("%s, %s, %s",Constant.lamda,data.index,data.cost));
			if (data.cost<min) {
				min=data.cost;
				minStep=data.index;
				minLamda=Constant.lamda;
			}
		}
		System.out.println("lamda, step, cost");
		System.out.println(String.format("Min cost=%s appears, when lamda=%s,step=%s",min,minLamda,minStep));
	}
	
	class SData{
		double cost;
		int index;
	}
	
	public SData minCost(Node[] sources, Node[] ends, double[][] weights, int maxStep) {
		double min=Double.MAX_VALUE;
		int index=0;
		for (int i = 0; i < stepNum; i++) {
			ArrayList<Path> paths = findAllPath4OneStep(sources, ends, weights);
			updateGobalNodeResistence(paths, field.getNodes());
			double cost=printTotalCost(paths);
			if (cost <min) {
				min=cost;
				index=i+1;
			}
		}
		SData data=new SData();
		data.index=index;
		data.cost=min;
		return data;
	}

	public void autoStep(Node[] sources, Node[] ends, double[][] weights, int stepNum) {
		ArrayList<Path> paths = new ArrayList<Path>();
		for (int i = 0; i < stepNum; i++) {
			System.out.println();
			System.out.println(String.format("------step %s------", ++stepCount));

			paths = findAllPath4OneStep(sources, ends, weights);
			System.out.println("All paths founded: " + paths.size());

			updateGobalNodeResistence(paths, field.getNodes());

			// show number of paths focused
//			printPathNumber();

			// count crossing point
			printCrossingPointNumber();

			printTotalCost(paths);
		}
		IG.clear();
		IG.layer("0");
		showPoints();
		visualize(paths);
	}

	public void oneStep(Node[] sources, Node[] ends, double[][] weights) {
		System.out.println();
		System.out.println(String.format("------step %s------", ++stepCount));
		
		ArrayList<Path> paths = findAllPath4OneStep(sources, ends, weights);
		System.out.println("All paths founded: "+paths.size());
		
		updateNodeResistence(paths);


		IG.clear();
		IG.layer("0");
		showPoints();
//		visualizeWithLayer(paths, sources, ends, weights);
		visualize(paths);
		
		// show number of paths focused
//		printPathNumber();

		// count crossing point
		printCrossingPointNumber();
		
		printTotalCost(paths);

	}

	public void oneStep(Node[] sources, Node[] ends, double[][] weights, int stepNum) {
		System.out.println();
		System.out.println(String.format("------step %s------", ++stepCount));

		ArrayList<Path> paths = findAllPath4OneStep(sources, ends, weights);
		System.out.println("All paths founded: " + paths.size());

		updateGobalNodeResistence(paths, field.getNodes());

		IG.clear();
		IG.layer("0");
		showPoints();
//		visualizeWithLayer(paths, sources, ends, weights);
		visualize(paths);

		// show number of paths focused
//		printPathNumber();

		// count crossing point
		printCrossingPointNumber();

		printTotalCost(paths);

	}

	private double printTotalCost(ArrayList<Path> paths) {
		double sum = 0;
		for (Path path : paths) {
			sum += path.cost();
		}
//		System.out.print("Total cost is " + sum);
		return sum;
	}
	
	private void showField() {
		showPoints();
		IG.layer("0-edges");
		for (Edge edge:field.getEdges()) {
			new ICurve(edge.source().pos(),edge.end().pos());
		}
	}
	
	private void showPoints() {
		IG.layer("0-points");
		for (Node node:field.getNodes()) {
			ISphere sphere=new ISphere(node.pos(),Constant.pointRadius);
			if (node.hasId()) {
				sphere.clr(1d,0d,0d);
				IText text = new IText(node.id(), textSize, node.pos());
			}
		}
	}
	
	private void printCrossingPointNumber() {
		int crossCount = 0;
		int mergeCount = 0;
		int divideCount = 0;
		for (Node node:field.getNodes()) {
			if (node.resistence == Constant.crossResistance)
				crossCount++;
			if (node.resistence == Constant.mergeResistance)
				mergeCount++;
			if (node.resistence == Constant.divideResistance)
				divideCount++;
		}
		System.out.println(String.format("%s nodes in total. %s crossed. %s merged. %s divided.",
				field.getNodes().size(), crossCount, mergeCount, divideCount));
	}
	
	private void printPathNumber() {
		for(int i=0;i<sources.length;i++) 
			for (int j=0;j<ends.length;j++){
				if (isDisplay[i][j]) {
					String msg=String.format("%s paths between %s and %s.",pathCount[i][j],sources[i],ends[j]);
					System.out.println(msg);
				}
			}
	}
	
	public void visualize(ArrayList<Path> paths) {
		for (Edge edge:field.getEdges()) {
			edge.showed=false;
			edge.flow=0d;
		}
		for (Path path : paths) {
//			if (!path.isDisplay)
//				continue;
//			System.out.println(path);
			for (int i = 0; i < path.nodes().size()-1; i++) {
				path.nodes().get(i).findEdge(path.nodes().get(i+1)).flow+=path.weight;
			}
			
//			IVec[] points = new IVec[path.numOfPoints()];
//			for (int i = 0; i < points.length; i++) {
//				points[i] = path.nodes().get(i).pos().cp();
//			}
//			ICurve curve = new ICurve(points);
//			IG.pipe(curve, Constant.weight2Radius(path.weight));


		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
		for (Edge edge:field.getEdges()) {
			if (edge.showed) continue;
			double flow=edge.flow+edge.pair.flow;
			if (flow<Constant.tolerance) continue;
			ICurve curve = new ICurve(edge.source().pos().cp(),edge.end().pos().cp());
			ISurface surface = IG.pipe(curve, Constant.weight2Radius(flow)).layer("0-pipes");
			new IText("" + df.format(flow), textSize, curve.mid()).layer("0-numbers");
			edge.showed=true;
			edge.pair.showed=true;
		}
	}

	public void visualize(ArrayList<Path> paths, Node source, Node end) {
		String layerName = source.id() + "-" + end.id();
		for (Edge edge : field.getEdges()) {
			edge.showed = false;
			edge.flow = 0d;
		}
		for (Path path : paths) {
			if ((!path.source().equals(source)) || (!path.end().equals(end)))
				continue;
			for (int i = 0; i < path.nodes().size() - 1; i++) {
				path.nodes().get(i).findEdge(path.nodes().get(i + 1)).flow += path.weight;
			}
		}
		for (Edge edge : field.getEdges()) {
			if (edge.showed)
				continue;
			double flow = edge.flow + edge.pair.flow;
			if (flow < Constant.tolerance)
				continue;
			ICurve curve = new ICurve(edge.source().pos().cp(), edge.end().pos().cp());
			ISurface surface = IG.pipe(curve, Constant.weight2Radius(flow));
			curve.del();
			surface.layer(layerName);
			edge.showed = true;
			edge.pair.showed = true;
		}
	}

	public void visualizeWithLayer(ArrayList<Path> paths, Node[] sources, Node[] ends, double[][] weights) {
		for (int i = 0; i < sources.length; i++)
			for (int j = 0; j < ends.length; j++) {
				if (weights[i][j] > 0) {
					visualize(paths, sources[i], ends[j]);
				}
			}
	}

	public ArrayList<Path> findAllPath4OneStep(Node[] sources, Node[] ends, double[][] weights) {
		return findAllPath4OneStep(sources, ends, weights, 1);
	}

	public ArrayList<Path> findAllPath4OneStep(Node[] sources, Node[] ends, double[][] weights, int stepNum) {
		ArrayList<Path> allPaths=new ArrayList<Path>();
		pathCount=new int[sources.length][ends.length];
		for(int i=0;i<sources.length;i++) 
			for (int j=0;j<ends.length;j++){
				if (weights[i][j]==0)
					continue;
				ArrayList<Path> paths=field.findAllShortestPath(sources[i], ends[j],weights[i][j]);
				for (Path path : paths) {
					path.weight = weights[i][j] / stepNum / paths.size();
					if (isDisplay[i][j])
						path.isDisplay = true;
					allPaths.add(path);
					pathCount[i][j] = paths.size();
				}

//				if (paths.size() > 0) {
//					Path path = paths.get(0);
//					path.weight = weights[i][j] / stepNum;
//					allPaths.add(path);
//					pathCount[i][j] = 1;
//				}
			}
		return allPaths;
	}
	
	public void updateNodeResistence(ArrayList<Path> paths) {
		for (Node node:field.getNodes()) {
			node.onPaths=new ArrayList<Path>();
		}
		for (Path path:paths) {
//			System.out.println(" path length�� "+path.nodes().size());
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
	
	public void updateGobalNodeResistence(ArrayList<Path> paths, ArrayList<Node> nodes) {
		for (Node node : nodes) {
			node.onPaths = new ArrayList<Path>();
		}
		for (Path path : paths) {
//			System.out.println(" path length�� "+path.nodes().size());
			for (int i = 1; i < path.nodes().size() - 1; i++) {
				path.nodes().get(i).onPaths.add(path);
			}
		}
		for (Node node : nodes) {
			double max = 0;
			ArrayList<Node> neighbours = new ArrayList<Node>();
			// mark nodes from different height
			for (Node n : node.neighbors()) {
				if (Math.abs(n.pos().z() - node.pos().z()) > Constant.tolerance)
					n.order = -1;
				else
					neighbours.add(n);
			}
			// sort for clockwise and mark the order
			clockWiseSort(neighbours, node);
			for (int i = 0; i < neighbours.size(); i++) {
				neighbours.get(i).order = i;
			}
			for (Path iPath : node.onPaths)
				for (Path jPath : node.onPaths) {
					Node pi = iPath.pre(node);
					Node ni = iPath.next(node);
					Node pj = jPath.pre(node);
					Node nj = jPath.next(node);
					double re = resistence(neighbours.size() - 1, pi.order, ni.order, pj.order, nj.order);
					if (re > max)
						max = re;
				}
			node.temResistence = max;
			if (stepCount == 1)
				node.resistence = max;
			else
				node.resistence = Constant.updateResistence(node.resistence, max);
		}

	}

	public double resistence(int max,int a1,int a2,int b1,int b2) {
		if (a1==b1 && a2==b2) return 0d;
		if (a1 == b1)
			return Constant.divideResistance;
		if (a2==b2)
			return Constant.mergeResistance;
		if (a1==-1) return 0d;
		if (a2==-1) return 0d;
		if (b1==-1) return 0d;
		if (b2==-1) return 0d;
		a2=(a2-a1+max)%max;
		b1=(b1-a1+max)%max;
		b2=(b2-a1+max)%max;
		a1=0;
		if (b1<a2 && (a2<b2 || b2==a1))
			return Constant.crossResistance;
		if (b1>=a2 && a1<b2 && b2<a2 )
			return Constant.crossResistance;
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
					if (weights[i][j]>Constant.tolerance)
					isDisplay[i][j]=true;
		}else {
			for(int i=0;i<displayPaths.length;i++) {
				isDisplay[displayPaths[i][0]][displayPaths[i][1]]=true;
			}
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
