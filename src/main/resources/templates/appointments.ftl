<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="appointments-buttons">
	<ul class="fake-buttons">
		<li>
			<a href="#search" class="app-fake-button">Search appointments</a>
		</li>
	</ul>
</div>

<table>
	<tr>
		<th>Appt. No.</th><th>Start</th><th>End</th><th>Attendee(s)</th><th>Instructor(s)</th><th>Service</th>
	</tr>
	<#list appointments as appointment>
		<tr>
			<td>${appointment.recID}</td>
			<td>${appointment.startTime}</td>
			<td>${appointment.endTime}</td>
			<td>${appointment.studentID}</td>
			<td>${appointment.teacherID}</td>
			<td>${appointment.service}</td>
		</tr>
	</#list>
</table>

</body>