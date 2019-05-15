package TGSSocket;

import ASSocket.Tickettgs;
//384
import DES.DES;

public class Ticketv {
	String Kcv;
	String IDc;
	String ADc;
	String IDv;
	String TS4;
	String Lifetime4;

	// 封装Ticketv（Kc,v||IDC||ADC|| IDv||TS4||Lifetime4）
	String enclosure(String password, String Kcv, String IDc, String ADc, String IDv, String TS4, String Lifetime4) {
		String message = "";
		DES des = new DES();
		message = Kcv + IDc + ADc + IDv + TS4 + Lifetime4;
		// 加密
		message = des.Encryption(message, password);
		return message;
	}

	// 解封Ticketv
	@SuppressWarnings("null")
	Ticketv deblocking(String message, String password) {
		DES des = new DES();
		message = des.Decryption(message, password);
		// 判断长度是否符合标准
		String[] result = null;
		Ticketv a = new Ticketv();
		if (message.length() == 46) {
			// 按固定格式截取报文
			a.Kcv = message.substring(0, 8);
			a.IDc = message.substring(8, 12);
			a.ADc = message.substring(12, 24);
			a.IDv = message.substring(24, 28);
			a.TS4 = message.substring(28, 40);
			a.Lifetime4 = message.substring(40, 46);
		} else {
			System.out.println("报文长度有误，请重新发送");
		}

		return a;
	}

	public static void main(String args[]) {
		Ticketv deal = new Ticketv();
		String a = "";
		Ticketv b;
		a = deal.enclosure("170628888811", "12345678", "1234", "123456789123", "1234", "123456789123", "123456");
		System.out.println(a.length());
		/*
		 * b=deal.deblocking(a,"170628888811"); System.out.println(b.Lifetime4);
		 */
	}
}
