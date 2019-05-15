package TGSSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import ASSocket.Tickettgs;
import UI.TGSUIServer;
import datatest.test;

public class TGSserver {

	TGSUIServer Myframe = new TGSUIServer();// 调用UI界面
	public static int PORT = 8787;// 端口号

	// TGS构造方法
	public TGSserver() {
		ServerSocket s = null;
		Socket socket = null;
		Myframe.frmAs.setVisible(true);// 显示界面功能可见
		try {
			s = new ServerSocket(PORT);// 启动服务器
			Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "TGS服务器已启动！\n");

			// 等待新请求、否则一直阻塞
			while (true) {
				socket = s.accept();// 接收连接
				// System.out.println("socket:"+socket);
				Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "接收到客户：" + socket.getInetAddress() + "的连接请求！\n");
				new serverone(socket);
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

	// 调用内部类
	public class serverone extends Thread {

		private Socket socket = null;
		private BufferedReader br = null;
		private PrintWriter pw = null;
		public String Lifetime4 = "000999";
		public String Lifetime2 = "000999";// 生存时间
		public String Ktgs = "12345678";// AS-Tgs密钥
		public String Kv = "87654321";// V-Tgs密钥

		public serverone(Socket s)// 构造方法
		{
			socket = s;
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				start();// 自动调用run()
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		public void run() {
			while (true) {
				String str;
				try {
					str = br.readLine();// 读取
					Myframe.frmAs.setVisible(true);
					Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "接收到C->TGS数据：\n" + str + "\n");

					// 调用对象
					C_TGS c_tgs = new C_TGS();
					TGS_handle handle = new TGS_handle();
					Tickettgs tickettgs = new Tickettgs();
					Authenticatorc au = new Authenticatorc();

					if (str.equals("END"))// 判断结束
					{
						// System.out.println("Client has Closed!");
						Myframe.txtMsg.append(
								"\n" + test.Time2() + "\n" + "客户端\n" + socket.getInetAddress() + "连接TGS服务器结束！\n");
						br.close();
						pw.close();
						socket.close();
						break;
					}

					if (str.length() > 20)// 收到长串C->TGS消息
					{

						// 拆包
						c_tgs = handle.FroC_deblocking(str);
						Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "拆包C->TGS数据结束！\n");

						// 解封Tickettgs
						tickettgs = tickettgs.deblocking(c_tgs.Ttgs, Ktgs);
						Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "解封Tickettgs票据成功！\n");

						// 检验生存周期
						String a = test.Time().substring(9, 12);
						String b = tickettgs.TS2.substring(9, 12);
						int c = Integer.parseInt(a);
						int d = Integer.parseInt(b);
						int f = (c - d);
						int t = Integer.parseInt(Lifetime2);

						// 如果满足条件，TGS->C
						if (str.length() > 20 && f < t) {
							Ticketv ticket = new Ticketv();
							String Kcv = test.Randomfigure().toString();
							String TS4 = test.Time();

							// 解封Authenticatorc
							au = au.deblocking(c_tgs.Autnenticatorc, tickettgs.Kctgs);
							Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "解封Authenticatorc认证结果成功！\n");

							// 封装Ticketv
							String ticket2 = ticket.enclosure(Kv, Kcv, tickettgs.IDc, au.ADc, c_tgs.IDv, TS4,
									Lifetime4);
							Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "封装Ticketv票据成功！\n");

							// TGS->C的加密结果
							String tgsc = handle.TOC_enclosure(tickettgs.Kctgs, c_tgs.head, c_tgs.Dest_IP,
									c_tgs.source_IP, Kcv, c_tgs.IDv, TS4, ticket2);
							pw.println(tgsc);
							Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "TGS->C加密结果数据已发送！\n");

							Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "发送给C的数据包为！\n" + ticket2);

						} else if (f >= t) {
							// System.out.println("信息错误，请重发");
							Myframe.txtMsg.append("\n" + test.Time2() + "\n" + "Tickettgs票期已过，请重发！\n");
							pw.println("REPEAT");
							pw.flush();
						}

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

		new TGSserver();
	}

}
