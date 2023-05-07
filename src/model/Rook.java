package model;

import view.GamePanel;

import java.util.ArrayList;

/**
 * 车
 */
public class Rook extends ChessObject{

	public Rook(int i, int j, String color) {
		super(i, j, color);
	}

	public ArrayList<int[]> getWays() {
		ArrayList<int[]> ways = new ArrayList<>();

		//Go up and left to find the path
		int i = this.i;
		int j = this.j;
		while (i >= 0){
			ways.add(new int[]{i,this.j});
			i--;
			if (i != -1 && !GamePanel.chessObjects[i][this.j].isNull()){
				ways.add(new int[]{i,this.j});
				break;
			}
		}
		while (j >= 0){
			ways.add(new int[]{this.i,j});
			j--;
			if (j != -1 && !GamePanel.chessObjects[this.i][j].isNull()){
				ways.add(new int[]{this.i,j});
				break;
			}
		}

		//Go down and right to find the path
		i = this.i;
		j = this.j;
		while (i <= 7){
			ways.add(new int[]{i,this.j});
			i++;
			//Check if other pieces are encountered in the path to prevent moving through other pieces
			if (i != 8 && !GamePanel.chessObjects[i][this.j].isNull()){
				ways.add(new int[]{i,this.j});
				break;
			}
		}
		while (j <= 7){
			ways.add(new int[]{this.i,j});
			j++;
			if (j != 8 && !GamePanel.chessObjects[this.i][j].isNull()){
				ways.add(new int[]{this.i,j});
				break;
			}
		}

		//If there is no available path to move, return a null value to determine
		if (ways.size() == 0){
			return null;
		}
		return ways;
	}
}
