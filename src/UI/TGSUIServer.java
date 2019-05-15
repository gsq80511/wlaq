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

public class TGSUIServer {

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
					TGSUIServer window = new TGSUIServer();
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
	public TGSUIServer() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frmAs = new JFrame();
		frmAs.setBackground(new Color(240, 230, 140));
		frmAs.getContentPane().setBackground(new Color(0, 0, 0));
		frmAs.setForeground(new Color(224, 255, 255));
		frmAs.setTitle("TGS\u670D\u52A1\u5668(*^__^*) ");
		frmAs.setBounds(100, 100, 678, 681);
		frmAs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAs.getContentPane().setLayout(null);
		
		button = new Button("\u9000\u51FA\uFF08EXIT\uFF09");
		button.setForeground(Color.RED);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//socket.close();
				frmAs.dispose();//关闭界面
			}
		});
		button.setActionCommand("");
		button.setBounds(557, 584, 93, 23);
		frmAs.getContentPane().add(button);
		
		txtMsg = new TextArea();
		txtMsg.setBackground(Color.CYAN);
		txtMsg.setForeground(new Color(0, 0, 0));
		txtMsg.setBounds(34, 25, 517, 582);
		txtMsg.setFont(new Font("黑体",Font.BOLD,15));
		frmAs.getContentPane().add(txtMsg);
		
	}

}
