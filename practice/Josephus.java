package forus.hex.training;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Josephus {

	public static void main(String[] args) {
		int person_num = 5, num = 3;
		System.out.println(josephusLast_On(person_num, num));
		System.out.println(josephusLast_Onm(person_num, num));
	}

	static long josephusLast_On(long n/* person num */, long m /* number */) {
		long last = 0;
		for (long i = 2; i <= n; i++) {
			last = (last + m) % i;
		}
		return last + 1;
	}

	static long josephusLast_Onm(int n/* person num */, int m /* number */) {
		BlockingQueue<Integer> q = new ArrayBlockingQueue<Integer>(n);
		int last = 0;
		try {
			for (int i = 1; i <= n; i++)
				q.put(i);
			while (!q.isEmpty()) {
				for (int i = 0; i < m - 1; i++)
					q.put(q.take());
				last = q.take();
			}
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		return last;
	}
}
