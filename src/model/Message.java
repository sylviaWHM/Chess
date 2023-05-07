package model;

public class Message {
	public MsgType type;
	public String content;
	public String chessName;
	//第1,2个参数为移动前的i,j 第3,4个为移动后的i,j
	public int[] loc;

	public Message() {

	}
}
