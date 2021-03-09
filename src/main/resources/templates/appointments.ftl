<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>

	<div class="appointments-buttons">
		<ul class="fake-buttons">
			<li>
				<a href="#search" class="app-fake-button">Search appointments</a>
			</li>
		</ul>
	</div>

	<table>
		<tr>
			<#if isAdmin><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th>
		</tr>
		<#list appointments as appointment>
			<tr>
				<#if isAdmin>
				<td>${appointment.id}</td>
				</#if>
				<td>${appointment.date}</td>
				<td>${appointment.startTime}</td>
				<td>${appointment.endTime}</td>
				<td>${appointment.student}</td>
				<td>${appointment.instructor}</td>
			</tr>
		</#list>
	</table>

</#if>

</body>
</html>