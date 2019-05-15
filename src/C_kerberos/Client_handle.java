package C_kerberos;

import DES.DES;

public class Client_handle {
	String ToAS_enclosure(String head, String source_IP, String Dest_IP, String IDC, String IDtgs, String TS1)
	// 封装到AS报文（包头，源ID，目的ID，IDC,IDtgs,TS1）
	{
		String message = "";
		int L_source_IP, L_Dest_IP;
		L_source_IP = source_IP.length();// 通过length获取字符串长度
		if (L_source_IP > 15)// 超过规定长度报错
			System.out.println("源IP格式错误，请重新发送");
		if (L_source_IP <= 15)// 小于规定长度，补0并去除中间的“.”
		{
			source_IP = addZero(source_IP);
		}
		L_Dest_IP = Dest_IP.length();
		if (L_Dest_IP > 15)// 超过规定长度报错
			System.out.println("目的IP格式错误，请重新发送");
		if (L_Dest_IP <= 15)// 小于规定长度，补0并去除中间的“.”
		{
			Dest_IP = addZero(Dest_IP);
		}
		message = head + source_IP + Dest_IP + IDC + IDtgs + TS1;
		return message;

	}

	public String addZero(String ip) {// IP填0
		String result = ""; // 用保存处理后的结果
		String[] array = ip.split("\\."); // 这个函数将传入的字符串根据这个小点来分解，因为这个点式一个转义字符，所以需要写成"\\."
		for (int i = 0; i < array.length; i++) {
			while (array[i].length() < 3) { // 一共被分成了四个字符串，字符串里已经没有了小点，如果一个字符串的长度小于三，那么就在前面加零
				array[i] = "0" + array[i];
			}

		}
		for (int i = 0; i < array.length; i++) {
			result += array[i]; // 将处理好的四个字符串连起来
		}
		return result;
	}

	String TOtgs_enclosure(String head, String source_IP, String Dest_IP, String IDv, String Ttgs,
			String Authenticatorc)
	// 封装报文（包头，源IP，目的IP，IDv,Ttgs,Authenticatorc）
	{
		String message = "";

		int L_source_IP, L_Dest_IP;
		L_source_IP = source_IP.length();// 通过length获取字符串长度
		if (L_source_IP > 15)// 超过规定长度报错
			System.out.println("源IP格式错误，请重新发送");
		if (L_source_IP <= 15)// 小于规定长度，补0并去除中间的“.”
		{
			source_IP = addZero(source_IP);
		}

		L_Dest_IP = Dest_IP.length();
		if (L_Dest_IP > 15)// 超过规定长度报错
			System.out.println("目的IP格式错误，请重新发送");
		if (L_Dest_IP <= 15)// 小于规定长度，补0并去除中间的“.”
		{
			Dest_IP = addZero(Dest_IP);
		}
		message = head + source_IP + Dest_IP + IDv + Ttgs + Authenticatorc;
		return message;

	}

	String TOv_enclosure(String head, String source_IP, String Dest_IP, String Tv, String Authenticatorc)
	// 封装到V报文（包头，源IP，目的IP，Tv,Authenticatorc）
	{
		String message = "";

		int L_source_IP, L_Dest_IP;
		L_source_IP = source_IP.length();// 通过length获取字符串长度
		if (L_source_IP > 15)// 超过规定长度报错
			System.out.println("源IP格式错误，请重新发送");
		if (L_source_IP <= 15)// 小于规定长度，补0并去除中间的“.”
		{
			source_IP = addZero(source_IP);
		}

		L_Dest_IP = Dest_IP.length();
		if (L_Dest_IP > 15)// 超过规定长度报错
			System.out.println("目的IP格式错误，请重新发送");
		if (L_Dest_IP <= 15)// 小于规定长度，补0并去除中间的“.”
		{
			Dest_IP = addZero(Dest_IP);
		}
		message = head + source_IP + Dest_IP + Tv + Authenticatorc;
		return message;

	}

	// 解封来自TGS的报文（报文，密钥）
	TGS_C FROTGS_deblocking(String message, String password) {

		// 判断长度是否符合标准
		String[] result = null;
		TGS_C a = new TGS_C();
		DES des = new DES();
		message = des.Decryption(message, password);
		if (message.length() > 4) {
			// 按固定格式截取报文

			a.head = message.substring(0, 4);
			a.source_IP = message.substring(4, 16);
			a.Dest_IP = message.substring(16, 28);
			a.Kcv = message.substring(28, 36);
			a.IDv = message.substring(36, 40);
			a.TS4 = message.substring(40, 52);
			a.Tv = message.substring(52, message.length());

			// 还原IP
			a.source_IP = rebuildIP(a.source_IP);
			// System.out.println(a.source_IP);
			a.Dest_IP = rebuildIP(a.Dest_IP);
		} else {
			System.out.println("报文长度有误，请重新发送");

		}

		return a;
	}

	// 解封来自AS的报文（报文，密钥）
	AS_C FROAS_deblocking(String message, String password) {

		// 判断长度是否符合标准
		AS_C a = new AS_C();
		DES des = new DES();

		message = des.Decryption(message, password);
		// System.out.println(message.length());
		// System.out.println(message);
		if (message.length() > 2) {
			// 按固定格式截取报文

			a.head = message.substring(0, 4);
			a.source_IP = message.substring(4, 16);
			a.Dest_IP = message.substring(16, 28);
			a.Kctgs = message.substring(28, 36);
			a.IDtgs = message.substring(36, 40);
			a.TS2 = message.substring(40, 52);
			a.Lifetime2 = message.substring(52, 58);
			a.Ttgs = message.substring(58, message.length());

			// 还原IP
			a.source_IP = rebuildIP(a.source_IP);
			a.Dest_IP = rebuildIP(a.Dest_IP);
		} else {
			System.out.println("报文长度有误，请重新发送");
		}
		return a;
	}

	// 解封来自V的报文（报文，密钥）
	/*
	 * V_C FROv_deblocking(String message,String password) {
	 * 
	 * //判断长度是否符合标准 String[] result=null; V_C a=new V_C(); DES des=new DES();
	 * message=des.Decryption(message, password); if(message.length()>4) {
	 * //按固定格式截取报文
	 * 
	 * a.head=message.substring(0, 4); a.source_IP=message.substring(4, 16); //
	 * a.Dest_IP=message.substring(16, 28); //System.out.println(Dest_IP); //
	 * a.TS5=message.substring(28, 41);
	 * 
	 * 
	 * //还原IP a.source_IP=rebuildIP(a.source_IP); System.out.println(a.source_IP);
	 * a.Dest_IP=rebuildIP(a.Dest_IP); } else { System.out.println("报文长度有误，请重新发送");
	 * 
	 * }
	 * 
	 * return a; }
	 */
	C_V deblocking(String message) {

		// 判断长度是否符合标准
		String[] result = null;
		C_V a = new C_V();
		if (message.length() == 668) {
			// 按固定格式截取报文

			a.head = message.substring(0, 4);
			a.source_IP = message.substring(4, 16);
			a.Dest_IP = message.substring(16, 28);
			// System.out.println(Dest_IP);
			a.Tv = message.substring(28, 412);
			a.Authenticatorc = message.substring(412, 668);
			// 还原IP
			a.source_IP = rebuildIP(a.source_IP);
			// System.out.println(a.source_IP);
			a.Dest_IP = rebuildIP(a.Dest_IP);
		} else {
			System.out.println("报文长度有误，请重新发送");

		}

		return a;
	}

	// 字符串还原为IP地址
	String rebuildIP(String IP) {
		String first, second, third, forth;
		first = IP.substring(0, 3);
		second = IP.substring(3, 6);
		third = IP.substring(6, 9);
		forth = IP.substring(9, 12);

		int i = Integer.valueOf(first);// 将字符串转化为整数
		first = String.valueOf(i);// 转化成字符串
		i = Integer.valueOf(second);
		second = String.valueOf(i);
		i = Integer.valueOf(third);
		third = String.valueOf(i);
		i = Integer.valueOf(forth);
		forth = String.valueOf(i);
		IP = first + "." + second + "." + third + "." + forth;
		return IP;

	}

	public static void main(String args[]) {
		Client_handle deal = new Client_handle();
		String a = "192168022006";
		AS_C b;
		System.out.println(deal.rebuildIP(a));
		a = deal.ToAS_enclosure("0000", "2.168.1.1", "192.168.1.2", "0001", "2001", "170628888811");
		System.out.println(a);
		a += "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";

	}

}
