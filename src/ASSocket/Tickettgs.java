package ASSocket;

//384
import DES.DES;

public class Tickettgs {
	public String Kctgs;
	public String IDc;
	public String ADc;
	public String ID;
	public String TS2;
	public String Lifetime2;

	// 封装Tickettgs（Kc,tgs|| IDC|| ADC|| IDtgs|| TS2|| Lifetime2）
	String enclosure(String password, String Kctgs, String IDc, String ADc, String IDtgs, String TS2,
			String Lifetime2) {
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
		message = Kctgs + IDc + ADc + IDtgs + TS2 + Lifetime2;
		// 加密
		message = des.Encryption(message, password);

		return message;

	}

	// 补0
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

	// 解封Tickettgs
	@SuppressWarnings("null")
	public Tickettgs deblocking(String message, String password) {
		DES des = new DES();
		message = des.Decryption(message, password);
		// 判断长度是否符合标准
		String[] result = null;
		Tickettgs a = new Tickettgs();
		if (message.length() == 46) {
			// 按固定格式截取报文
			a.Kctgs = message.substring(0, 8);
			a.IDc = message.substring(8, 12);
			a.ADc = message.substring(12, 24);
			a.ID = message.substring(24, 28);
			a.TS2 = message.substring(28, 40);
			a.Lifetime2 = message.substring(40, 46);
		} else {
			System.out.println("报文长度有误，请重新发送");

		}

		return a;
	}

	public static void main(String args[]) {
		Tickettgs deal = new Tickettgs();
		String a;
		a = deal.enclosure("17062888", "12345678", "1234", "123456789123", "1234", "123456789123", "123456");
		System.out.println(a.length());
		deal = deal.deblocking(a, "170628888811");
		System.out.println(deal.ADc);
		System.out.println(deal.ID);
		System.out.println(deal.IDc);
		System.out.println(deal.Kctgs);
		System.out.println(deal.Lifetime2);
		System.out.println(deal.TS2);

	}
}
