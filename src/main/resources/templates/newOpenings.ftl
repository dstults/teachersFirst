<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="fixed-width-subpage">

	<#if userId lte 0>
		<#include "please_login.ftl">
	<#elseif isAdmin || isInstructor>

	<form method="post" action="/">
		<input type="hidden" name="action" value="new_openings">

		<input type="text" name="instructorId" value="${instructorId}" placeholder="startDate">

		<select name="daysOfWeek" id="daysOfWeek">
			<option value="Sunday">Sunday</option>
			<option value="Monday">Monday</option>
			<option value="Tuesday">Tuesday</option>
			<option value="Wednesday">Wednesday</option>
			<option value="Thursday">Thursday</option>
			<option value="Friday">Friday</option>
			<option value="Saturday">Saturday</option>
		</select>

		<input type="text" name="startDate" value="${startDate}" placeholder="startDate">
		<input type="text" name="endDate" value="${endDate}" placeholder="endDate">
		<input type="text" name="startTime" value="${startTime}" placeholder="startTime">
		<input type="text" name="endTime" value="${endTime}" placeholder="endTime">

		<input type="submit" value="Register">
	</form>

	<#else>
		<p>Sorry, but you must be a teacher to use this feature.</p>
	</#if>
</div>
</body>
</html>