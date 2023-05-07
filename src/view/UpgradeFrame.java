package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class UpgradeFrame extends JFrame implements ActionListener {
	JRadioButton radioButton1;
	JRadioButton radioButton2;
	JRadioButton radioButton3;
	JRadioButton radioButton4;
	ChessObject pawn;
	GamePanel gamePanel;

	public UpgradeFrame(int x,int y, ChessObject pawn,GamePanel gamePanel){
		this.pawn = pawn;
		this.gamePanel = gamePanel;
		setLayout(null);
		setUndecorated(true);
		setSize(370,200);
		setLocation(x,y);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
		JPanel panel = (JPanel) getContentPane();
		panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		init();
		setVisible(true);
	}

	public void init(){
		JLabel label = new JLabel("You choose to upgrade to:");
		label.setFont(new Font("微软雅黑",Font.BOLD,22));
		label.setBounds(40,20,320,55);
		add(label);

		Font font = new Font("微软雅黑",Font.BOLD,15);
		radioButton1 = new JRadioButton("Queen");
		radioButton1.setFont(font);
		radioButton1.addActionListener(this);
		radioButton1.setBounds(60,80,85,40);
		add(radioButton1);

		radioButton2 = new JRadioButton("Rook");
		radioButton2.setFont(font);
		radioButton2.addActionListener(this);
		radioButton2.setBounds(220,80,88,40);
		add(radioButton2);

		radioButton3 = new JRadioButton("Bishop");
		radioButton3.setFont(font);
		radioButton3.addActionListener(this);
		radioButton3.setBounds(60,140,88,40);
		add(radioButton3);

		radioButton4 = new JRadioButton("Knight");
		radioButton4.setFont(font);
		radioButton4.addActionListener(this);
		radioButton4.setBounds(220,140,88,40);
		add(radioButton4);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		int i = pawn.i;
		int j = pawn.j;
		String color = pawn.color;
		if (source == radioButton1){
			pawn = new Queen(i,j,color);
		}
		else if (source == radioButton2){
			pawn = new Rook(i,j,color);
		}
		else if (source == radioButton3){
			pawn = new Bishop(i,j,color);
		}
		else if (source == radioButton4){
			pawn = new Knight(i,j,color);
		}
		//Update in the chessboard
		GamePanel.chessObjects[i][j] = pawn;
		//If in online mode, update in another client as well
		if (gamePanel.type == 3){
			Message message = new Message();
			message.type = MsgType.UPGRADE_PAWN;
			message.loc = new int[]{pawn.i,pawn.j};
			message.chessName = pawn.getClass().getSimpleName();
			gamePanel.clientThread.sendMessage(message);
		}
		dispose();
	}

}
