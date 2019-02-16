package main;

public class Constant {
	public static double tolerance=0.001;
	
	// max distance for merge points
	static double mergeMaxDist=1;
	
	//different resistance value
	public static double crossResistance=5d;
	public static double mergeResistance=4d;
	public static double divideResistance=2.5d;
	
	public static double dist2Resistence(double dist) {
		return dist/1.3d;
	}
	
	//display settings
	public static double pointRadius=0.5d;
	
}
