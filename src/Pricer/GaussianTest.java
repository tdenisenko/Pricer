package Pricer;

import java.util.Random;

public class GaussianTest {

	static Random r = new Random();
	private static double nextNextGaussian;
	private static boolean haveNextNextGaussian = false;

	private static double nextGaussian() {
		if (haveNextNextGaussian) {
			haveNextNextGaussian = false;
			return nextNextGaussian;
		} else {
			double v1, v2, s;
			do {
				v1 = 2 * r.nextDouble() - 1; // between 1 and 1
				v2 = 2 * r.nextDouble() - 1; // between 1 and 1
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
			nextNextGaussian = v2 * multiplier;
			haveNextNextGaussian = true;
			return v1 * multiplier;
		}
	}
	
	public static void main(String[] args) {
		double sum = 0;
		double next;
		double max = Double.MIN_NORMAL;
		double min = Double.MAX_VALUE;
		int mean = 0;
		for(int i = 0; i < 10000; i++){
			next = nextGaussian()*25;
			sum += next;
			if(next > max) {
				max = next;
			}
			if(next < min) {
				min = next;
			}
			if(next < 25 && next > -25){
				mean++;
			}
		}
		System.out.println("Mean: 0\nStd. Dev.: 25\nMin: " + min + "\nMax: " + max + "\nAvg.: " + sum/1000 + "\nNumbers in mean: " + mean/100.0 + "%");
	}
}
