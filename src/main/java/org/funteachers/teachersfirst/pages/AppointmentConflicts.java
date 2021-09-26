package org.funteachers.teachersfirst.pages;

import java.util.*;

import org.funteachers.teachersfirst.*;
import org.funteachers.teachersfirst.daos.sql.*;
import org.funteachers.teachersfirst.managers.*;
import org.funteachers.teachersfirst.obj.*;

public class AppointmentConflicts extends PageLoader {
	// Constructor
	public AppointmentConflicts(ConnectionPackage cp) { super(cp); }

	// Page-specific

	@Override
	public void loadPage() {
		// JSON - only
		getAppointmentConflictsJson();
	}

	private void getAppointmentConflictsJson() {
		final boolean userIsLoggedIn = this.uid > 0;
		if (!userIsLoggedIn) {
			sendJsonMessage("Error: You are not logged in.");
			return;
		}
		final boolean connectedToDatabase = this.connectionPackage.getConnection(this.getClass().getSimpleName()) != null;
		if (!connectedToDatabase) {
			sendJsonMessage("Error: Failed to contact database, please try again.");
			return;
		}

		// Possible options for returned JSON appointments:
		final String uid1String = QueryHelpers.getGet(request, "uid1");
		final String uid2String = QueryHelpers.getGet(request, "uid2");
		final String dateString = QueryHelpers.getGet(request, "date");
		final String startTimeString = QueryHelpers.getGet(request, "startTime");
		final String endTimeString = QueryHelpers.getGet(request, "endTime");

		final int uid1 = Integer.parseInt(uid1String);
		final int uid2 = Integer.parseInt(uid2String);
		final String dateStringSql = dateString.substring(6, 10) + "-" + dateString.substring(0, 2) + "-" + dateString.substring(3, 5);
		final String startTimeSql = dateStringSql + " " + startTimeString;
		final String endTimeSql = dateStringSql + " " + endTimeString;

		// Get data from DAOs
		final AppointmentSqlDAO appointmentDAO = (AppointmentSqlDAO) this.connectionPackage.getAppointmentDAO(this.getClass().getSimpleName());
		final List<Appointment> conflicts = appointmentDAO.getConflictsBetweenDatetimes(uid1, uid2, startTimeSql, endTimeSql);

		final String json = JsonUtils.BuildArrays(conflicts);
		trySendJson(json);
	}

}
