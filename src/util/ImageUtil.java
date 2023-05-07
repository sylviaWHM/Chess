package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
	public final static Color color = new Color(20,102,60);
	public final static BufferedImage logo = getImage("images/icon.png");
	public final static BufferedImage back = getImage("images/back.png");
	public final static ImageIcon logoIcon = new ImageIcon("images/logo.png");
	public final static ImageIcon backIcon = new ImageIcon("images/back.jpg");

	public static void setStyle(JFrame frame){
		//Sets the default layout of the window. Size cannot be modified. Display in the middle of the screen. Set the logo.
		frame.setLayout(null);
		frame.setResizable(false);
		//Definition Toolkit
		Toolkit kit = Toolkit.getDefaultToolkit();
		//Get the size of the screen
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		frame.setIconImage(ImageUtil.logo);
		//Set window centering
		frame.setLocation(screenWidth/2-frame.getWidth()/2, screenHeight/2-frame.getHeight()/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void addBackground(JFrame frame){
		JLabel label = new JLabel(backIcon);
		label.setBounds(0,0,frame.getWidth(),frame.getHeight());
		frame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		((JPanel)frame.getContentPane()).setOpaque(false); //Set transparency
	}

	public static BufferedImage getImage(String filename){
		try {
			return ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
