package betterlife.hex.learn.algorithms.projecteuler;

/**
 * @author Jonathan.hexj
 *<url>https://projecteuler.net/problem=1</url>
 */
public class Multiples35 {
	public static void main(String[] args) {
		int endpos = 1000;
		long sum1 = multiples35(endpos);
		System.out.println("sum is :" + sum1);
	}

	public static long multiples35(int endpos) {
		long sum = 0;
		for (int i = 0; i < endpos; i++) {
			if (i % 3 == 0 || i % 5 == 0) {
				sum += i;
			}
		}
		return sum;
	}
}
