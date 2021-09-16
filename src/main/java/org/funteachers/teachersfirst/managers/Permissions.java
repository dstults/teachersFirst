package org.funteachers.teachersfirst.managers;

import org.funteachers.teachersfirst.obj.*;

public class Permissions {
	
	public static boolean MemberCanSeeMember(Member actor, Member target) {
		return actor.getIsAdmin() || actor.getIsInstructor() || target.getIsInstructor() || actor.getRecID() == target.getRecID();
	}
	
	public static boolean MemberCanDeleteMember(Member actor, Member target) {
		// Cannot delete self
		if (actor == target) return false;

		// Must be instructor or admin
		if (!actor.getIsAdmin() && !actor.getIsInstructor()) return false;

		// Non-admin cannot delete admin
		if (!actor.getIsAdmin() && target.getIsAdmin()) return false;

		return true;
	}

	public static boolean MemberCanDeleteOpening(Member operator, Opening opening) {
		// All admins can delete all openings
		if (operator.getIsAdmin()) return true;

		// Must be "owner" (opening's "instructor")
		if (operator.getRecID() != opening.getInstructorID()) return false;

		return true;
	}

}
