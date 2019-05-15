package DES;

import java.util.BitSet;

public class DES8byte {
	char K[][] = new char[16][48];// 保存16轮子密钥
	char L[][] = new char[17][28];// 保存每轮子密钥左半部分
	char R[][] = new char[17][28];// 保存每轮子密钥右半部分
	char LL[][] = new char[17][32];// 保存每轮明文左半部分
	char RR[][] = new char[17][32];// 保存每轮明文右半部分
	char data[][] = new char[16][48];// 保存16轮扩充后明文右半部分
	char key[] = new char[56];// 保存56位密钥(PC_1)
	String EncryptData;// 存放密文
	String DecryptData;// 存放解密后明文
	data Data = new data();// 置换表等

	int getpow(int i, int j)// i的j次方
	{
		int result = 1;
		if (j == 0)
			return result;
		else {
			for (int k = 0; k < j; k++)
				result = result * i;
			return result;
		}

	}

	String toBitSet(String str) { // 字符转换为二进制
		String result = "";
		BitSet bitSet = new BitSet();
		byte[] strBits = str.getBytes();
		for (int i = 0; i < strBits.length * 8; i++) {
			if ((strBits[i / 8] & (128 >> (i % 8))) > 0) {
				bitSet.set(i);
			}
		}
		for (int i = 0; i < strBits.length * 8; i++) {
			if (bitSet.get(i))
				result += "1";
			else
				result += "0";
		}
		return result;
	}

	String Replace(String str, char b[]) {// 明文初始置换
		// data Data=new data();
		String str2;
		str2 = toBitSet(str);
		// System.out.println(str2.length());
		char[] a;
		a = str2.toCharArray();// 转换为字符数组
		/*
		 * char b[]=new char[a.length]; int group=a.length/64; //for (int i = 0; i <
		 * a.length; i++) //System.out.print(a[i]); //System.out.println(""); for(int
		 * i=0;i<group;i++) for(int j=0;j<64;j++) {
		 * b[i*64+j]=a[i*64+(Data.IP_Table[j]-1)]; }
		 */
		// char b[]=new char[64];
		for (int i = 0; i < 64; i++) {
			b[i] = a[(Data.IP_Table[i] - 1)];
		}
		/*
		 * for(int j=0;j<32;j++)//保存LLO和RR0 { LL[0][j]=a[(Data.IP_Table[j]-1)];
		 * RR[0][j]=a[(Data.IP_Table[j+32]-1)]; }
		 */
		String out;
		out = String.valueOf(b);
		return out;
	}

	void SonKey(String str)// 子密钥产生
	{
		// data Data=new data();
		String str2;
		str2 = toBitSet(str);
		char[] a;
		a = str2.toCharArray();// 转换为字符数组
		for (int i = 0; i < 28; i++) {// 生成L0和R0
			L[0][i] = a[Data.C0[i] - 1];
			R[0][i] = a[Data.D0[i] - 1];
		}
		for (int j = 0; j < 16; j++) {// 生成Li和Ri
			for (int k = 0; k < 28; k++) {
				L[j + 1][k] = L[j][(k + Data.move_times[j]) % 28];
				R[j + 1][k] = R[j][(k + Data.move_times[j]) % 28];
			}
		}
		char key2[] = new char[56];// 用于合并左右部分
		for (int m = 0; m < 16; m++) {// 密钥置换PC_2
			for (int mm = 0; mm < 28; mm++) {
				key2[mm] = L[m + 1][mm];
				key2[mm + 28] = R[m + 1][mm];
			}
			for (int q = 0; q < 48; q++)
				K[m][q] = key2[Data.PC_2[q] - 1];
		}
	}

	void Scom(char a[], char b[])// S盒压缩
	{
		data Data = new data();
		int arry[] = new int[48];
		int arry2[] = new int[32];
		for (int i = 0; i < 48; i++)// 化为整形
		{
			// System.out.print(a[i]);
			arry[i] = (int) a[i] - 48;
			// System.out.print(arry[i]);
		}
		// System.out.println(" ");
		int h, l;// 行和列
		int result[] = new int[8];// 存放经过S盒后的十进制值
		int flag[] = new int[4];
		for (int i = 0; i < 8; i++)// 判定行和列，找到对应的S盒数据
		{
			h = (arry[(1 + (i * 6)) - 1] * 2 + arry[(6 + (i * 6)) - 1]);// 行
			l = (arry[(2 + (i * 6)) - 1] * 8 + arry[(3 + (i * 6)) - 1] * 4 + arry[(4 + (i * 6)) - 1] * 2
					+ arry[(5 + (i * 6)) - 1]); // 列
			result[i] = Data.S_Box[i][h][l];// S盒数据
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 3; j >= 0; j--) {
				flag[j] = result[i] % 2;
				result[i] = result[i] / 2;
			}
			for (int k = 0; k < 4; k++) {
				arry2[4 * i + k] = flag[k];
			}
		}
		for (int m = 0; m < 32; m++)// 化为char
			b[m] = (char) (arry2[m] + 48);
	}

	void Function(char tep2[], char data[], int flag)// F函数
	{
		// data Data=new data();
		char tmp[] = new char[48];
		char tep[] = new char[32];
		for (int i = 0; i < 48; i++)// 与子密钥异或
		{
			tmp[i] = data[Data.E_Table[i] - 1];// 明文E表扩充
			if (tmp[i] != K[flag - 1][i])
				tmp[i] = '1';
			else
				tmp[i] = '0';
		}
		Scom(tmp, tep);// S盒压缩
		for (int j = 0; j < 32; j++)// P盒置换
			tep2[j] = tep[Data.P_Table[j] - 1];

	}

	String Encryption(String str, String password)// 加密操作(8位密文）
	{
		// System.out.println("加密过程：");
		// data Data=new data();
		char b[] = new char[64];
		char result[] = new char[64];
		char result2[] = new char[64];
		char tep2[] = new char[32];
		char tep3[] = new char[32];
		Replace(str, b);// 初始置换
		// System.out.print("初始置换明文（64bit)：");
		// System.out.println(b);
		for (int i = 0; i < 32; i++) {
			LL[0][i] = b[i];
			RR[0][i] = b[i + 32];
		}
		SonKey(password);// 生成子密钥
		for (int j = 0; j < 16; j++) {
			// System.out.println("加密单轮变换第 "+(j+1)+"轮：");
			for (int w = 0; w < 32; w++)
				tep3[w] = RR[j][w];
			Function(tep2, tep3, j + 1);
			for (int k = 0; k < 32; k++) {
				LL[j + 1][k] = RR[j][k];
				if (LL[j][k] != tep2[k])
					RR[j + 1][k] = '1';
				else
					RR[j + 1][k] = '0';
			}
			// System.out.print("加密明文左部分：");
//			for(int k=0;k<32;k++)
//				System.out.print(LL[j+1][k]+" ");
//			//System.out.println(" ");
//			//System.out.print("加密明文右部分：");
//			for(int k=0;k<32;k++)
//				System.out.print(RR[j+1][k]+" ");
//			System.out.println(" ");
		}
		for (int m = 0; m < 32; m++)// 对换
		{
			result[m] = RR[16][m];
			result[m + 32] = LL[16][m];
		}
		for (int p = 0; p < 64; p++)// 逆初始置换
		{
			result2[p] = result[Data.IP_1_Table[p] - 1];
		}
		EncryptData = String.valueOf(result2);// 保存密文
		return EncryptData;
	}

	String Decryption(String str, String password)// 解密操作
	{
		// System.out.println("解密过程：");
		// data Data=new data();
		String re = new String();
		char result[] = new char[64];
		char result2[] = new char[64];
		char tep2[] = new char[32];
		char tep3[] = new char[32];
		char b[] = new char[64];
		char en[] = str.toCharArray();
		for (int i = 0; i < 64; i++)// 初始置换
		{
			b[i] = en[Data.IP_Table[i] - 1];
		}
		for (int i = 0; i < 32; i++) {
			LL[0][i] = b[i];
			RR[0][i] = b[i + 32];
		}
		char kk[][] = new char[16][48];// 保存加密时密钥
		SonKey(password);// 生成子密钥
		for (int f = 0; f < 16; f++)
			for (int s = 0; s < 48; s++)
				kk[f][s] = K[f][s];
		for (int ff = 0; ff < 16; ff++)
			for (int ss = 0; ss < 48; ss++)
				K[ff][ss] = kk[15 - ff][ss];
//		for(int i=0;i<16;i++){
//			//System.out.print("子密钥K["+(i+1)+"]:\t ");
//			for(int j=0;j<48;j++)
//			{
//				System.out.print(K[i][j]+" ");
//				if(j==47)
//					System.out.println(" ");	
//			}
//		}
		for (int j = 0; j < 16; j++) {
			// System.out.println("解密单轮变换第 "+(j+1)+"轮：");
			for (int w = 0; w < 32; w++)
				tep3[w] = RR[j][w];
			Function(tep2, tep3, j + 1);
			for (int k = 0; k < 32; k++) {
				LL[j + 1][k] = RR[j][k];
				if (LL[j][k] != tep2[k])
					RR[j + 1][k] = '1';
				else
					RR[j + 1][k] = '0';
				// RR[j+1][k]=(char) (LL[j][k]^tep2[k]);
			}
			// System.out.print("解密明文左部分：");
//			for(int k=0;k<32;k++)
//				System.out.print(LL[j+1][k]+" ");
//			//System.out.println(" ");
//			//System.out.print("解密明文右部分：");
//			for(int k=0;k<32;k++)
//				System.out.print(RR[j+1][k]+" ");
//			//System.out.println(" ");
		}
		for (int m = 0; m < 32; m++)// 对换
		{
			result[m] = RR[16][m];
			result[m + 32] = LL[16][m];
		}
		// System.out.print("解密二进制结果为：");
		for (int p = 0; p < 64; p++)// 逆初始置换
		{
			result2[p] = result[Data.IP_1_Table[p] - 1];
			// System.out.print(result2[p]);
		}
		// System.out.println(" ");
		/* 二进制转换为字符 */
		int flag[] = new int[8];
		for (int r1 = 0; r1 < 8; r1++)
			for (int r2 = 0; r2 < 8; r2++) {
				flag[r1] += ((int) result2[r1 * 8 + r2] - 48) * getpow(2, (7 - r2));// getpow()方法为i的j次方
			}
		for (int ff = 0; ff < 8; ff++) {
			re += (char) flag[ff];
		}

		DecryptData = String.valueOf(re);// 保存解密后明文
		return DecryptData;
	}

	// 测试
	public static void main(String args[]) {
		DES8byte des = new DES8byte();
		String str = "wangchen";
		System.out.println("输入的明文为：" + str);
		String password = "abcdefgh";
		System.out.println("输入的密钥为：" + password);
		des.Encryption(str, password);
//    		for(int i=0;i<16;i++){
//    			System.out.print("子密钥K["+(i+1)+"]:\t ");
//    			for(int j=0;j<48;j++)
//    			{
//    				System.out.print(des.K[i][j]+" ");
//    				if(j==47)
//    					System.out.println(" ");	
//    			}
//    		}
		des.Decryption(des.EncryptData, password);
		System.out.println("解密结果为：" + des.DecryptData);
	}
}
