/**
 * 
 */
/**
 * @author 记住我的名字DCX
 *
 */
package datatest;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.*;

public class test {

	// 将输入的密码hash成定长8位密钥
	public static String hashcode(String Secret) {
		String str = Secret;
		int hash = str.hashCode();
		String str2 = null;
		int j = Math.abs(hash);
		Integer k = j;

		if (k.toString().length() == 8) {
			str2 = k.toString();
		}

		if (k.toString().length() < 8) {
			str2 = "0" + k.toString();
			while (str2.length() < 8) {
				str2 = "0" + str2;

			}
		}

		if (k.toString().length() > 8) {
			str2 = k.toString().substring(0, 8);
		}

		return str2;

	}

	// 获取时间
	public static String Time() {
		SimpleDateFormat df = new SimpleDateFormat("MddHHmmssSSS");// 设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		return date;
	}

	// 获取时间到界面
	public static String Time2() {
		SimpleDateFormat df = new SimpleDateFormat("MM-dd_HH:mm:ss ");// 设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		return date;
	}

	// 连接数据库并获取密码
	public static String Secret(int IDc) throws SQLException {
		Connection dbConn = null;
		String Secret = null;
		// 连接数据库
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=test";
		String userName = "sa";
		String userPwd = "1";
		try {
			Class.forName(driverName);
			System.out.println("加载驱动成功！");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("加载驱动失败！");
		}
		try {
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
			System.out.println("连接数据库成功！");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("SQL Server连接失败！");
		}

		Statement stmt = dbConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);// 创建SQL命令对象

		// 获取数据库数据
		ResultSet rs = stmt.executeQuery("SELECT * FROM ClientData");// 遍历查找信息表
		while (rs.next()) {
			if (Integer.toString(IDc).equals(rs.getString("IDc"))) {

				// System.out.println("用户"+IDc+"验证通过！");
				Secret = rs.getString(2);

			}
		}
		return Secret;

	}

	// 连接数据库并插入数据
	public static int Database(int IDc, String Secret) throws SQLException {
		Connection dbConn = null;

		// 连接数据库
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=test";
		String userName = "sa";
		String userPwd = "1";
		try {
			Class.forName(driverName);
			System.out.println("加载驱动成功！");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("加载驱动失败！");
		}
		try {
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
			System.out.println("连接数据库成功！");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("SQL Server连接失败！");
		}

		Statement stmt = dbConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);// 创建SQL命令对象

		/*
		 * //创建表 System.out.println("开始创建表"); String query =
		 * "create table ClientData(IDc int,Secret NCHAR(50),Hashkey varchar(50))";//
		 * 创建表SQL语句 stmt.executeUpdate(query);// 执行SQL命令对象 System.out.println("表创建成功");
		 */
		String hashresult = hashcode(Secret);

		// 插入数据
		System.out.println("开始插入数据");
		String a1 = null;
		int y = 0, l = 0;
		ResultSet rs = stmt.executeQuery("SELECT * FROM ClientData");// 遍历查找信息表
		if (!rs.next()) {
			String a2 = String.format("INSERT INTO ClientData VALUES('%d','%s','%s')", IDc, Secret, hashresult);// 插入数据SQL语句
			stmt.executeUpdate(a2);// 执行SQL命令对象
			System.out.println("插入数据成功!");
			l++;
		}

		else {
			rs.previous();
			while (rs.next()) {
				int idd = rs.getInt(1);
				if (idd == IDc) {
					System.out.println("用户已存在，请勿重复注册！");
					y++;
					break;
				}
			}
		}
		if (y == 0 && l == 0) // 如果用户不存在且表不为空，那就插入数据
		{
			a1 = String.format("INSERT INTO ClientData VALUES('%d','%s','%s')", IDc, Secret, hashresult);// 插入数据SQL语句
			stmt.executeUpdate(a1);// 执行SQL命令对象
			System.out.println("插入数据成功");
		}

		return y;
	}

	// 获取数据库数据进行比较是否认证成功,最后返回result
	public static String DBget(int IDc, String Secret) throws SQLException {
		Connection dbConn = null;

		// 连接数据库
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=test";
		String userName = "sa";
		String userPwd = "1";
		try {
			Class.forName(driverName);
			// System.out.println("加载驱动成功！");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("加载驱动失败！");
		}
		try {
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
			// System.out.println("连接数据库成功！");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.print("SQL Server连接失败！");
		}

		Statement stmt = dbConn.createStatement();// 创建SQL命令对象

		// 获取数据库数据
		ResultSet rs = stmt.executeQuery("SELECT * FROM ClientData");// 遍历查找信息表
		String e = "*", f = "$";
		int j = 0;
		String result = "";
		String hash = "";
		String result2 = "";
		while (rs.next()) {
			if (Integer.toString(IDc).equals(rs.getString("IDc"))) {

				System.out.println("用户" + IDc + "验证通过！");
				j++;
				Secret = Secret.trim();
				String Secretlist = rs.getString(2).trim();
				// System.out.println(Secretlist+"* *"+Secret.trim()+"*");
				if (Secretlist.equals(Secret)) {
					System.out.println("用户Secret验证通过！");
					break;
				} else {
					System.out.println("密码错误*****请重新输入*****！");
					result = e;

				}
			}
		}

		if (j == 0) {
			System.out.println("用户不存在*****请注册*****！");
			result = f;

		}

		String hashcoderesult = hashcode(Secret);
		hash = hashcoderesult;

		if (result == "") {
			result2 = hash + " " + Secret;
		} else if (result.length() != 0) {
			result2 = result + " " + hash + " " + Secret;
		}

		return result2;

	}

	// 生成随机密钥Kc,tgs
	public static StringBuilder Randomfigure() {
		StringBuilder str = new StringBuilder();// 定义变长字符串
		Random random = new Random();
		// 随机生成数字，并添加到字符串
		for (int i = 0; i < 8; i++) {
			str.append(random.nextInt(10));
		}
		return str;

	}

	public static void main(String[] args) throws UnknownHostException, SQLException {
		int IDc = 0001;
		String Secret = "dds123fff";
		Secret(12345678);
		System.out.println("ss");
		// Database(IDc,Secret);
		// String Kc = DBget(IDc,Secret);
		// StringBuilder l =Randomfigure();
		// System.out.println(l);
		// String inetAddress=InetAddress.getLocalHost().getHostAddress();
		// System.out.println(inetAddress);
	}
}
