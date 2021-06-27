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
			<th></th><#if isAdmin || isInstructor><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th><th>Status</th>
		</tr>
		<#list futureAppointments as appointment>
			<#if isAdmin && appointment.isMyAppointment><tr class="soft-highlight"><#else><tr></#if>
				<td id="appt-${appointment.recID?c}-control"><a href="javascript:confirmDeleteAppointment(${appointment.recID?c});" class="red bold">X</a></td>
				<#if isAdmin || isInstructor><td>${appointment.recID?c}</td></#if>
				<td>${appointment.dateFormatted}</td>
				<td>${appointment.startTimeFormatted}</td>
				<td>${appointment.endTimeFormatted}</td>
				<td><a href="/profile?memberId=${appointment.studentID?c}">${appointment.studentName}</a></td>
				<td><a href="/profile?memberId=${appointment.instructorID?c}">${appointment.instructorName}</a></td>
				<td id="appt-${appointment.recID?c}-status"><#if appointment.schedulingVerified>verified<#else>NOT VERIFIED</#if></td>
			</tr>
		</#list>
	</table>

	<h2 class="appointments-text">Previous appointments</h2>
	<table class="info-list">
		<tr>
			<#if isAdmin || isInstructor><th style="min-width: 150px;">Controls</th><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th><th>Status</th>
		</tr>
		<#list pastAppointments as appointment>
			<#if isAdmin && appointment.isMyAppointment><tr class="soft-highlight"><#else><tr></#if>
				<td id="appt-${appointment.recID?c}-control">
					<#if isAdmin>
					<a href="javascript:confirmDeleteAppointment(${appointment.recID?c});" class="red slightly-bigger bold">X</a>&nbsp;&nbsp;&nbsp;
					<a href="javascript:confirmMissAppointment(${appointment.recID?c});" class="blue bigger">&#9746;</a>&nbsp;&nbsp;&nbsp;
					<a href="javascript:confirmCompleteAppointment(${appointment.recID?c});" class="green bigger">&#9745;</a>
					</#if>
					<#if isInstructor && appointment.wasNotCompleted>
					<a href="javascript:confirmDeleteAppointment(${appointment.recID?c});" class="red bold">X</a>
					<#elseif isInstructor && appointment.completionUnconfirmed>
					<a href="javascript:confirmMissAppointment(${appointment.recID?c});" class="blue bigger">&#9746;</a>&nbsp;&nbsp;&nbsp;
					<a href="javascript:confirmCompleteAppointment(${appointment.recID?c});" class="green bigger">&#9745;</a>
					</#if>
				</td>
				<#if isAdmin || isInstructor><td>${appointment.recID?c}</td></#if>
				<td>${appointment.dateFormatted}</td>
				<td>${appointment.startTimeFormatted}</td>
				<td>${appointment.endTimeFormatted}</td>
				<td><a href="/profile?memberId=${appointment.studentID?c}">${appointment.studentName}</a></td>
				<td><a href="/profile?memberId=${appointment.instructorID?c}">${appointment.instructorName}</a></td>
				<td id="appt-${appointment.recID?c}-status"><#if appointment.completionUnconfirmed>NEEDS COMPLETION CONFIRMATION<#elseif appointment.wasCompleted>completed<#elseif appointment.wasNotCompleted>CANCELLED</#if></td>
			</tr>
		</#list>
	</table>

</#if>

</body>
<script>
	const messageBanner = doc.getElementById('messageBanner');
	let futureAppointmentData = null;
	let pastAppointmentData = null;
	fetch('/appointments?json').then(response => response.json()).then(data => {
		[ futureAppointmentData, pastAppointmentData ] = data;
		console.log('data parse successful for [futureAppointmentData] and [pastAppointmentData]');
	}).catch(err => console.error(err.message));

	const getControlElement = (appointmentId) => {
		const e1 = document.getElementById('appt-' + appointmentId + '-control');
		e1.innerHTML = '...';
		return e1;
	};
	const getStatusElement = (appointmentId) => {
		const e2 = document.getElementById('appt-' + appointmentId + '-status');
		e2.innerHTML = '...updating...';
		return e2;
	};
	const handleDeleteResponse = (xhr, controlElement, statusElement, statusText) => {
		if (xhr.status === 200) {
			controlElement.innerHTML = '';

			let parser = new DOMParser();
			let doc = parser.parseFromString(xhr.response, 'text/html');
			statusElement.innerHTML = messageBanner.innerHTML;

		} else {
			controlElement.innerHTML = 'ERROR';
		}
	};
	const confirmDeleteAppointment = (appointmentId) => {
		if (confirm('Are you sure you want to delete appointment #' + appointmentId + ' ?')) {
			const e1 = getControlElement(appointmentId);
			const e2 = getStatusElement(appointmentId);
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			//xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
			xhr.onload = () => handleDeleteResponse(xhr, e1, e2, '!! DELETED !!');
			xhr.send('action=delete_appointment&appointmentId=' + appointmentId);
		}
	};
	const handleCompleteMissResponse = (xhr, controlElement, statusElement, statusText, appointmentId) => {
		if (xhr.status === 200) {
			if (statusText.includes('CANCELLED')) {
				controlElement.innerHTML = '<a href="javascript:confirmDeleteAppointment(' + appointmentId + ');" class="red bold">X</a>';
			} else {
				controlElement.innerHTML = '';
			}
			statusElement.innerHTML = statusText;
		} else {
			controlElement.innerHTML = 'ERROR';
		}
	};
	const confirmMissAppointment = (appointmentId) => {
		if (confirm('You\'re sure you did not complete appointment #' + appointmentId + ' ?')) {
			const e1 = getControlElement(appointmentId);
			const e2 = getStatusElement(appointmentId);
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			//xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
			xhr.onload = () => handleCompleteMissResponse(xhr, e1, e2, 'CANCELLED', appointmentId);
			xhr.send('action=miss_appointment&appointmentId=' + appointmentId);
		}
	}
	const confirmCompleteAppointment = (appointmentId) => {
		if (confirm('You\'re sure you completed appointment #' + appointmentId + ' ?')) {
			const e1 = getControlElement(appointmentId);
			const e2 = getStatusElement(appointmentId);
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			//xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
			xhr.onload = () => handleCompleteMissResponse(xhr, e1, e2, 'completed', appointmentId);
			xhr.send('action=complete_appointment&appointmentId=' + appointmentId);
		}
	}
</script>
</html>