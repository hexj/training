package betterlife.hex.learn.algorithms.dp.triangle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonathan.hexj
 *         http://blog.csdn.net/fox64194167/article/details/21001855
 */
public class DeTriangle {

	public static void main(String[] args) {
		deTriangle();
	}

	private static void deTriangle() {
		DeTriangle triangle = new DeTriangle();
		int row = 100; // number of rows
		List<List<Long>> list2d = null;
		list2d = triangle.init(row, list2d);
		triangle.display(list2d);
		System.out.println("Min path is : " + triangle.lestPath(list2d));
	}

	/**
	 * just could add next level near two number to get the least sum from top
	 * to the bottom i.e. a[i][j] could add a[i+1][j] and a[i+1][j+1]
	 * 上层的数可以通下一层的相邻俩个数,找出从顶到底的最小路径
	 * 
	 * @param list2d
	 * @return
	 */
	public Long lestPath(final List<List<Long>> list2d) {
		List<List<Long>> pathval = new ArrayList<>(list2d);
		int lsize = list2d.size();
		if (lsize > 1) {
			for (int i = lsize - 2; i >= 0; i--) {
				for (int j = 0; j <= i; j++) {
					Long currval = list2d.get(i).get(j);
					Long nextOne = list2d.get(i + 1).get(j);
					Long nextTwo = list2d.get(i + 1).get(j + 1);
					pathval.get(i).set(j, currval + Math.min(nextOne, nextTwo));
				}
			}
		}
		display(pathval);
		return pathval.get(0).get(0);
	}

	/**
	 * initial array list2d
	 * 
	 * @param rows
	 * @param list2d
	 * @return initialed array list2d
	 */
	private List<List<Long>> init(int rows, List<List<Long>> list2d) {
		list2d = new ArrayList<>();
		for (int i = 0; i < rows; i++) {
			List<Long> list1d = new ArrayList<>();
			for (int j = 0; j <= i; j++) {
				Long numij = new Long(Math.round(Math.random() * 100));
				list1d.add(numij);
			}
			list2d.add(list1d);
		}
		return list2d;
	}

	private void display(List<List<Long>> list) {
		displayTriangle(list);
	}

	private void displayTriangle(List<List<Long>> list) {
		int lsize = list.size();
		for (int i = 0; i < lsize; i++) {
			List<Long> list2 = list.get(i);
			for (int j = 0; j < (lsize - i - 1); j++) {
				System.out.print("\t");
			}

			for (int j = 0; j < list2.size(); j++) {
				Long num = list2.get(j);
				System.out.print(num);
				if (j < list2.size() - 1) {
					System.out.print("\t\t");
				}
			}
			System.out.println();
		}
	}
}
