package Client;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import java.awt.ScrollPane;
import java.awt.TextArea;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class ChatRoom {

	public JFrame frame;
	public JTextField textField;
	public JTextField textField_2;
	public JButton btnNewButton;
	public JLabel label_1;
	public ScrollPane scrollPane_1;
	public JButton button_1;
	public JButton btnNewButton_1;
	public TextArea txtMsg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoom window = new ChatRoom();
					window.frame.setVisible(true);
					// window.connect();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatRoom() {
		initialize();
		// frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(173, 216, 230));
		frame.setForeground(new Color(175, 238, 238));
		frame.setTitle("聊天室");
		frame.setBounds(100, 100, 672, 558);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnNewButton = new JButton("\u7528\u6237\u540D");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(10, 8, 84, 42);
		frame.getContentPane().add(btnNewButton);

		textField = new JTextField();
		textField.setBounds(108, 9, 223, 41);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		label_1 = new JLabel("信息显示");
		label_1.setForeground(Color.DARK_GRAY);
		label_1.setBounds(14, 63, 72, 18);
		frame.getContentPane().add(label_1);

		scrollPane_1 = new ScrollPane();
		scrollPane_1.setBackground(new Color(250, 235, 215));
		scrollPane_1.setForeground(new Color(240, 255, 240));
		scrollPane_1.setBounds(10, 97, 622, 312);
		frame.getContentPane().add(scrollPane_1);

		txtMsg = new TextArea();// 构造一个新的文本区
		txtMsg.setBackground(Color.WHITE);
		txtMsg.setForeground(Color.BLUE);
		txtMsg.setFont(new Font("黑体", Font.BOLD, 20));
		scrollPane_1.add(txtMsg);
		// txtMsg.append("*****聊天室消息记录*****\n");

		button_1 = new JButton("发送");
		button_1.setBackground(Color.WHITE);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// textField_2.getText();
			}
		});
		button_1.setBounds(493, 453, 108, 45);
		frame.getContentPane().add(button_1);

		textField_2 = new JTextField();
		textField_2.setBounds(14, 453, 422, 45);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);

		btnNewButton_1 = new JButton("退出");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnNewButton_1.setBounds(493, 13, 86, 42);
		frame.getContentPane().add(btnNewButton_1);
	}
}
