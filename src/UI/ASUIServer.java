package UI;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import ASSocket.server;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Button;
import java.awt.Color;

public class ASUIServer {

	public JFrame frmAs;
	public server server2 ;
	public Button button ;
	public TextArea txtMsg;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ASUIServer window = new ASUIServer();
					window.frmAs.setVisible(true);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ASUIServer() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frmAs = new JFrame();
		frmAs.setBackground(new Color(240, 230, 140));
		frmAs.getContentPane().setBackground(Color.BLACK);
		frmAs.setForeground(new Color(135, 206, 235));
		frmAs.setTitle("AS\u670D\u52A1\u5668(*^__^*) ");
		frmAs.setBounds(100, 100, 693, 695);
		frmAs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAs.getContentPane().setLayout(null);
		
		button = new Button("\u9000\u51FA\uFF08EXIT\uFF09");
		button.setForeground(Color.RED);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//socket.close();
				frmAs.dispose();
			}
		});
		button.setActionCommand("");
		button.setBounds(572, 615, 93, 23);
		frmAs.getContentPane().add(button);
		
		txtMsg = new TextArea();
		txtMsg.setBackground(Color.WHITE);
		txtMsg.setForeground(new Color(0, 0, 0));
		txtMsg.setBounds(34, 25, 528, 613);
		txtMsg.setFont(new Font("ºÚÌå",Font.BOLD,15));
		frmAs.getContentPane().add(txtMsg);
		
	}
	
}
