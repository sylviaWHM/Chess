package view;

import model.User;
import server.ClientThread;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;

public class OnlineFrame extends JFrame {
	JLabel label;
	JButton button1;
	JButton button2;
	public User user;
	ClientThread clientThread;
	public GameFrame gameFrame;

	public OnlineFrame(GameFrame gameFrame,User user){
		this.gameFrame = gameFrame;
		this.user = user;
		setTitle("Online battle mode");
		setSize(550,350);
		ImageUtil.setStyle(this);
		init();
		setVisible(true);
	}

	// Initialisation screen
	public void init(){
		label = new JLabel("Matching you with opponents");
		label.setVisible(false);
		label.setForeground(Color.ORANGE);
		label.setBounds(35,60,400,40);
		label.setFont(new Font("微软雅黑",Font.BOLD,30));
		add(label);

		button1 = new JButton("Match players");
		button1.setForeground(Color.ORANGE);
		button1.setBounds(115,210,320,40);
		button1.setFont(new Font("微软雅黑",Font.BOLD,20));
		add(button1);
		button1.addActionListener(e -> {
			connect();
			loadThread();
			label.setVisible(true);
			button1.setVisible(false);
		});

		button2 = new JButton("Return to the menu");
		button2.setForeground(Color.ORANGE);
		button2.setBounds(115,255,320,40);
		button2.setFont(new Font("微软雅黑",Font.BOLD,20));
		add(button2);
		button2.addActionListener(e -> {
			gameFrame.setVisible(true);
			dispose();
		});
	}

	public void connect(){
		clientThread = new ClientThread(this);
		clientThread.start();
	}

	public void recover(){
		label.setVisible(false);
		button1.setVisible(true);
	}

	// start game
	public GamePanel startGame(String color){
		GamePanel gamePanel = gameFrame.startGame(clientThread,3,color);
		dispose();
		return gamePanel;
	}

	// Match your opponent
	int num = 1;
	public void loadThread(){
		label.setText("Matching you with opponents");
		new Thread(() -> {
			while (true){
				try {
					Thread.sleep(500);
					label.setText(label.getText()+".");
					num++;
					if (num == 4){
						num = 1;
						label.setText("Matching you with opponents");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
