package C_kerberos;

import DES.DES;

public class AS_handle {
	String TOC_enclosure(String password, String head, String source_IP, String Dest_IP, String Kctgs, String IDtgs,
			String TS2, String Lifetime2, String tgs)
	// 封装报文（密钥，包头，源ID，目的ID，IDC,IDtgs,TS1）
	{
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
		message = head + source_IP + Dest_IP + Kctgs + IDtgs + TS2 + Lifetime2 + tgs;
		// 加密
		message = des.Encryption(message, password);

		return message;

	}

	public String addZero(String ip) {// IP填0
		String result = ""; // 用保存处理后的结果
		String[] array = ip.split("\\."); // 这个函数将传入的字符串根据这个小点来分解，因为这个点式一个转义字符，所以需要写成"\\."
		for (int i = 0; i < array.length; i++) {
			while (array[i].length() < 3) { // 一共被分成了四个字符串，字符串里已经没有了小点，如果一个字符串的长度小于三，那么就在前面加零
				array[i] = "0" + array[i];
			}
////		    if(i!=array.length-1){        //加完后，再继续在后面加上一个小点，因为最后一个不需要加，所以用if控制一下
////		    array[i]=array[i]+".";
//		    }
		}
		for (int i = 0; i < array.length; i++) {
			result += array[i]; // 将处理好的四个字符串连起来
		}
		return result;
	}

	@SuppressWarnings("null")
	C_AS deblocking(String message) {

		// 判断长度是否符合标准
		String[] result = null;
		C_AS a = new C_AS();
		if (message.length() > 12)////////////////////
		{
			// 按固定格式截取报文

			a.head = message.substring(0, 4);
			a.source_IP = message.substring(4, 16);

			a.Dest_IP = message.substring(16, 28);
			// System.out.println(Dest_IP);
			a.IDC = message.substring(28, 32);
			a.IDtgs = message.substring(32, 36);
			a.TS1 = message.substring(36, 48);

			// 还原IP
			a.source_IP = rebuildIP(a.source_IP);
			System.out.println(a.source_IP);
			a.Dest_IP = rebuildIP(a.Dest_IP);
		} else {
			System.out.println("报文长度有误，请重新发送");

		}

		return a;
	}

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
		AS_handle deal = new AS_handle();
		String a = "";
		C_AS b;

		// System.out.println(deal.addZero("62.4.22.115"));
		a = deal.TOC_enclosure("12345678", "0001", "2.168.1.1", "192.168.43.121", "12345678", "1234", "170628888811",
				"123456", "11111111111111111111111111111111111111111111111111111111111111111111");
		System.out.println(a);
		// System.out.println(deal.rebuildIP("11111111111111"));

		b = deal.deblocking(a);

		System.out.println(b.TS1);

	}

}
