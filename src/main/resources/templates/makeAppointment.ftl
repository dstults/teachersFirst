<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>

<div class="make-appointment">
	<form method="get" action="/confirm_make_appointment">
		<input type="hidden" name="studentId" value="${studentId}">
		<input type="hidden" name="instructorId" value="${instructorId}">
		<input type="hidden" name="date" value="${date}">
		<input type="hidden" name="openingStartTime" value="${openingStartTime}">
		<input type="hidden" name="openingEndTime" value="${openingEndTime}">

		<p>Student: ${studentName}</p>
		<p>Instructor: ${instructorName}</p>
		<p>Date: ${date}</p>
		<p>Available from: ${openingStartTime}</p>
		<p>Available till: ${openingEndTime}</p>

		<label for="appointmentStartTime">Choose a start time:</label>
		<select name="appointmentStartTime" id="appointmentStartTime">
			<#list possibleStartTimes as possibleStartTime>
				<#if possibleStartTime = defaultStartTime>
				<option value="${possibleStartTime}" selected="selected">${possibleStartTime}</option>
				<#else>
				<option value="${possibleStartTime}">${possibleStartTime}</option>
				</#if>
			</#list>
		</select>
		<br>

		<label for="appointmentDuration">Choose the class duration: </label>
		<select name="appointmentDuration" id="appointmentDuration">
			<#list possibleDurations as possibleDuration>
				<#if possibleDuration = defaultDuration>
				<option value="${possibleDuration}" selected="selected">${possibleDuration}</option>
				<#else>
				<option value="${possibleDuration}">${possibleDuration}</option>
				</#if>
			</#list>
		</select>

		<input type="submit" value="Make Appointment">
	</form>
</div>

</#if>

</body>
</html>