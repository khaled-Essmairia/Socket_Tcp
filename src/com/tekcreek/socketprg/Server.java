package com.tekcreek.socketprg;

import java.net.ServerSocket;
import java.net.Socket;

import com.tekcreek.socketprg.view.ChatFrame;

public class Server {

	public static void main(String[] args) throws Exception {
		
		ServerSocket ser = new ServerSocket(9000);
		while(true) {
			System.out.println("Waiting for client");
			Socket sock = ser.accept();
			ChatFrame frame = new ChatFrame("Server:Window", sock);
			frame.setVisible(true);
		}
	
	}

}
