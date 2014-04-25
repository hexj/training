package betterlife.hex.learn.algorithms.projecteuler;

import java.math.BigInteger;

import betterlife.hex.learn.algorithms.bignum.Constants;

/**
 * @author Jonathan.hexj
 *<url>https://projecteuler.net/problem=2</url>
 */
public class FibonacciEvenSum {

	public static void main(String[] args) {
		fibonacci();
		fibonacciEven();
	}

	public static int fibonacciEven() {
		int sum = 0;
		int prev = 1;
		int next = 1;
		while (next <= 4000000) {
			next = prev + next;
			prev = next - prev;
			if(next % 2 == 0){
				sum += next;
			}
		}
		System.out.println(sum);
		return sum;
	}

	public static BigInteger fibonacci() {
		BigInteger prev = BigInteger.ONE, next = new BigInteger("2");
		BigInteger result = BigInteger.valueOf(Constants.INIT);
		BigInteger count = BigInteger.ZERO;
		BigInteger endnum = new BigInteger("4000000");
		BigInteger sum = new BigInteger("2");

		while (result.compareTo(endnum) <= 0) {
			result = prev.add(next);
			prev = next;
			next = result;
			count = count.add(BigInteger.ONE);
			if (even(result)) {
				sum = sum.add(result);
			}
		}
		System.out.println(sum);
		return sum;
	}

	public static boolean even(BigInteger val) {
		return (val.mod(new BigInteger("2")).equals(BigInteger.ZERO));
	}
}
