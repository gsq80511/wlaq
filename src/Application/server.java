package Application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class server {

	public static int PORT = 7777;//gs
	SUI window = new SUI();
	public List<serverone> clients = new ArrayList<serverone>(); // ����ͻ����߳���

	public server() {/////////////

		ServerSocket s = null;
		Socket socket = null;
		try {
			s = new ServerSocket(PORT);

			window.frame.setVisible(true);

			// �ȴ������󡢷���һֱ����
			while (true) {
				socket = s.accept();

				// System.out.println("socket:"+socket);
				// window.txtMsg1.append(socket+"\n");
				serverone c = new serverone(socket);
				// new Thread(c).start(); //�����߳�
				clients.add(c); // ����߳���
			}
		} catch (Exception e) {
			try {
				// socket.close();
			} catch (Exception e1) {

				e1.printStackTrace();
			}
		} finally {
			try {
				// s.close();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	}

	public class serverone extends Thread {

		private Socket socket = null;
		private BufferedReader br = null;
		private PrintWriter pw = null;

		public serverone(Socket s) {
			socket = s;
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				start();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		void sendall(String str) {
			try {

				pw.println(str);// ����Ϣ������ͻ���
				// window.txtMsg1.append(str+"\n");
				pw.flush();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			while (true) {
				String str, str2;
				try {
					str = br.readLine();
					str2 = str.substring(6);
					for (serverone e : clients) {// �������
						// window.txtMsg1.append(str+"\n");
						e.sendall(str + "\n");
					}
					if (str2.equals("�˳�������")) {
						window.txtMsg1.append(str + "\n");
						System.out.println(str2);
						br.close();
						pw.close();
						socket.close();

					}////
					if (str2.equals("����������")) {/////
						window.txtMsg1.append(str + "��Kerberos��֤ͨ����\n");
						System.out.println(str2);

					}
					System.out.println("Client Socket Message:" + str);

					// pw = new PrintWriter(new BufferedWriter(new
					// OutputStreamWriter(socket.getOutputStream())));

//					pw.println(""+str);
//					pw.flush();
				} catch (Exception e) {
					try {
						br.close();
						pw.close();
						socket.close();
					} catch (IOException e1) {

						e1.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new server();

	}

}
