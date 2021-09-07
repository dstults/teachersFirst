package org.funteachers.teachersfirst.managers;

import org.funteachers.teachersfirst.obj.Member;

public class Permissions {
	
	public static boolean MemberCanDeleteMember(Member actor, Member target) {
		// Cannot delete self
		if (actor == target) return false;

		// Must be instructor or admin
		if (!actor.getIsAdmin() && !actor.getIsInstructor()) return false;

		// Non-admin cannot delete admin
		if (target.getIsAdmin() && !actor.getIsAdmin()) return false;

		return true;
	}

}
