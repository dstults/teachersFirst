package org.funteachers.teachersfirst.managers;

import org.funteachers.teachersfirst.obj.*;

public class Permissions {
	
	public static boolean MemberCanSeeMember(Member actor, Member target) {
		// Null checks
		if (actor == null || target == null) return false;

		// Employees (admins and instructors) see all
		if (actor.getIsAdmin() || actor.getIsInstructor()) return true;

		// Instructors can be seen by anyone
		if (target.getIsInstructor()) return true;

		// Everyone can see themselves -- could use obj.equals(obj) but recID is faster and safer
		if (actor.getRecID() == target.getRecID()) return true;

		return false;
	}
	
	public static boolean MemberCanDeleteMember(Member actor, Member target) {
		// Null checks
		if (actor == null || target == null) return false;

		// Cannot delete self -- could use obj.equals(obj) but recID is faster and safer
		if (actor.getRecID() == target.getRecID()) return false;

		// Must be instructor or admin
		if (!actor.getIsAdmin() && !actor.getIsInstructor()) return false;

		// Non-admin cannot delete admin
		if (!actor.getIsAdmin() && target.getIsAdmin()) return false;

		return true;
	}

	public static boolean MemberCanDeleteOpening(Member operator, Opening opening) {
		// Null checks
		if (operator == null || opening == null) return false;

		// All admins can delete all openings
		if (operator.getIsAdmin()) return true;

		// Must be "owner" (opening's "instructor")
		if (operator.getRecID() != opening.getInstructorID()) return false;

		return true;
	}

}
