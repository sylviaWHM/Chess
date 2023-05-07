package server;

import com.alibaba.fastjson.JSON;
import model.*;
import view.GamePanel;
import view.OnlineFrame;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 客户端线程类
 */
public class ClientThread extends Thread{
	boolean isRunning;
	DataInputStream input;
	DataOutputStream output;
	OnlineFrame onlineFrame;
	GamePanel gamePanel;

	public ClientThread(OnlineFrame onlineFrame){
		this.onlineFrame = onlineFrame;
		isRunning = true;
	}

	@Override
	public void run() {
		try {
			Socket socket = new Socket("localhost",8899);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			//传输自己的名称
			output.writeUTF(onlineFrame.user.name);

			while (isRunning){
				Message message = receiveMessage();
				if (message.type == MsgType.START_GAME){
					String[] data = message.content.split("[@]");
					gamePanel = onlineFrame.startGame(data[0]);
					gamePanel.otherName = data[1];
					gamePanel.otherNameLabel.setText("<html>rival:<br>" + data[1] + "<html>");
				}
				else if (message.type == MsgType.PLAY_CHESS){
					int[] loc = message.loc;
					ChessObject chessObject = GamePanel.chessObjects[loc[0]][loc[1]];
					if (chessObject.getClass().getSimpleName().equals("Pawn")){
						Pawn pawn = (Pawn) chessObject;
						pawn.flag = false;
					}
					chessObject.move(loc[2],loc[3]);
					gamePanel.nextRound();
				}
				else if (message.type == MsgType.UPGRADE_PAWN){
					int i = message.loc[0];
					int j = message.loc[1];
					String chessName = message.chessName;
					ChessObject pawn = GamePanel.chessObjects[i][j];
					String color = pawn.color;

					switch (chessName) {
						case "Queen":
							pawn = new Queen(i, j, color);
							break;
						case "Rook":
							pawn = new Rook(i, j, color);
							break;
						case "Bishop":
							pawn = new Bishop(i, j, color);
							break;
						case "Knight":
							pawn = new Knight(i, j, color);
							break;
					}
					GamePanel.chessObjects[i][j] = pawn;
				}
				else if (message.type == MsgType.ADMIT_DEFEAT){
					gamePanel.gameOver(true);
				}
			}
		} catch (IOException e) {
			onlineFrame.recover();
			JOptionPane.showMessageDialog(null,"Failed to connect to server!");
		}
	}

	public Message receiveMessage() throws IOException {
		String data = input.readUTF();
		return JSON.parseObject(data,Message.class);
	}

	public void sendMessage(Message message) {
		try {
			String data = JSON.toJSONString(message);
			output.writeUTF(data);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
