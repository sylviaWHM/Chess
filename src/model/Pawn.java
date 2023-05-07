package model;

import view.GamePanel;

import java.util.ArrayList;


/**
 * 兵
 */
public class Pawn extends ChessObject{
	boolean isFirstMove = true;
	boolean canEaten = false;
	public boolean flag = true;
	public GamePanel gamePanel;

	public Pawn(int i, int j, String color,GamePanel gamePanel) {
		super(i, j, color);
		this.gamePanel = gamePanel;
	}

	public ArrayList<int[]> getWays(){
		ArrayList<int[]> ways = new ArrayList<>();
		canEaten = false;

		if (color.equals("white")){
			//No pieces ahead, can move forward
			if (j - 1 >= 0 && GamePanel.chessObjects[i][j-1].isNull()){
				ways.add(new int[]{i,j-1});
			}
			//Front diagonal with an opponent's chess piece can be captured.
			if (i - 1 >= 0 && j - 1 >= 0 && !GamePanel.chessObjects[i-1][j-1].isNull()){
				ways.add(new int[]{i-1,j-1});
			}
			if (i + 1 <= 7 && j - 1 >= 0 && !GamePanel.chessObjects[i+1][j-1].isNull()){
				ways.add(new int[]{i+1,j-1});
			}

			if (isFirstMove && j - 2 >= 0 && GamePanel.chessObjects[i][j-1].isNull() && GamePanel.chessObjects[i][j-2].isNull()){
				ways.add(new int[]{i,j-2});
				canEaten = true;
			}
		}

		if (color.equals("black")){
			if (j + 1 <= 7 && GamePanel.chessObjects[i][j+1].isNull()){
				ways.add(new int[]{i,j+1});
			}
			//Front diagonal with an opponent's chess piece can be captured.
			if (i - 1 >= 0 && j + 1 <= 7 && !GamePanel.chessObjects[i-1][j+1].isNull()){
				ways.add(new int[]{i-1,j+1});
			}
			if (i + 1 <= 7 && j + 1 <= 7 && !GamePanel.chessObjects[i+1][j+1].isNull()){
				ways.add(new int[]{i+1,j+1});
			}

			if (isFirstMove && j + 2 <= 7 && GamePanel.chessObjects[i][j+1].isNull() && GamePanel.chessObjects[i][j+2].isNull()){
				ways.add(new int[]{i,j+2});
				canEaten = true;
			}
		}

		//吃过路兵
		if (i - 1 >= 0){
			ChessObject chessObject = GamePanel.chessObjects[i-1][j];
			if (chessObject.getClass().getSimpleName().contains("Pawn")){
				Pawn pawn = (Pawn) chessObject;
				if (pawn.canEaten){
					ways.add(new int[]{i-1,j});
				}
			}
		}

		if (i + 1 <= 7){
			ChessObject chessObject = GamePanel.chessObjects[i+1][j];
			if (chessObject.getClass().getSimpleName().contains("Pawn")){
				Pawn pawn = (Pawn) chessObject;
				if (pawn.canEaten){
					ways.add(new int[]{i+1,j});
				}
			}
		}

		//If there is no available path to move, return a null value to determine
		if (ways.size() == 0){
			return null;
		}
		return ways;
	}

	public void move(int i, int j) {
		//A pawn can only move one step forward after its first move.
		isFirstMove = false;
		super.move(i, j);

		//upgrade
		if (flag){
			if (color.equals("black") && j == 7){
				gamePanel.upgradePawn(this);
			}
			if (color.equals("white") && j == 0){
				gamePanel.upgradePawn(this);
			}
		}
	}
}
