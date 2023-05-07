package model;

import java.util.ArrayList;

/**
 * 马
 */
public class Knight extends ChessObject{

	public Knight(int i, int j, String color) {
		super(i, j, color);
	}

	public ArrayList<int[]> getWays() {
		ArrayList<int[]> ways = new ArrayList<>();
		if (i - 2 >= 0 && j - 1 >= 0){
			ways.add(new int[]{i-2,j-1});
		}
		if (i + 2 <= 7 && j - 1 >= 0){
			ways.add(new int[]{i+2,j-1});
		}
		if (i - 2 >= 0 && j + 1 <= 7){
			ways.add(new int[]{i-2,j+1});
		}
		if (i + 2 <= 7 && j + 1 <= 7){
			ways.add(new int[]{i+2,j+1});
		}

		if (i - 1 >= 0 && j - 2 >= 0){
			ways.add(new int[]{i-1,j-2});
		}
		if (i + 1 <= 7 && j - 2 >= 0){
			ways.add(new int[]{i+1,j-2});
		}
		if (i - 1 >= 0 && j + 2 <= 7){
			ways.add(new int[]{i-1,j+2});
		}
		if (i + 1 <= 7 && j + 1 <= 7){
			ways.add(new int[]{i+1,j+2});
		}
		return ways;
	}
}
