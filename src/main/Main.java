package main;

import java.util.ArrayList;

import igeo.*;
import processing.core.PApplet;

public class Main extends PApplet {
	
	// vars for input
	String dataPath = "finalData.txt";
	Node[] sources, ends;
	double[][] weights;
	
	//
	double mergeMaxDist=10;
	
	//
	Field field;

	public void setup() {
		this.inputData(dataPath);
		this.mergePoints(mergeMaxDist);
		field=new Field();
		field.buildField(sources, ends,weights);
		for (Node node:field.getNodes()) {
			System.out.println(node);
		}
		System.out.println("build field done.");
		
		
		
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


	}

	public void draw() {

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
