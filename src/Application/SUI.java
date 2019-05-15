package Application;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.List;
import java.awt.Choice;
import java.awt.TextField;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Panel;
import java.awt.Color;
import javax.swing.JTextArea;

public class SUI {

	JFrame frame;
	JLabel label;
	ScrollPane scrollPane_1;
	TextField textField;
	JButton btnip;
	TextField textField_1;
	JButton button_1;
	JButton button;
	TextArea txtMsg1;

	/**
	 * Launch the application.
	 * 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SUI window = new SUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(222, 111, 208));
		frame.getContentPane().setBackground(new Color(188, 182, 193));
		frame.setTitle("应用服务器");
		frame.setBounds(150, 150, 743, 465);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		txtMsg1 = new TextArea();// 构造一个新的文本区
		txtMsg1.setBackground(Color.WHITE);

		label = new JLabel("消息记录");
		label.setBounds(130, 17, 124, 31);
		frame.getContentPane().add(label);

		scrollPane_1 = new ScrollPane();// 滚动条
		scrollPane_1.setBackground(new Color(173, 216, 85));
		scrollPane_1.setBounds(108, 54, 556, 304);
		scrollPane_1.add(txtMsg1);
		frame.getContentPane().add(scrollPane_1);

		textField = new TextField();// 服务器IP
		textField.setBounds(10, 68, 93, 23);
		frame.getContentPane().add(textField);
		btnip = new JButton("IP地址");
		btnip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textField.setText("172.20.10.7\n");
			}
		});
		btnip.setBackground(Color.WHITE);
		btnip.setBounds(10, 10, 93, 44);
		frame.getContentPane().add(btnip);

		textField_1 = new TextField();// 端口号
		textField_1.setBounds(10, 224, 89, 23);
		frame.getContentPane().add(textField_1);
		button_1 = new JButton("端口号");
		button_1.setBackground(Color.WHITE);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textField_1.setText("8787\n");
			}
		});
		button_1.setBounds(10, 174, 93, 44);
		frame.getContentPane().add(button_1);

		button = new JButton("退出");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		button.setBackground(new Color(255, 192, 203));
		button.setBounds(585, 364, 89, 23);
		frame.getContentPane().add(button);

	}
}
