package org.funteachers.teachersfirst.managers;

import org.funteachers.teachersfirst.obj.*;

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

	public static boolean MemberCanDeleteOpening(Member operator, Opening opening) {
		// All admins can delete all openings -- short out
		if (operator.getIsAdmin()) return true;

		// Must at least be an instructor if not an admin (above)
		if (!operator.getIsInstructor()) return false;

		// If instructor, must be self
		if (operator.getRecID() != opening.getInstructorID()) return false;

		return true;
	}

}
