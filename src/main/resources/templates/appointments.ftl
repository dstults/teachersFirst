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
			<th></th><#if isAdmin || isInstructor><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th>
		</tr>
		<#list futureAppointments as appointment>
			<#if isAdmin && appointment.isMyAppointment><tr class="soft-highlight"><#else><tr></#if>
				<td><a href="javascript:confirmDeleteAppointment(${appointment.recID?c});" class="red bold">X</a></td>
				<#if isAdmin || isInstructor><td>${appointment.recID?c}</td></#if>
				<td>${appointment.dateFormatted}</td>
				<td>${appointment.startTimeFormatted}</td>
				<td>${appointment.endTimeFormatted}</td>
				<td><a href="/profile?memberId=${appointment.studentId}">${appointment.studentName}</a></td>
				<td><a href="/profile?memberId=${appointment.instructorId}">${appointment.instructorName}</a></td>
			</tr>
		</#list>
	</table>

	<h2 class="appointments-text">Previous appointments</h2>
	<table class="info-list">
		<tr>
			<#if isAdmin || isInstructor><th></th><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th>
		</tr>
		<#list pastAppointments as appointment>
			<#if isAdmin && appointment.isMyAppointment><tr class="soft-highlight"><#else><tr></#if>
				<td><#if isAdmin || isInstructor && appointment.wasNotCompleted>
				<a href="javascript:confirmDeleteAppointment(${appointment.recID?c});" class="red bold"> X </a>
				</#if><#if isAdmin || isInstructor && appointment.wasNotCompleted>
				<a href="javascript:confirmMissAppointment(${appointment.recID?c});" class="blue bold"> &#9746; </a>
				<a href="javascript:confirmCompleteAppointment(${appointment.recID?c})appointment.id;" class="green bold"> &#9745; </a>
				</#if></td>
				<#if isAdmin || isInstructor><td>${appointment.recID?c}</td></#if>
				<td>${appointment.dateFormatted}</td>
				<td>${appointment.startTimeFormatted}</td>
				<td>${appointment.endTimeFormatted}</td>
				<td><a href="/profile?memberId=${appointment.studentId}">${appointment.studentName}</a></td>
				<td><a href="/profile?memberId=${appointment.instructorId}">${appointment.instructorName}</a></td>
			</tr>
		</#list>
	</table>

</#if>

</body>
<script>
	const confirmDeleteAppointment = (appointmentId) => {
		if (confirm('Are you sure you want to delete appointment.recID #' + appointmentId + ' ?')) {
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
			xhr.send('action=delete_appointment&appointmentId=' + appointmentId);
		}
	}
	const confirmMissAppointment = (appointmentId) => {
		if (confirm('You\'re sure you did not complete appointment.recID #' + appointmentId + ' ?')) {
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
			xhr.send('action=miss_appointment&appointmentId=' + appointmentId);
		}
	}
	const confirmCompleteAppointment = (appointmentId) => {
		if (confirm('You\'re sure you completed appointment.recID #' + appointmentId + ' ?')) {
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
			xhr.send('action=complete_appointment&appointmentId=' + appointmentId);
		}
	}
</script>
</html>