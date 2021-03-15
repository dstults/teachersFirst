package edu.lwtech.csd297.teachersfirst.pojos;

import java.util.*;

public class JsonUtils {
	
	@SafeVarargs
	public static String BuildArrays(List<IJsonnable>...joLists) {
		StringBuilder sb = new StringBuilder("{");
		int i;
		int j = 0;
		for (List<IJsonnable> joList : joLists) {
			if (j > 0) sb.append(",");
			i = 0;
			sb.append("{");
			for (IJsonnable jo : joList) {
				if (i > 0) sb.append(",");
				sb.append(jo.toJson());
				i++;
			}
			sb.append("}");
			j++;
		}
		return sb.toString();
	}

}
