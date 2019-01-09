package main;

import java.util.Random;

public class Rand {
	private static Random rand = new Random(System.currentTimeMillis());

	public static double nextDouble() {
		return rand.nextDouble();
	}

}
