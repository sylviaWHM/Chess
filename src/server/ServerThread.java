package server;

import com.alibaba.fastjson.JSON;
import model.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 服务端线程类
 */
public class ServerThread extends Thread{
	boolean isRunning;
	ServerStart server;
	Socket socket;
	DataInputStream input;
	DataOutputStream output;

	public ServerThread(ServerStart server,Socket socket){
		isRunning = true;
		this.server = server;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			while (isRunning){
				Message message = receiveMessage();
				if (server.thread1 == this){
					server.thread2.sendMessage(message);
				}else {
					server.thread1.sendMessage(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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


