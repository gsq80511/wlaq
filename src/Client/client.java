package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class client {
	String IP = "172.20.10.7";
	int port = 7777;
	Socket socket = null;
	BufferedReader br = null;
	PrintWriter pw = null;
	boolean canWaiter = true;
	ChatRoom window = new ChatRoom();
	Waiter waiter;// 创建一个线程
	public String username;

	public client(String name) {
		username = name;
		while (username.length() != 6) {
			username += " ";
		}
		// window.txtMsg.append("vicvuewf");
		window.frame.setVisible(true);//
		try {
			// 客户端socket指定服务器的地址和端口号
			socket = new Socket(IP, port);
			System.out.println("Socket=" + socket);////////

			/* 建立一个客户端UI */

			// 同服务器原理一样
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

			waiter = new Waiter();// 建立线程
			waiter.start();// 启动线程
			pw.println(username + "进入聊天室");
			pw.flush();
			window.button_1.addActionListener(new ActionListener() {// 界面触发事件,发送消息
				public void actionPerformed(ActionEvent e) {
					System.out.println(window.textField_2.getText());
					// window.txtMsg.append(window.textField_2.getText());
					sendMsg(window.textField_2.getText());
					window.textField_2.setText("");
					window.textField_2.requestFocus();// 重置文本框
				}
			});
			window.btnNewButton_1.addActionListener(new ActionListener() {// 退出
				public void actionPerformed(ActionEvent e) {
					window.frame.dispose();
					sendexit("退出聊天室");

				}
			});
			window.btnNewButton.addActionListener(new ActionListener() {// 显示用户名
				public void actionPerformed(ActionEvent e) {
					window.textField.setText(username);
				}
			});
//			pw.println("END");
//			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMsg(String str) {// 发送消息
		try {
			SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");// 输出实时时间
			String time = formater.format(new Date());
			//pw.println(username + ":" + time + "  " + str);// 向服务器发送消息
			// pw.println(time+" "+str);//向服务器发送消息
			pw.flush();
		} catch (Exception ee) {
			JOptionPane.showMessageDialog(null, "发送消息失败！");
		}
	}

	private void sendexit(String str) {// 退出
		try {
			pw.println(username + str);// 向服务器发送消息
//			canWaiter=false;
			pw.flush();
//			pw.close();
//			br.close();
//			socket.close();
		} catch (Exception ee) {
			JOptionPane.showMessageDialog(null, "发送消息失败！");
		}
	}

	private class Waiter extends Thread {
		public void run() {
			// window.frame.setVisible(true);
			while (canWaiter) {
				String msg;
				try {
					msg = br.readLine();
					if (msg.equals("exit"))// 接收Application断开连接回应
					{
						break;
					}
					// 复制输入流上的内容
					if (!msg.equals(null))
						window.txtMsg.append(msg + "\n");
					System.out.println(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// txtMsg.append(time+" "+name1+"："+msg+"\n");
			}
		}
	}

	public static void main(String[] args) {

		// new client("df");

	}

}
