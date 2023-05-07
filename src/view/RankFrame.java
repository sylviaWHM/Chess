package view;

import util.ImageUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class RankFrame extends JFrame {
	GameFrame gameFrame;

	public RankFrame(GameFrame gameFrame){
		this.gameFrame = gameFrame;
		setTitle("list");
		setSize(500,500);
		ImageUtil.setStyle(this);
		ImageUtil.addBackground(this);
		init();
		setVisible(true);
	}

	public void init(){
		//Set the default table model to be added to the table
		Object[] columnNames = {"ranking","Player name","Win rate"};
		DefaultTableModel model = new DefaultTableModel(null,columnNames);
		JTable table = new JTable(model);

		//Centering the contents of a cell
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class,renderer);

		//Create a scrolling panel, put the table into the scrolling panel
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(50,20,400,360);
		add(scrollPane);

		//Importing data to a table
		ArrayList<String[]> rankLevel = gameFrame.dbUtil.getRankLevel();
		for (int i = 0; i < rankLevel.size(); i++) {
			String[] data = rankLevel.get(i);
			String winRate;
			if (data[1] == null){
				winRate = "0%";
			}else {
				double rate = Double.parseDouble(data[1])*100;
				winRate = String.format("%.2f",rate) + "%";
			}
			model.addRow(new Object[]{i+1,data[0],winRate});
		}

		JButton button = new JButton("Go back to the menu");
		button.setBounds(100,410,300,40);
		add(button);
		button.addActionListener(e -> {
			gameFrame.setVisible(true);
			dispose();
		});
	}

}
