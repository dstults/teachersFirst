package edu.lwtech.csd297.teachersfirst.pojos;

import java.util.*;

public class JsonUtils {
	
	public static String queryToJson(String query) {
		if (query == null) return "";
		String sanitized = query.trim();
		if (sanitized.isEmpty()) return "";
		if (sanitized.substring(0,1).equals("?"))
			sanitized = sanitized.substring(1);
		
		String[] splits = sanitized.split("&");
		String[] kvPair;
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (String line : splits) {
			if (i > 0) sb.append(",");
			kvPair = line.split("=");
			if (kvPair.length == 1) {
				kvPair[0] = kvPair[0].replace("'", "\\'");
				sb.append("'" + kvPair[0] + "':undefined");
			} else if (kvPair.length == 2) {
				kvPair[0] = kvPair[0].replace("'", "\\'");
				kvPair[1] = kvPair[1].replace("'", "\\'");
				sb.append("'" + kvPair[0] + "':'" + kvPair[1] + "'");
			} else {
				//TODO: use logger
				//System.out.println("Invalid query.");
				return "";
			}
			i++;
		}
		return sb.toString();
	}

	@SafeVarargs
	public static String BuildArrays(List<? extends IJsonnable>...joLists) {
		StringBuilder sb = new StringBuilder();
		int i;
		int j = 0;
		if (joLists.length > 1) sb.append("[");
		for (List<? extends IJsonnable> joList : joLists) {
			if (j > 0) sb.append(",");
			i = 0;
			sb.append("[");
			for (IJsonnable jo : joList) {
				if (i > 0) sb.append(",");
				sb.append(jo.toJson());
				i++;
			}
			sb.append("]");
			j++;
		}
		if (joLists.length > 1) sb.append("]");
		return sb.toString();
	}

}
