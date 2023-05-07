package model;

import view.GamePanel;

import java.util.ArrayList;

/**
 * 后
 */
public class Queen extends ChessObject{

	public Queen(int i, int j, String color) {
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
		//Go top left to find the path
		i = this.i;
		j = this.j;
		while (i >= 0 && j >= 0){
			ways.add(new int[]{i,j});
			i--;
			j--;
			if (i >= 0 && j >= 0 && !GamePanel.chessObjects[i][j].isNull()){
				ways.add(new int[]{i,j});
				break;
			}
		}
		//Find the path to the top right
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
		//Go to the bottom left to find the path
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
			//Check if other pieces are encountered in the path to prevent moving through other pieces
			if (i <= 7 && j <= 7 && !GamePanel.chessObjects[i][j].isNull()){
				ways.add(new int[]{i,j});
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
