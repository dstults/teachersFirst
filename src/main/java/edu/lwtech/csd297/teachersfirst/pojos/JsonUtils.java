package edu.lwtech.csd297.teachersfirst.pojos;

import java.util.*;

public class JsonUtils {
	
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
