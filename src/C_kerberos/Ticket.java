package C_kerberos;

import DES.DES;

public class Ticket {
	String Kctgs;
	String IDc;
	String ADc;
	String ID;
	String TS;
	String Lifetime;

	String enclosure(String password, String Kctgs, String IDc, String ADc, String IDtgs, String TS2, String Lifetime2)
	// 封装报文（包头，源ID，目的ID，IDC,IDtgs,TS1）
	{
		String message = "";
		DES des = new DES();
		message = Kctgs + IDc + ADc + IDtgs + TS2 + Lifetime2;
		System.out.println(message.length());
		// 加密
		message = des.Encryption(message, password);
		System.out.println(message.length());
		return message;

	}

	@SuppressWarnings("null")
	Ticket deblocking(String message, String password) {
		DES des = new DES();
		message = des.Decryption(message, password);

		// 判断长度是否符合标准
		String[] result = null;
		Ticket a = new Ticket();
		if (message.length() == 46) {
			// 按固定格式截取报文

			// System.out.println(Dest_IP);
			a.Kctgs = message.substring(0, 8);
			a.IDc = message.substring(8, 12);
			a.ADc = message.substring(12, 24);
			a.ID = message.substring(24, 28);
			a.TS = message.substring(28, 40);
			a.Lifetime = message.substring(40, 46);
		} else {
			System.out.println("报文长度有误，请重新发送");

		}

		return a;
	}

	public static void main(String args[]) {
		Ticket deal = new Ticket();
		String a = "";
		Ticket b;

		// System.out.println(deal.addZero("62.4.22.115"));
		a = deal.enclosure("170628888811", "12345678", "1234", "123456789123", "1234", "123456789123", "123456");
		System.out.println(a);
		// System.out.println(deal.rebuildIP("11111111111111"));

		b = deal.deblocking(a, "170628888811");

		System.out.println(b.Lifetime);

	}
}
