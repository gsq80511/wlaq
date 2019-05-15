package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import C_kerberos.socket;

import javax.swing.JPasswordField;
import javax.swing.JPanel;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;
import java.awt.TextField;

public class ClientUi {

	private JFrame frmKeberos;
	private JTextField textField;
	public TextField textField_1;
	String IP = "172.20.10.7";
	int port = 8087;
	public String username;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientUi window = new ClientUi();
					window.frmKeberos.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientUi() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmKeberos = new JFrame();
		frmKeberos.getContentPane().setBackground(Color.WHITE);
		frmKeberos.setTitle("Keberos客户端");
		frmKeberos.setBounds(100, 100, 412, 300);
		frmKeberos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmKeberos.getContentPane().setLayout(null);

		JButton btnNewButton_2 = new JButton("用户注册");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField.getText() != null && textField_1.getText() != null) {
					send();// 发送注册信息
				}

			}
		});
		btnNewButton_2.setBounds(68, 154, 113, 27);
		frmKeberos.getContentPane().add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("用户登陆");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField.getText() != null && textField_1.getText() != null) {
					send2();// 发送登陆信息
				}
			}
		});
		btnNewButton_3.setBounds(212, 154, 113, 27);
		frmKeberos.getContentPane().add(btnNewButton_3);

		JLabel label = new JLabel("用户名");
		label.setBounds(52, 46, 72, 18);
		frmKeberos.getContentPane().add(label);

		JLabel label_1 = new JLabel("  密码");
		label_1.setBounds(52, 98, 72, 18);
		frmKeberos.getContentPane().add(label_1);

		textField = new JTextField();
		textField.setBounds(123, 43, 203, 24);
		frmKeberos.getContentPane().add(textField);
		textField.setColumns(10);

		textField_1 = new TextField();
		textField_1.setBounds(123, 98, 202, 25);
		frmKeberos.getContentPane().add(textField_1);
	}

	public void send()// 发送注册消息
	{
		try {
			System.out.println(" 用户注册");
			Socket socket = new Socket(IP, port);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			// System.out.println(" 用户注册2");
			String hashID = hash(textField.getText());
			pw.println("* " + hashID + " " + textField_1.getText());
			pw.flush();
			System.out.println("* " + hashID + " " + textField_1.getText());
			while (true) {
				String str = br.readLine();
				if (str.equals("0"))// 注册成功
				{
					username = textField.getText();
					prompt window1 = new prompt();
					window1.frame.setVisible(true);
					window1.textArea.append(" \n   注册成功！");
					Thread.sleep(10000);
					textField.setText("");
					textField.requestFocus();// 重置文本框
					textField_1.setText("");
					textField_1.requestFocus();// 重置文本框
					pw.close();
					br.close();
					socket.close();
					break;
				} else if (str.equals("1"))// 用户已存在
				{
					prompt window1 = new prompt();
					window1.frame.setVisible(true);
					window1.textArea.append(" \n 用户已存在！");
					Thread.sleep(10000);
					textField.setText("");
					textField.requestFocus();// 重置文本框
					textField_1.setText("");
					textField_1.requestFocus();// 重置文本框
					pw.close();
					br.close();
					socket.close();
					break;
				}
				br.close();
				pw.close();
				socket.close();
				break;

			}

		} catch (Exception e1) {
			System.out.println("异常:" + e1.getMessage());
		}

	}

	public void send2()// 发送登陆消息
	{
		try {
			System.out.println(" 用户登陆");
			Socket socket = new Socket(IP, port);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			// System.out.println(" 用户登陆2");
			System.out.println("Socket=" + socket);
			username = textField.getText();
			String hashID = hash(textField.getText());
			pw.println("$ " + hashID + " " + textField_1.getText());
			pw.flush();
			System.out.println("* " + hashID + " " + textField_1.getText());
			while (true) {
				String str = br.readLine();
				System.out.println(str);
				String str2 = str.substring(0, 1);
				if (str2.equals("*"))// 密码错误
				{
					prompt window1 = new prompt();
					window1.frame.setVisible(true);
					window1.textArea.append(" \n   密码错误！");
					textField_1.setText("");
					textField_1.requestFocus();// 重置文本框
					pw.close();
					br.close();
					socket.close();
					break;
				} else if (str2.equals("$"))// 用户不存在
				{
					prompt window1 = new prompt();
					window1.frame.setVisible(true);
					window1.textArea.append(" \n用户名不存在！");
					textField.setText("");
					textField.requestFocus();// 重置文本框
					textField_1.setText("");
					textField_1.requestFocus();// 重置文本框
					pw.close();
					br.close();
					socket.close();
					break;
				} else {

					pw.close();
					br.close();
					socket.close();
					socket a = new socket(hashID, textField_1.getText());// 认证过程
					System.out.println(a.judge);
					if (a.judge.equals("true")) {
						frmKeberos.dispose();// 关闭登陆界面
						new client(username);
					}
					break;
				}

			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// 将输入的用户名hash成定长4位ID
	public static String hash(String Secret) {
		String str = Secret;
		int hash = str.hashCode();
		String str2 = null;
		int j = Math.abs(hash);
		Integer k = j;

		if (k.toString().length() == 4) {
			str2 = k.toString();
		}

		if (k.toString().length() < 4) {
			str2 = "0" + k.toString();
			while (str2.length() < 4) {
				str2 = "0" + str2;

			}
		}

		if (k.toString().length() > 4) {
			str2 = k.toString().substring(0, 4);
		}

		return str2;

	}

}
