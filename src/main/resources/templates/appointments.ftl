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

	<h2 class="appointments-text" style="margin-top: 11rem;">Upcoming appointments</h2>
	<table class="info-list">
		<tr>
			<th></th><#if isAdmin><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th>
		</tr>
		<#list futureAppointments as appointment>
			<#if isAdmin && appointment.isMyAppointment>
			<tr class="soft-highlight">
			<#else>
			<tr>
			</#if>
				<td><a href="javascript:confirmDeleteAppointment(${appointment.id?c});" class="red bold">X</a></td>
				<#if isAdmin>
				<td>${appointment.id?c}</td>
				</#if>
				<td>${appointment.date}</td>
				<td>${appointment.startTime}</td>
				<td>${appointment.endTime}</td>
				<td><a href="/profile?memberId=${appointment.studentId}">${appointment.studentName}</a></td>
				<td><a href="/profile?memberId=${appointment.instructorId}">${appointment.instructorName}</a></td>
			</tr>
		</#list>
	</table>

	<h2 class="appointments-text">Previous appointments</h2>
	<table class="info-list">
		<tr>
			<#if isAdmin><th></th><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th>
		</tr>
		<#list pastAppointments as appointment>
			<#if isAdmin && appointment.isMyAppointment>
			<tr class="soft-highlight">
			<#else>
			<tr>
			</#if>
				<#if isAdmin>
				<td><a href="javascript:confirmDeleteAppointment(${appointment.id?c});" class="red bold">X</a></td>
				<td>${appointment.id?c}</td>
				</#if>
				<td>${appointment.date}</td>
				<td>${appointment.startTime}</td>
				<td>${appointment.endTime}</td>
				<td><a href="/profile?memberId=${appointment.studentId}">${appointment.studentName}</a></td>
				<td><a href="/profile?memberId=${appointment.instructorId}">${appointment.instructorName}</a></td>
			</tr>
		</#list>
	</table>

</#if>

</body>
<script>
const confirmDeleteAppointment = (appointmentId) => {
	if (confirm('Are you sure you want to delete appointment ID #' + appointmentId + ' ?')) {
		const xhr = new XMLHttpRequest();
		xhr.open('POST', '/');
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
		xhr.send('action=delete_appointment&appointmentId=' + appointmentId);
	}
}
</script>
</html>