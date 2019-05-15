package ASSocket;

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

import javax.swing.JFrame;
import javax.swing.JPanel;

import UI.ASUIServer;
import datatest.test;

public class server {

	ASUIServer Myframe = new ASUIServer();// 调用UI界面
	public static int PORT = 8087;// 端口号

	public server() {
		ServerSocket s = null;
		Socket socket = null;
		Myframe.frmAs.setVisible(true);// 显示可见
		try {
			s = new ServerSocket(PORT);// 启动服务器
			Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "AS服务器已启动！\n");

			// 等待新请求、否则一直阻塞
			while (true) {
				socket = s.accept();
				// System.out.println("socket:"+socket);
				Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "接收到客户：" + socket.getInetAddress() + "的连接请求！\n");
				serverone one = new serverone(socket);
			}

		} catch (Exception e) {
			try {
				// socket.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				// s.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// 上面调用，处理监听类
	public class serverone extends Thread {

		public Socket socket = null;
		public BufferedReader br = null;
		public PrintWriter pw = null;
		public String Lifetime2 = "000999";// 生存时间
		String password = "12345678";// AS-Tgs密钥

		// 构造方法
		public serverone(Socket s) {
			socket = s;
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				// pw.println("socket:"+socket);
				start();// 自动调用run()
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		public void run() {
			while (true) {
				String str;
				int y;
				try {
					str = br.readLine();
					if (str.equals(""))// 读取空继续读取
					{
						continue;
					}
					String[] k = str.split(" ");// 将读取到的注册或登录信息分割，下面判断
					Myframe.frmAs.setVisible(true);
					Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "读取到C->AS数据：\n" + str + "\n");

					if (str.length() < 20 && k[0].equals("*"))// 注册
					{
						y = test.Database(Integer.parseInt(k[1]), k[2]);// 返回信息发给客户端，让其进行判断
						pw.println(Integer.toString(y));
						pw.flush();
						if (y == 0) {
							Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "客户端 " + k[1] + "("
									+ socket.getInetAddress() + ")" + " 注册成功！\n");
						} else if (y == 1) {
							Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "客户端 " + k[1] + "("
									+ socket.getInetAddress() + ")" + " 已存在！\n");
						}

					}

					if (str.length() < 20 && k[0].equals("$"))// 验证
					{
						String kk = test.DBget(Integer.parseInt(k[1]), k[2]);
						String[] kk2 = kk.split(" ");
						if (kk2[0].length() == 8) {
							Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "客户端 " + k[1] + "("
									+ socket.getInetAddress() + ")" + " 验证成功！\n");
							pw.println(kk);
							pw.flush();
						} else if (kk2[0].length() != 8) {
							Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "客户端 " + k[1] + "("
									+ socket.getInetAddress() + ")" + " 验证失败！\n");
							pw.println(kk);
							pw.flush();

						}

					}

					C_AS c_as = new C_AS();
					AS_handle handle = new AS_handle();
					Tickettgs ticket = new Tickettgs();

					if (str.length() > 20)// 处理发来的String信息
					{

						// 拆包
						c_as = handle.deblocking(str);

						// 数据库获取密码
						String ss = test.Secret(Integer.parseInt(c_as.IDC));

						// 数据库返回分离密钥
						String kk = test.DBget(Integer.parseInt(c_as.IDC), ss);

						// 获取Kc
						String Kc = null;
						String[] kk2 = kk.split(" ");
						if (kk2[0].length() == 8) {
							Kc = kk2[0];
						} else if (kk2[0].length() != 8) {
							Kc = kk2[1];
						}

						// 随机获取Kctgs
						String Kctgs = test.Randomfigure().toString();

						// 获取当前时间戳TS2
						String TS2 = test.Time();

						// 封装Ticket
						String Ticket = ticket.enclosure(password, Kctgs, c_as.IDC, c_as.source_IP, c_as.IDtgs, TS2,
								Lifetime2);
						Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "Ticket封装成功！\n");
						Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "封装后的数据为：\n" + Ticket + "\n");

						// AS->C的加密结果
						String asc = handle.TOC_enclosure(Kc, c_as.head, c_as.Dest_IP, c_as.source_IP, Kctgs,
								c_as.IDtgs, TS2, Lifetime2, Ticket);
						pw.println(asc);
						pw.flush();
						Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "AS->C的加密结果已发送！\n");
						Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "AS服务器认证过程已完成！\n");

					}

					if (str.equals("END"))// 判断结束
					{
						// System.out.println("Client has Closed!");
						Myframe.txtMsg.append(
								"\n" + test.Time2() + "\n" + "客户端：" + socket.getInetAddress() + " 连接AS服务器结束！\n");
						br.close();
						pw.close();
						socket.close();
						break;
					}

				} catch (Exception e) {
					try {
						br.close();
						pw.close();
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
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
