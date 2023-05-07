package view;

import com.formdev.flatlaf.FlatDarculaLaf;
import model.User;
import server.ClientThread;
import util.DbUtil;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame implements ActionListener {
	User user;
	DbUtil dbUtil;
	JPanel panel;
	JButton button1;
	JButton button2;
	JButton button3;
	JButton button4;
	JButton button5;

	static {
		//Modify window style
		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GameFrame(User user) {
		this.user = user;
		dbUtil = new DbUtil();
		dbUtil.connect();
		setTitle("chess");
		setSize(650, 450);
		ImageUtil.setStyle(this);
		init();
		setVisible(true);
	}

	public void init() {
		panel = new JPanel(null);
		panel.setBounds(0,0,getWidth(),getHeight());
		add(panel);

		Font font = new Font("微软雅黑",Font.BOLD,24);
		button1 = new JButton("Online battle mode");
		button1.setFont(font);
		button1.addActionListener(this);
		button1.setBounds(80,30,490,50);
		panel.add(button1);

		button2 = new JButton("Two-player game mode");
		button2.setFont(font);
		button2.addActionListener(this);
		button2.setBounds(80,100,490,50);
		panel.add(button2);

		button3 = new JButton("Human-machine battle mode");
		button3.setFont(font);
		button3.addActionListener(this);
		button3.setBounds(80,170,490,50);
		panel.add(button3);

		button4 = new JButton("Ranking list");
		button4.setFont(font);
		button4.addActionListener(this);
		button4.setBounds(80,240,490,50);
		panel.add(button4);

		button5 = new JButton("Personal Center");
		button5.setFont(font);
		button5.addActionListener(this);
		button5.setBounds(80,310,490,50);
		panel.add(button5);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == button1) {
			new OnlineFrame(this,user);
		}
		else if (source == button2) {
			startGame(null,1,"white");
		}
		else if (source == button3) {
			startGame(null,2,"white");
		}
		else if (source == button4) {
			new RankFrame(this);
		}
		else if (source == button5) {
			new RecordFrame(this);
		}
		setVisible(false);
	}

	//type: 1：two-player 2：human vs machine
	public GamePanel startGame(ClientThread clientThread,int type,String color){
		JFrame frame = new JFrame("chess");
		frame.setSize(710,700);
		GamePanel gamePanel = new GamePanel(frame,this,user,type);
		gamePanel.color = color;
		gamePanel.clientThread = clientThread;
		frame.setContentPane(gamePanel);

		frame.setVisible(true);
		ImageUtil.setStyle(frame);
		return gamePanel;
	}

}