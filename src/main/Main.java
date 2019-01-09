package main;

import java.util.ArrayList;

import igeo.*;
import processing.core.PApplet;

public class Main extends PApplet {
	
	// vars for input
	String dataPath = "finalData.txt";
	IVec[] sources, ends;
	double[][] weights;
	
	//
	double mergeMaxDist=31;
	
	//
	Field field;

	public void setup() {
		inputData(dataPath);

	}

	public void draw() {

	}
	
	private void inputData(String filePath) {
		System.out.println("Open file: " + filePath);
		String lines[] = loadStrings(filePath);
		String[] words = PApplet.splitTokens(lines[0]);
		int sourceNum = Integer.parseInt(words[0]);
		int endNum = Integer.parseInt(words[1]);
		sources = new IVec[sourceNum];
		ends = new IVec[endNum];
		weights = new double[sourceNum][endNum];
		for (int i = 0; i < sourceNum; i++) {
			words = PApplet.splitTokens(lines[2 + i]);
			sources[i] = new IVec(Double.parseDouble(words[1]), Double.parseDouble(words[2]),
					Double.parseDouble(words[3]));
		}
		for (int i = 0; i < endNum; i++) {
			words = PApplet.splitTokens(lines[2 + sourceNum + i]);
			ends[i] = new IVec(Double.parseDouble(words[1]), Double.parseDouble(words[2]),
					Double.parseDouble(words[3]));
		}
		for (int i = 0; i < sourceNum; i++) {
			words = PApplet.splitTokens(lines[3 + sourceNum + endNum + i]);
			for (int j = 0; j < endNum; j++) {
				weights[i][j] = Double.parseDouble(words[j]);
			}
		}
	}
	
	

}
