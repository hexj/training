package betterlife.hex.learn.algorithms.projecteuler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonathan.hexj
 *<url>https://projecteuler.net/problem=3</url>
 */
public class LargestPrimeFactor {
	public static void main(String[] args) {
		// testMaxPrimeFactor();
		// testLargestPrimeFactor2();
		testLargestPrimeFactor();
	}

	public static long largestPrimeFactor(long number) {
		long result = -1;
		int count = 0;
		for (long i = 2; i <= number; i++) {
			if (number % i == 0) {
				result = i;
				number /= i;
				--i;
			}
			System.out.println(++count + "+++number =" + number + ", i = " + i
					+ ", result =" + result);
		}
		return result;
	}

	public static long largestPrimeFactor2(long num) {
		long prime = 2, count = 0;

		while (prime * prime <= num) {
			if (num % prime == 0) {
				num /= prime;
			} else {
				prime++;
			}

			System.out.println(++count + "+++number =" + num + ", prime ="
					+ prime);
		}
		return num;
	}

	/**
	 * 暴力尝试所有素数, 时间复杂度 T(n) = n/2 * log(n)
	 * 
	 * @param num
	 * @return
	 */
	public static int largestPrimeFactor(int num) {
		int result = 1;
		List<Integer> primeList = new ArrayList<Integer>();
		primeList.add(2);
		for (int i = 3; i <= num / 2; i++) {
			boolean primeFlag = true;
			for (int j = 0; j < primeList.size(); j++) {
				if (i % primeList.get(j) == 0) {
					primeFlag = false;
					break;
				}
			}
			if (primeFlag) {// is prime
				if (num % i == 0) {// is factor
					result = i;
					System.out.println(result);
				}
				primeList.add(i);
			}
		}
		return result;
	}

	/**
	 * 整数限制,
	 * 
	 * @param num
	 * @return
	 */
	public static long maxPrimeFactor(int num) {
		long result = 1;
		// prim is an array to mark the index is prim or not
		int prim[] = new int[num + 1];

		for (int i = 2, temp = 0; i < num; ++i)
			// 没有被前面数字标记过表示为素数
			if (prim[i] == 0) {
				temp++;
				// num could divided by i mark it to temp(index of prime)
				// 在i之后递加标记所有可以被i整除的j(2*i, 3*i,.....)为当前素数的序数
				for (int j = i; j < num + 1; j += i) {
					prim[j] = temp;
				}
			}
		result = prim[num];
		return result;
	}

	private static void testLargestPrimeFactor2() {
		int num = 1000000;
		long result = largestPrimeFactor(num);
		System.out.println("testlargestPrimeFactor =" + result);
		System.out.println("maxPrimeFactor =" + maxPrimeFactor(num));
	}

	private static void testMaxPrimeFactor() {
		long result = maxPrimeFactor(101000);
		System.out.println("maxPrimeFactor of 101000 = " + result);
	}

	private static void testLargestPrimeFactor() {
		long result = largestPrimeFactor2(111); // 600851475143L
		System.out.println("largestPrimeFactor =" + result);
	}
}
