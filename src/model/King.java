package model;

import java.util.ArrayList;

/**
 * 王
 */
public class King extends ChessObject{

	public King(int i, int j, String color) {
		super(i, j, color);
	}

	public ArrayList<int[]> getWays() {
		ArrayList<int[]> ways = new ArrayList<>();

		int i = this.i;
		int j = this.j - 1;
		if (isInChess(i,j)){
			ways.add(new int[]{i,j});
		}

		i = this.i - 1;
		j = this.j;
		if (isInChess(i,j)){
			ways.add(new int[]{i,j});
		}

		i = this.i;
		j = this.j + 1;
		if (isInChess(i,j)){
			ways.add(new int[]{i,j});
		}

		i = this.i + 1;
		j = this.j;
		if (isInChess(i,j)){
			ways.add(new int[]{i,j});
		}

		i = this.i - 1;
		j = this.j - 1;
		if (isInChess(i,j)){
			ways.add(new int[]{i,j});
		}

		i = this.i + 1;
		j = this.j - 1;
		if (isInChess(i,j)){
			ways.add(new int[]{i,j});
		}

		i = this.i - 1;
		j = this.j + 1;
		if (isInChess(i,j)){
			ways.add(new int[]{i,j});
		}

		i = this.i + 1;
		j = this.j + 1;
		if (isInChess(i,j)){
			ways.add(new int[]{i,j});
		}

		//If there is no way out, return a null value to determine.
		if (ways.size() == 0){
			return null;
		}
		return ways;
	}
}
