package view;

import model.Rank;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;

public class RecordFrame extends JFrame {
	GameFrame gameFrame;

	public RecordFrame(GameFrame gameFrame){
		this.gameFrame = gameFrame;
		setTitle("Personal Center");
		setSize(500,500);
		ImageUtil.setStyle(this);
		ImageUtil.addBackground(this);
		init();
		setVisible(true);
	}

	public void init(){
		DefaultListModel<Rank> model = new DefaultListModel<>();
		JList<Rank> list = new JList<>(model);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(75,50,350,300);
		add(scrollPane);

		list.setFixedCellHeight(40);
		CellRenderer cellRenderer = new CellRenderer();
		list.setCellRenderer(cellRenderer);

		//add data
		for (Rank rank:gameFrame.dbUtil.getAllRank(gameFrame.user.name)){
			model.addElement(rank);
		}
		JButton button = new JButton("Go back to the menu");
		button.setBounds(100,410,300,40);
		add(button);
		button.addActionListener(e -> {
			gameFrame.setVisible(true);
			dispose();
		});
	}

	static class CellRenderer implements ListCellRenderer<Rank> {
		@Override
		public Component getListCellRendererComponent(JList<? extends Rank> list, Rank value, int index, boolean isSelected, boolean cellHasFocus) {
			JPanel panel = new JPanel(null);

			JLabel name = new JLabel(value.name + " vs " + value.otherName);
			name.setBounds(20,5,140,30);
			name.setForeground(Color.ORANGE);
			name.setFont(new Font("黑体",Font.BOLD,22));
			panel.add(name);

			JLabel result = new JLabel(value.result);
			result.setBounds(180,5,80,30);
			result.setFont(new Font("微软雅黑",Font.BOLD,18));
			if (result.getText().equals("victory")){
				result.setForeground(Color.BLUE);
			}else {
				result.setForeground(Color.RED);
			}
			panel.add(result);

			JLabel time = new JLabel(value.date);
			time.setBounds(250,5,150,30);
			time.setFont(new Font("黑体",Font.BOLD,14));
			panel.add(time);

			if(isSelected){
				panel.setBackground(Color.GRAY);
			}
			return panel;
		}
	}

}
