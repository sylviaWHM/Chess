package model;

import view.GamePanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Robot {
	int round = 0;
	public ArrayList<ChessObject> chessList = new ArrayList<>();

	public void move(){
		check();
		Random random = new Random();
		//First 3-5 turns for pawn move or knight move strategy
		if (round < 3 + random.nextInt(2)){
			int c = random.nextInt(3);
			if (c <= 1){
				move("Pawn");
			}else {
				move("Knight");
			}
		}else {
			//Find if there is a move that can capture the opponent's piece
			if (eat()){
				return;
			}
			//If not, move pawn at random
			move("all");
		}
		round++;
	}

	public void check(){
		chessList.clear();
		//分别加入集合中
		for (ChessObject[] chessArray:GamePanel.chessObjects){
			for (ChessObject chessObject:chessArray){
				if (chessObject.isNull()){
					continue;
				}
				if (chessObject.color.equals("black")){
					chessList.add(chessObject);
				}
			}
		}
	}

	//Find if there is a move that captures the opponent's piece in all moves of all pieces. If true, move
	private boolean eat(){
		ChessObject[][] chessObjects = GamePanel.chessObjects;
		Collections.shuffle(chessList);
		for (ChessObject chessObject:chessList){
			ArrayList<int[]> ways = chessObject.getWays();
			if (ways == null){
				continue;
			}

			Collections.shuffle(ways);
			for (int[] way:ways){
				int i = way[0];
				int j = way[1];

				if (i < 0 || i > 7 || j < 0 || j > 7){
					continue;
				}
				//If it is not your own piece, can move
				if (!chessObjects[i][j].isNull() && !chessObject.color.equals(chessObjects[i][j].color)){
					chessObject.move(i,j);
					return true;
				}
			}
		}
		return false;
	}

	public void move(String name){
		Collections.shuffle(chessList);
		ChessObject[][] chessObjects = GamePanel.chessObjects;
		for (ChessObject chessObject:chessList){
			if (name.equals("all") || chessObject.getClass().getSimpleName().equals(name)){
				ArrayList<int[]> ways = chessObject.getWays();
				if (ways == null){
					continue;
				}
				Collections.shuffle(ways);

				for (int[] way:ways){
					int i = way[0];
					int j = way[1];
					//If it is not your own piece, can move
					if (!chessObject.color.equals(chessObjects[i][j].color)){
						chessObject.move(i,j);
						return;
					}
				}
			}
		}
	}

}
