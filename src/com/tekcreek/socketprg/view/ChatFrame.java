package com.tekcreek.socketprg.view;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	JTextArea textArea = new JTextArea();
	JTextField textField = new JTextField();
	JButton btnSend = new JButton("Send");
	
	private Socket sock;
	DataInputStream din;
	DataOutputStream dout;
	ReceiverThread receiver;
	
	public ChatFrame(String title, Socket sock) {
		setTitle(title);
		this.sock = sock;
		setupSocket();
		prepareFrame();
	}

	private void setupSocket() {
		// 1. Open Streams
		try {
			din = new DataInputStream(sock.getInputStream());
			dout = new DataOutputStream(sock.getOutputStream());
		}catch(Exception e) {}
		
		// 2. Start the receiver thread (for read)
		receiver = new ReceiverThread();
		receiver.start();
	}

	private void prepareFrame() {
		Container content = getContentPane();

		Box south = Box.createHorizontalBox();
		south.add(textField);
		south.add(btnSend);

		JScrollPane scrollPane = new JScrollPane(textArea);
		content.add(scrollPane, "Center");
		content.add(south, "South");

		setBounds(100, 100, 300, 300);
		setResizable(false);
		addWindowListener(new WindowHandler());
		btnSend.addActionListener(new SendHandler());

	}
	
	private void onMessage(String message) {
		if ("quit".equalsIgnoreCase(message)) {
			try {
				dout.writeUTF("quit");
				din.close();
				dout.close();
				sock.close();
			} catch(Exception e) {}
			dispose();
			receiver.interrupt();
		} else {
			String appendedText = textArea.getText() + "\n" + "Other:" + message;
			textArea.setText(appendedText);
		}
	}
	
	class WindowHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			try {
				dout.writeUTF("quit");
			}catch(Exception exp) {}
		}
	}
	
	class SendHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String message = textField.getText();
			String appendedText = textArea.getText() + "\n" + "You:" + message;
			textArea.setText(appendedText);
			
			try {
				dout.writeUTF(message);
			}catch(Exception exp) {}
		}
	}
	
	class ReceiverThread extends Thread {
		public void run() {
			while(! interrupted()) {
				try {
					String message = din.readUTF();
					onMessage(message);
				}catch(Exception e) {}
			}
		}
	}
}
