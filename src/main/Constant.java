package main;

public class Constant {
	public static double tolerance=0.001;
	
	public static double crossResistence=5d;
	public static double mergeResistence=4d;
	public static double divideResistence=2.5d;
	
	public static double dist2Resistence(double dist) {
		return dist/1.3d;
	}
	
}
