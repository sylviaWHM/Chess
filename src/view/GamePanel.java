package view;

import model.Robot;
import model.*;
import server.ClientThread;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// 游戏面板
public class GamePanel extends JPanel {
	JFrame frame;
	GameFrame gameFrame;
	JButton button1,button2,button3;

	public String otherName;//Opponent's name
	public JLabel otherNameLabel;

	Graphics gImage;
	BufferedImage bufferedImage;
	public ClientThread clientThread;

	User user;
	Robot robot;
	int type;//1：two-player 2：human vs machine 3：online matching
	String tip;//Game tips
	String color;//The color of the player's chess pieces.
	boolean isPlaying = true;
	ChessObject chooseChess;//Pieces currently selected
	public static ChessObject[][] chessObjects = new ChessObject[8][8];

	public GamePanel(JFrame frame,GameFrame gameFrame,User user,int type) {
		this.frame = frame;
		this.gameFrame = gameFrame;
		this.user = user;
		this.type = type;
		if (type == 2){
			robot = new Robot();
		}
		initComponent();// Initialising components
		initChess();// Initialising the pieces
		startGame();
		addListener();
		addPaintThread();
	}

	//加载控件
	private void initComponent() {
		setLayout(null);
		otherNameLabel = new JLabel();
		otherNameLabel.setForeground(Color.ORANGE);
		otherNameLabel.setBounds(490,30,100,80);
		otherNameLabel.setFont(new Font("微软雅黑",Font.BOLD,20));
		add(otherNameLabel);

		button1 = new JButton("resume");
		button1.setBounds(500,440,190,35);
		add(button1);
		button1.addActionListener(e -> {
			isPlaying = true;
			initChess();
			startGame();
		});

		button2 = new JButton("Admit defeat");
		button2.setBounds(500,480,190,35);
		add(button2);
		button2.addActionListener(e -> {
			//If the game is over, you can no longer admit defeat.
			if (!isPlaying){
				return;
			}
			isPlaying = false;
			if (tip.equals("Black Chess Round")){
				tip = "White side wins the game!";
			}else {
				tip = "Black wins the game!";
			}
			if (clientThread != null){
				Message message = new Message();
				message.type = MsgType.ADMIT_DEFEAT;
				clientThread.sendMessage(message);
				gameOver(false);
			}
		});

		button3 = new JButton("menu");
		button3.setBounds(500,520,190,35);
		add(button3);
		button3.addActionListener(e -> {
			//Set robot to null
			robot = null;
			frame.dispose();
			gameFrame.setVisible(true);
		});

		//Online mode does not allow going back to the menu and restarting the game
		if (type == 3){
			button1.setVisible(false);
			button3.setVisible(false);
		}
	}

	//Adding painting thread
	private void addPaintThread(){
		new Thread(() -> {
			while (true){
				try {
					repaint();
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	//start game
	public void startGame(){
		tip = "Black Chess Round";
		nextRound();
	}

	//End of the game and writing of the game results to the database (online mode only)
	public void gameOver(boolean isWin){
		isPlaying = false;
		button3.setVisible(true);
		if (isWin){
			tip = "You've won!";
		}else {
			tip = "You failed!";
		}

		//Add a record
		String result;
		if (isWin){
			result = "victory";
		}else {
			result = "fail";
		}
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Rank rank = new Rank(user.name,otherName,result,format.format(date));
		gameFrame.dbUtil.addRank(rank);
		//Update the winning percentage
		gameFrame.dbUtil.updateWinRate(user.name);
	}

	//Next turn
	public void nextRound(){
		checkGame();
		if (tip.equals("White chess rounds")){
			tip = "Black Chess Round";
			if (robot != null){
				new Thread(() -> {
					try {
						Thread.sleep(300);
						robot.move();
						nextRound();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}).start();
			}
		}else if (tip.equals("Black Chess Round")){
			tip = "White chess rounds";
		}
	}

	private void checkGame(){
		int i = 0,j = 0;
		for (ChessObject[] chessArray:chessObjects){
			for (ChessObject chessObject:chessArray){
				//If there is still a black king on the board
				if (chessObject.getClass().getSimpleName().equals("King") && chessObject.color.equals("white")) {
					i = 1;
				}
				//If there is still a white king on the board
				if (chessObject.getClass().getSimpleName().equals("King") && chessObject.color.equals("black")) {
					j = 1;
				}
			}
		}
		if (type != 3){
			if (i == 1 && j == 0){
				tip = "White side wins the game!";
				JOptionPane.showMessageDialog(frame,tip);
			}else if (i == 0 && j == 1){
				tip = "Black wins the game!";
				JOptionPane.showMessageDialog(frame,tip);
			}
		}
		//In case of two-player mode
		else {
			if (i == 1 && j == 0 && color.equals("white") || i == 0 && j == 1 && color.equals("black")){
				tip = "You win the game!";
				gameOver(true);
				JOptionPane.showMessageDialog(frame,tip);
			}else if (i == 1 && j == 0 && color.equals("black") || i == 0 && j == 1 && color.equals("white")){
				tip = "You failed!!!";
				gameOver(false);
				JOptionPane.showMessageDialog(frame,tip);
			}
		}
	}

	private void addListener() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//Deselect all chess pieces first.
				for (ChessObject[] chessArray:chessObjects){
					for (ChessObject chessObject:chessArray){
						chessObject.isChoose = false;
					}
				}

				int x = e.getX();
				int y = e.getY();
				for (ChessObject[] chessArray:chessObjects){
					for (ChessObject chessObject:chessArray){
						if (!isPlaying){
							return;
						}
						if (!chessObject.isLocated(x,y)){
							continue;
						}

						System.out.println(chessObject);
						//Move the chess piece and cancel the selection if selecting a valid path, and clear all hints.
						if (chooseChess != null && chessObject.isTip){
							int oldI = chooseChess.i;
							int oldJ = chooseChess.j;
							int i = chessObject.i;
							int j = chessObject.j;
							chooseChess.move(i,j);
							chooseChess = null;

							clearTip();
							nextRound();
							if (clientThread != null){
								Message message = new Message();
								message.type = MsgType.PLAY_CHESS;
								message.loc = new int[]{oldI,oldJ,i,j};
								clientThread.sendMessage(message);
							}
							return;
						}

						//Suitable for online player mode and player mode Only move your own pieces on your own turn
						if (type != 1){
							if (color.equals("black") && tip.equals("White chess rounds")){
								continue;
							}
							if (color.equals("white") && tip.equals("Black Chess Round")){
								continue;
							}
						}
						if (tip.equals("White chess rounds") && chessObject.color != null && chessObject.color.equals("black")){
							continue;
						}
						if (tip.equals("Black Chess Round") && chessObject.color != null && chessObject.color.equals("white")){
							continue;
						}
						//If an empty piece is selected, but not the suggested path, all hints are removed and the selected piece is cancelled.
						if (chooseChess != null && chessObject.isNull() && !chessObject.isTip){
							clearTip();
							chooseChess.isChoose = false;
							chooseChess = null;
							return;
						}

						//Pieces selected，Clear all tips
						if (!chessObject.isNull()){
							chessObject.isChoose = !chessObject.isChoose;
							clearTip();
							//Show the moves available for this piece
							ArrayList<int[]> ways = chessObject.getWays();
							if (chessObject.isChoose && ways != null){
								chooseChess = chessObject;
								for (int[] way:ways){
									int i = way[0];
									int j = way[1];
									//If it is not our piece, a tip is shown that it can be moved
									if (!chooseChess.color.equals(chessObjects[i][j].color)){
										chessObjects[i][j].isTip = true;
									}
								}
								return;
							}
						}
					}
				}
				clearTip();
			}
		});
	}

	// Initializing Pieces
	public void initChess(){
		//First create empty pieces for an 8x8 board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				chessObjects[i][j] = new ChessObject(i,j);
			}
		}
		for (int i = 0; i < 8; i++) {
			chessObjects[i][1] = new Pawn(i,1,"black",this);
			chessObjects[i][6] = new Pawn(i,6,"white",this);
		}
		chessObjects[0][0] = new Rook(0,0,"black");
		chessObjects[7][0] = new Rook(7,0,"black");
		chessObjects[0][7] = new Rook(0,7,"white");
		chessObjects[7][7] = new Rook(7,7,"white");

		chessObjects[1][0] = new Knight(1,0,"black");
		chessObjects[6][0] = new Knight(6,0,"black");
		chessObjects[1][7] = new Knight(1,7,"white");
		chessObjects[6][7] = new Knight(6,7,"white");

		chessObjects[2][0] = new Bishop(2,0,"black");
		chessObjects[5][0] = new Bishop(5,0,"black");
		chessObjects[2][7] = new Bishop(2,7,"white");
		chessObjects[5][7] = new Bishop(5,7,"white");

		chessObjects[3][0] = new Queen(3,0,"black");
		chessObjects[3][7] = new Queen(3,7,"white");

		chessObjects[4][0] = new King(4,0,"black");
		chessObjects[4][7] = new King(4,7,"white");
	}

	//Delete all tips
	public void clearTip(){
		for (ChessObject[] chessArray1:chessObjects){
			for (ChessObject chessObject1:chessArray1){
				chessObject1.isTip = false;
			}
		}
	}

	//pawn moves to the last line of the opponent side for an upgrade
	public void upgradePawn(ChessObject pawn){
		//If the piece is a robot piece, it is automatically upgrade to queen
		if (robot != null && pawn.color.equals("black")){
			pawn = new Queen(pawn.i,pawn.j,pawn.color);
			chessObjects[pawn.i][pawn.j] = pawn;
			return;
		}
		new UpgradeFrame(frame.getX()+170,frame.getY()+200,pawn,this);
	}

	public void paint(Graphics g){
		bufferedImage = (BufferedImage) createImage(getWidth(),getHeight());
		gImage = bufferedImage.getGraphics();

		Graphics2D g2d = (Graphics2D) gImage;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		gImage.drawImage(ImageUtil.back,0,0,null);
		for (ChessObject[] chessArray:chessObjects){
			for (ChessObject chessObject:chessArray){
				chessObject.draw(gImage);
			}
		}
		//Drawing status box
		gImage.setColor(Color.GRAY);
		gImage.fillRect(0,485,485,75);
		gImage.setColor(Color.BLACK);
		gImage.drawRect(0,485,484,75);
		gImage.drawRect(1,486,482,73);

		gImage.setColor(Color.BLACK);
		gImage.setFont(new Font("微软雅黑",Font.BOLD,15));
		if (color.equals("white")){
			gImage.drawString("You are the white chess square",200,550);
		}else{
			gImage.drawString("You are the black chess side",200,550);
		}

		gImage.setColor(ImageUtil.color);
		gImage.setFont(new Font("微软雅黑",Font.BOLD,30));
		gImage.drawString(tip,180-6*(tip.length()-4),530);

		g.drawImage(bufferedImage,0,0,null);
		super.paintChildren(g);
	}
}
