package edu.lwtech.csd297.teachersfirst.pojos;

import java.util.List;

public class MemberHelpers {
	
	public static Member FindByID(List<Member> members, int recID) {
		for (Member member : members) {
			if (member.getRecID() == recID) return member;
		}
		return null;
	}
	
	public static String FindNameByID(List<Member> members, int recID) {
		Member found = FindByID(members, recID);
		if (found == null) return "unknown";
		return found.getName();
	}

}
