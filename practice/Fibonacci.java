package com.hexj.learn;

import java.util.ArrayList;
import java.util.List;

public class Fibonacci {

	public static void main(String[] args) {
		int n = 12, bothnum = 4, dietime = 3;
		fib2(n, bothnum, dietime);
		// System.out.println(jossefose(5, 3));
	}

	/** 普通Fibonacci */
	public static List<Long> fibonacci(int n) {
		long post = 1, pre = 0;
		List<Long> list = new ArrayList<Long>();
		list.add(new Long(post));
		for (int i = 1; i <= n; i++) {
			post += pre;
			pre = post - pre;
			list.add(new Long(post));
		}
		return list;
	}

	/**
	 * 某品种蛐蛐，寿命dietime个月，一对蛐蛐每个月能繁殖bornpertime对小蛐蛐，<br>
	 * 而每对小蛐蛐，一个月后也能繁殖bornpertime对新小蛐蛐， <br>
	 * 如果开始只有一对蛐蛐，那么第term个月有多少对蛐蛐？
	 */
	public static Long fib2(int term, int bornpertime, int dietime) {
		long post = 1, pre = 1, swp = 0;
		List<Long> dielist = new ArrayList<Long>();
		dielist.add(new Long(post));
		for (int i = 1; i <= term; i++) {
			swp = post;

			Long dienum = 0l;
			if (i >= dietime) {
				dienum = dielist.get(i - dietime);
				pre -= dienum;
			}

			long borncount = pre * bornpertime;
			dielist.add(borncount);
			post += borncount;
			System.out.println(i + "个月, " + swp + "对, 新出生 : " + pre
					* bornpertime + ", die num : " + dienum);
			pre = post;
			System.out.println(post);
		}
		return post;
	}

	/** 约瑟夫环问题 */
	public static int jossefose(int personnum, int num) {
		int last = 0;
		for (int i = 2; i <= personnum; i++) {
			last = (last + num) % i;
		}
		return last + 1;
	}

}
