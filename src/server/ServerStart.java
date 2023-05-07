package server;

import model.Message;
import model.MsgType;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStart extends Thread{
	public ServerThread thread1;
	public ServerThread thread2;

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(8899);
			while (true){
				//User1
				Socket socket = serverSocket.accept();
				System.out.println("There is a client connection...");
				thread1 = new ServerThread(this,socket);
				thread1.start();
				DataInputStream input = new DataInputStream(socket.getInputStream());
				String name1 = input.readUTF();
				//User2
				socket = serverSocket.accept();
				System.out.println("There is a client connection...");
				thread2 = new ServerThread(this,socket);
				thread2.start();
				input = new DataInputStream(socket.getInputStream());
				String name2 = input.readUTF();

				//Start the game.The first player who connects to the server will play as the white side, while the subsequent player will play as the black side.
				Message message = new Message();
				message.type = MsgType.START_GAME;
				message.content = "white@" + name2;
				thread1.sendMessage(message);
				message.content = "black@" + name1;
				thread2.sendMessage(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ServerStart().start();
	}
}
