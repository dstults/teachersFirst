<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>

<div class="fixed-width-subpage">

	<p>Please confirm that you'd like to make an appointment:</p>
	<p>Student: ${studentName}</p>
	<p>Instructor: ${instructorName}</p>
	<p>Date: ${date}</p>
	<p>Starting: ${appointmentStartTime}</p>
	<p>Ending: ${appointmentEndTime}</p>
	<br>
	<p>Does everything look correct?</p>
	<p>Wait, I need to fix something: <a href="/make_appointment?studentId=${studentId}&instructorId=${instructorId}&date=${date}&openingStartTime=${openingStartTime}&openingEndTime=${openingEndTime}&appointmentStartTime=${appointmentStartTime}&appointmentEndTime=${appointmentEndTime}">Go back.</a></p>

	<form method="post" action="/">
		<input type="hidden" name="action" value="make_appointment">
		<input type="hidden" name="studentId" value="${studentId}">
		<input type="hidden" name="instructorId" value="${instructorId}">
		<input type="hidden" name="date" value="${date}">
		<input type="hidden" name="appointmentStartTime" value="${appointmentStartTime}">
		<input type="hidden" name="appointmentEndTime" value="${appointmentEndTime}">
		<input type="submit" value="Make appointment">
	</form>

</div>

</#if>

</body>
</html>