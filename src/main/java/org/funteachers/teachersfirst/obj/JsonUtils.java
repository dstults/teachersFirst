package org.funteachers.teachersfirst.obj;

import java.util.*;

public class JsonUtils {

	public static String queryToJson(String query) {
		if (query == null) return "";
		String sanitized = query.trim();
		if (sanitized.isEmpty()) return "";
		if (sanitized.substring(0, 1).equals("?"))
			sanitized = sanitized.substring(1);
		
		String[] splits = sanitized.split("&");
		String[] kvPair;
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (String line : splits) {
			if (i > 0) sb.append(",");
			kvPair = line.split("=");
			if (kvPair.length == 1) {
				sb.append("\"" + kvPair[0] + "\":undefined");
			} else if (kvPair.length == 2) {
				sb.append("\"" + kvPair[0] + "\":\"" + kvPair[1] + "\"");
			} else {
				// Crashes during unit tests:
				//ServerMain.logger.error("Invalid query when trying to convert from query to JSON.");
				System.out.println("Invalid query when trying to convert from query to JSON.");
				return "";
			}
			i++;
		}
		return sb.toString();
	}

	@SafeVarargs
	public static String BuildArrays(List<? extends IJsonnable>...joLists) {
		if (joLists == null || joLists.length == 0) return "[]";

		StringBuilder sb = new StringBuilder();
		int i;
		int j = 0;
		if (joLists.length > 1) sb.append("[");
		for (List<? extends IJsonnable> joList : joLists) {
			if (joList == null) {
				// Crashes during unit tests:
				//ServerMain.logger.error("JsonUtils.BuildArrays 'joLists' passed a null list at iterator [ " + j + " ]!");
				System.out.println("JsonUtils.BuildArrays 'joLists' passed a null list at iterator [ " + j + " ]!");
				sb.append("[]");
				continue;
			}
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
