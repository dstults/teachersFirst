<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>

	<a href="javascript:filterAppointments();" class="buttonize-link">Filter Appointments</a>
	<div class="appointment-title-container">
		<div class="appointment-page-controls">
			<a href="javascript:futurePagePrev();">Prev Page</a>
			<p id="current-future-page">1</p>
			<a href="javascript:futurePageNext();">Next Page</a>
		</div>
		<h2 class="appointments-subtitle">Upcoming appointments</h2>
		<div></div>
	</div>
	<table class="info-list">
		<tr>
			<th></th><#if isAdmin || isInstructor><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th><th>Status</th>
		</tr>
		<#list 0..9 as i>
			<tr id="future-row-${i?c}">
				<td><a id="future-row-${i?c}-deleter" href="javascript:confirmDeleteFutureAppointment(${i});" class="red bold"></a></td>
				<#if isAdmin || isInstructor><td id="future-row-${i?c}-recID"></td></#if>
				<td id="future-row-${i?c}-date"></td>
				<td id="future-row-${i?c}-startTime"></td>
				<td id="future-row-${i?c}-endTime"></td>
				<td><a id="future-row-${i?c}-student"></a></td>
				<td><a id="future-row-${i?c}-instructor"></a></td>
				<td id="future-row-${i?c}-status"></td>
			</tr>
		</#list>
	</table>

	<div class="appointment-title-container">
		<div class="appointment-page-controls">
			<a href="javascript:pastPagePrev();">Prev Page</a>
			<p id="current-past-page">1</p>
			<a href="javascript:pastPageNext();">Next Page</a>
		</div>
		<h2 class="appointments-subtitle">Previous appointments</h2>
		<div></div>
	</div>
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
	const isAdmin = <#if isAdmin>true<#else>false</#if>;
	const isInstructor = <#if isInstructor>true<#else>false</#if>;
	const messageBanner = document.getElementById('messageBanner');
	let allFutureData = null;
	let filteredFutureData = null;
	let futureAppointmentPage = 0;
	const pageNumberElem = document.getElementById('current-future-page');
	let allPastData = null;
	let filteredPastData = null;
	let pastAppointmentPage = 0;
	fetch('https://funteachers.org/appointments?json').then(response => response.json()).then(data => {
		[ allFutureData, allPastData ] = data;
		[ filteredFutureData, filteredPastData]= [ allFutureData, allPastData ];		
		renderFutureAppointments();
	}).catch(err => console.error(err.message));

	const futurePagePrev = _ => {
		if (futureAppointmentPage > 0) {
			futureAppointmentPage--;
			renderFutureAppointments();
		} else {
			// This is annoying, it should just disable/reenable.
			//alert('Already at first page!');
		}
	};
	const futurePageNext = _ => {
		if (futureAppointmentPage < Math.trunc(filteredFutureData.length / 10)) {
			futureAppointmentPage++;
			renderFutureAppointments();
		} else {
			// This is annoying, it should just disable/reenable.
			//alert('Already at last page!');
		}
	};
	const getFutureAppointmentFromRow = (row) => filteredFutureData[row + 10 * futureAppointmentPage];
	const renderFutureAppointments = _ => {
		pageNumberElem.innerHTML = futureAppointmentPage + 1;
		for (let i = 0; i < 10; i++) {
			const currentAppointment = getFutureAppointmentFromRow(i);
			const row = document.getElementById('future-row-' + i);
			const deleter = document.getElementById('future-row-' + i + '-deleter');
			const recId = document.getElementById('future-row-' + i + '-recID');
			const date = document.getElementById('future-row-' + i + '-date');
			const startTime = document.getElementById('future-row-' + i + '-startTime');
			const endTime = document.getElementById('future-row-' + i + '-endTime');
			const student = document.getElementById('future-row-' + i + '-student');
			const instructor = document.getElementById('future-row-' + i + '-instructor');
			const status = document.getElementById('future-row-' + i + '-status');
			if (!currentAppointment) {
				row.classList.remove('soft-highlight');
				deleter.innerHTML = '';
				if (recId) recId.innerHTML = '';
				date.innerHTML = '';
				startTime.innerHTML = '';
				endTime.innerHTML = '';
				student.innerHTML = '';
				instructor.innerHTML = '';
				status.innerHTML = '';
			} else {
				row.classList.add('soft-highlight');
				deleter.innerHTML = 'X';
				if (recId) recId.innerHTML = currentAppointment.id;
				date.innerHTML = currentAppointment.dateFormatted;
				startTime.innerHTML = currentAppointment.startTimeFormatted;
				endTime.innerHTML = currentAppointment.endTimeFormatted;
				student.innerHTML = currentAppointment.studentName;
				instructor.innerHTML = currentAppointment.instructorName;
				status.innerHTML = currentAppointment.schedulingVerified ? 'verified' : 'NOT VERIFIED';
			}
		}
	};

	const pastPagePrev = _ => {
		if (pastAppointmentPage > 0) {
			pastAppointmentPage--;
			renderPastAppointments();
		} else {
			// This is annoying, it should just disable/reenable.
			//alert('Already at first page!');
		}
	};
	const pastPageNext = _ => {
		if (pastAppointmentPage < Math.trunc(filteredPastData.length / 10)) {
			pastAppointmentPage++;
			renderPastAppointments();
		} else {
			// This is annoying, it should just disable/reenable.
			//alert('Already at last page!');
		}
	};
	const getPastAppointmentFromRow = (row) => filteredPastData[row + 10 * pastAppointmentPage];

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
	const filterAppointments = _ => {
		const filter = prompt('Search criteria:\n(Student/instructor name or student/instructor ID)').toLowerCase();
		if (!filter) {
			filteredFutureData = allFutureData;
			filteredPastData = allPastData;
		} else {
			filteredFutureData = allFutureData.filter(a => a.studentName.toLowerCase().includes(filter) || a.instructorName.toLowerCase().includes(filter) || a.studentId == filter || a.instructorId == filter);
			filteredPastData = allPastData.filter(a => a.studentName.toLowerCase().includes(filter) || a.instructorName.toLowerCase().includes(filter) || a.studentId == filter || a.instructorId == filter);
		}
	}
	const confirmDeleteFutureAppointment = (row) => {
		const appointment = getFutureAppointmentFromRow(row);
		alert(row);
	};
	const confirmDeletePastAppointment = (row) => {
		const appointment = getPastAppointmentFromRow(row);
		alert(row);
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