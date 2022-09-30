package com.tlicorporation.triphil;

public class SharedVariables {
	
	private static String ip = "192.168.0.103";
	public static String class_driver = "net.sourceforge.jtds.jdbc.Driver";
	private static String db = "h";
	//db = "Payroll";
	private static String un = "hitesh";
	private static String password = "789";


	public static String getIp() {
		return ip;
	}

	public static void setIp(String ip) {
		SharedVariables.ip = ip;
	}

	public static String getDb() {
		return db;
	}

	public static void setDb(String db) {
		SharedVariables.db = db;
	}

	public static String getUn() {
		return un;
	}

	public static void setUn(String un) {
		SharedVariables.un = un;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		SharedVariables.password = password;
	}
}
