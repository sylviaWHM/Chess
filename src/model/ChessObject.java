package model;

import util.ImageUtil;
import view.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * 棋类
 */
public class ChessObject {
	public int x;
	public int y;
	public int i;
	public int j;
	public String color;
	BufferedImage image;
	public boolean isChoose = false;
	public boolean isTip = false;

	public ChessObject(int i, int j, String color) {
		this.i = i;
		this.j = j;
		this.color = color;
		this.image = ImageUtil.getImage("images/" + color + "_" + getClass().getSimpleName() + ".png");
	}


	public ChessObject(int i, int j){
		this.i = i;
		this.j = j;
	}

	public void draw(Graphics g){
		x = 19 + i *56;
		y = 16 + j *56;
		g.drawImage(image,x,y,55,55,null);
		if (isChoose){
			g.setColor(Color.GREEN);
			g.drawRect(x,y,54,54);
			g.drawRect(x+1,y+1,52,52);
		}
		if (isTip){
			g.setColor(Color.GREEN);
			g.fillOval(x + 18,y + 18,20,20);
		}
	}

	//Move a piece to a point
	public void move(int i,int j){
		GamePanel.chessObjects[this.i][this.j] = new ChessObject(this.i,this.j);
		this.i = i;
		this.j = j;
		GamePanel.chessObjects[i][j] = this;
		isChoose = false;
	}

	//Check if it is an empty chess piece, meaning that it is just a grid used to display the possible paths.
	public boolean isNull(){
		return color == null || color.equals("null");
	}

	public boolean isInChess(int i,int j){
		return i >= 0 && i <= 7 && j >= 0 && j <= 7;
	}

	//Within mouse range
	public boolean isLocated(int x, int y){
		return x > this.x && x < this.x + 55 && y > this.y && y < this.y + 55;
	}

	//Get a walkable path
	public ArrayList<int[]> getWays(){
		return null;
	}

	@Override
	public String toString() {
		return "{" + "i=" + i + ", j=" + j + ", color=" + color + ", name=" + this.getClass().getSimpleName() + '}';
	}
}
