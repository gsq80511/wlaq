package C_kerberos;

import DES.DES;

//256
public class Authenticatorc {
	String IDc;
	String ADc;
	String TS3;

	String enclosure(String password, String IDc, String ADc, String TS3)
	// 封装报文（密钥，IDC,ADC,TS3）
	{
		String message = "";
		DES des = new DES();
		int L_source_IP;
		L_source_IP = ADc.length();// 通过length获取字符串长度
		if (L_source_IP > 15)// 超过规定长度报错
			System.out.println("源IP格式错误，请重新发送");
		if (L_source_IP <= 15)// 小于规定长度，补0并去除中间的“.”
		{
			ADc = addZero(ADc);
		}

		message = IDc + ADc + TS3;
		// System.out.println(message.length());
		// 加密
		message = des.Encryption(message, password);
		// System.out.println(message.length());
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
	Authenticatorc deblocking(String message, String password) {
		DES des = new DES();
		message = des.Decryption(message, password);

		// 判断长度是否符合标准
		String[] result = null;
		Authenticatorc a = new Authenticatorc();
		if (message.length() == 28) {
			// 按固定格式截取报文

			// System.out.println(Dest_IP);

			a.IDc = message.substring(0, 4);
			a.ADc = message.substring(4, 16);
			a.TS3 = message.substring(16, 28);

		} else {
			System.out.println("报文长度有误，请重新发送");

		}

		return a;
	}

	public static void main(String args[]) {
		Authenticatorc deal = new Authenticatorc();
		String a = "";
		Authenticatorc b;

		// System.out.println(deal.addZero("62.4.22.115"));
		a = deal.enclosure("170628888811", "1234", "123456789123", "123456789123");
		System.out.println(a);
		// System.out.println(deal.rebuildIP("11111111111111"));

		b = deal.deblocking(a, "170628888811");

		System.out.println(b.TS3);

	}
}
