package model;

import view.GamePanel;

import java.util.ArrayList;

/**
 * 象
 */
public class Bishop extends ChessObject{

	public Bishop(int i, int j, String color) {
		super(i, j, color);
	}

	public ArrayList<int[]> getWays() {
		ArrayList<int[]> ways = new ArrayList<>();
		//Go top left to find the path
		int i = this.i;
		int j = this.j;
		while (i >= 0 && j >= 0){
			ways.add(new int[]{i,j});
			i--;
			j--;
			if (i >= 0 && j >= 0 && !GamePanel.chessObjects[i][j].isNull()){
				ways.add(new int[]{i,j});
				break;
			}
		}

		//Search for the path towards the upper right direction.
		i = this.i;
		j = this.j;
		while (i <= 7 && j >= 0){
			ways.add(new int[]{i,j});
			i++;
			j--;
			if (i <= 7 && j >= 0 && !GamePanel.chessObjects[i][j].isNull()){
				ways.add(new int[]{i,j});
				break;
			}
		}

		//向左下方走寻找路径
		i = this.i;
		j = this.j;
		while (i >= 0 && j <= 7){
			ways.add(new int[]{i,j});
			i--;
			j++;
			if (i >= 0 && j <= 7 && !GamePanel.chessObjects[i][j].isNull()){
				ways.add(new int[]{i,j});
				break;
			}
		}

		//Find the path to the bottom right
		i = this.i;
		j = this.j;
		while (i <= 7 && j <= 7){
			ways.add(new int[]{i,j});
			i++;
			j++;
			//Check if other pieces are encountered in the path to prevent movement through other pieces.
			if (i <= 7 && j <= 7 && !GamePanel.chessObjects[i][j].isNull()){
				ways.add(new int[]{i,j});
				break;
			}
		}

		//If there is no path to move, it will return a null value for checking purposes.
		if (ways.size() == 0){
			return null;
		}
		return ways;
	}
}
