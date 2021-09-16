package org.funteachers.teachersfirst.managers;

public class StringTools {
	
	public static String left(String in, int maxLength) {
		if (in == null) return "";
		int len = in.length();
		if (len > maxLength && maxLength >= 4) return in.substring(0, maxLength - 3) + "...";
		
		len = Math.min(len, maxLength);
		return in.substring(0, len);
	}

}
