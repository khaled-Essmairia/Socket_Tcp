package com.tekcreek.socketprg;

import java.net.Socket;

import com.tekcreek.socketprg.view.ChatFrame;

public class Client {

	public static void main(String[] args) throws Exception {
		
		Socket sock = new Socket("localhost", 9000);
		ChatFrame frame = new ChatFrame("Client:Window", sock);
		frame.setVisible(true);
	}

}
