package TGSSocket;

import DES.DES;

public class TGS_handle {

	// 封装报文（密钥，包头，源IP，目的IP，Kcv,IDv,TS4,,Ticketv）
	String TOC_enclosure(String password, String head, String source_IP, String Dest_IP, String Kcv, String IDv,
			String TS4, String v) {
		String message = "";
		DES des = new DES();
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
		message = head + source_IP + Dest_IP + Kcv + IDv + TS4 + v;
		// 加密
		message = des.Encryption(message, password);

		return message;

	}

	// IP填0
	public String addZero(String ip) {
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

	// 拆包
	public C_TGS FroC_deblocking(String message) {
		// 判断长度是否符合标准
		String[] result = null;
		C_TGS a = new C_TGS();
		// System.out.println(message.length());
		if (message.length() == 672) {
			// 按固定格式截取报文
			a.head = message.substring(0, 4);
			a.source_IP = message.substring(4, 16);
			a.Dest_IP = message.substring(16, 28);
			a.IDv = message.substring(28, 32);
			a.Ttgs = message.substring(32, 416);
			a.Autnenticatorc = message.substring(416, 672);

			// 还原IP
			a.source_IP = rebuildIP(a.source_IP);
			a.Dest_IP = rebuildIP(a.Dest_IP);
		} else {
			System.out.println("报文长度有误，请重新发送");

		}

		return a;
	}

	// IP加.
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

	}
}
