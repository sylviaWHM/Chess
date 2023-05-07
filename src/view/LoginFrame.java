package view;

import com.formdev.flatlaf.FlatDarculaLaf;
import model.User;
import util.DbUtil;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {
	JPanel panel1,panel2;
	JLabel label1,label2,label3,label4;
	JTextField field1,field2,field3,field4;
	JButton button1,button2,button3,button4;
	DbUtil dbUtil;

	//Modify window style
	static{
		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Connecting to the database, loading windows
	public LoginFrame() {
		//Connect to the database
		dbUtil = new DbUtil();
		new Thread(() -> dbUtil.connect()).start();
		//Load window properties
		setTitle("login");
		setSize(450, 300);
		ImageUtil.setStyle(this);
		init();
		setVisible(true);
	}

	// Initial login and registration screen
	public void init() {
		panel1 = new JPanel(null);
		panel1.setBounds(0,0,getWidth(),getHeight());
		add(panel1);

		panel2 = new JPanel(null);
		panel2.setOpaque(false);
		panel2.setBounds(0,0,getWidth(),getHeight());
		add(panel2);
		setContentPane(panel1);

		JLabel title = new JLabel("chess", ImageUtil.logoIcon,SwingConstants.LEFT);
		title.setForeground(Color.ORANGE);
		title.setBounds(120,15,260,50);
		title.setFont(new Font("微软雅黑",Font.BOLD,40));
		panel1.add(title);

		label1 = new JLabel("Account:");
		label1.setForeground(Color.WHITE);
		label1.setBounds(125,90,90,30);
		label1.setFont(new Font("微软雅黑",Font.BOLD,16));
		panel1.add(label1);

		label2 = new JLabel("Password:");
		label2.setForeground(Color.WHITE);
		label2.setBounds(125,135,90,30);
		label2.setFont(new Font("微软雅黑",Font.BOLD,16));
		panel1.add(label2);

		label3 = new JLabel("Account:");
		label3.setForeground(Color.WHITE);
		label3.setBounds(125,85,90,30);
		label3.setFont(new Font("微软雅黑",Font.BOLD,16));
		panel2.add(label3);

		label4 = new JLabel("Password:");
		label4.setForeground(Color.WHITE);
		label4.setBounds(125,140,90,30);
		label4.setFont(new Font("微软雅黑",Font.BOLD,16));
		panel2.add(label4);

		field1 = new JTextField();
		field1.setText("Please enter the account number");
		field1.setBounds(225,90,150,36);
		panel1.add(field1);

		field2 = new JTextField();
		field2.setText("Please enter a password");
		field2.setBounds(225,140,150,36);
		panel1.add(field2);

		field3 = new JTextField();
		field3.setBounds(225,90,150,36);
		panel2.add(field3);

		field4 = new JTextField();
		field4.setBounds(225,140,150,36);
		panel2.add(field4);

		button1 = new JButton("login");
		button1.addActionListener(this);
		button1.setForeground(Color.ORANGE);
		button1.setBounds(120,200,120,35);
		panel1.add(button1);

		button2 = new JButton("register");
		button2.addActionListener(this);
		button2.setForeground(Color.ORANGE);
		button2.setBounds(250,200,120,35);
		panel1.add(button2);

		button3 = new JButton("Confirm");
		button3.addActionListener(this);
		button3.setForeground(Color.ORANGE);
		button3.setBounds(120,200,125,35);
		panel2.add(button3);

		button4 = new JButton("return");
		button4.addActionListener(this);
		button4.setForeground(Color.ORANGE);
		button4.setBounds(255,200,125,35);
		panel2.add(button4);
	}

	// Execution of operations
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == button1){
			login();
		}else if (source == button2){
			setContentPane(panel2);
		}else if (source == button3){
			register();
		}else if (source == button4){
			setContentPane(panel1);
		}
	}

	public void login(){
		String name = field1.getText();
		String password = field2.getText();
		//If the text box is empty
		if (name.equals("") || password.equals("")){
			JOptionPane.showMessageDialog(null,"Please enter your account number and password!");
		}else {
			User user = dbUtil.loginUser(name,password);
			//If name is empty
			if (user == null){
				JOptionPane.showMessageDialog(null,"Login failed! Wrong account or password!");
			}
			//Login successfully, enter the main screen
			else {
				new GameFrame(user);
				setVisible(false);
			}
		}
	}

	public void register(){
		String name = field3.getText();
		String password = field4.getText();
		//If the text box is empty
		if (name.equals("") || password.equals("")){
			JOptionPane.showMessageDialog(null,"Please enter all information!");
		}else {
			//Register successfully and return to the login screen
			if (dbUtil.addUser(name,password)){
				JOptionPane.showMessageDialog(null,"Registration successful!");
				field3.setText(null);
				field4.setText(null);
				setContentPane(panel1);
			}else {
				JOptionPane.showMessageDialog(null,"Registration failed! The account already exists!");
			}
		}
	}

	public static void main(String[] args) {
		new LoginFrame();
	}
}