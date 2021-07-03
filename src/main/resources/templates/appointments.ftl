<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
	<div class="page-content-750-1000">
<#if userId lte 0>
	<#include "please_login.ftl">
<#else>
		<div class="top-buttons">
			<a href="javascript:filterAppointments();" class="buttonize-link">Filter Appointments</a>
		</div>
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
				<th style="width: 50px;">#</th><th style="width: 135px;">Controls</th><#if isAdmin || isInstructor><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th><th style="width: 265px;">Status</th>
			</tr>
			<#list 0..9 as i>
			<tr id="future-row-${i?c}">
				<td id="future-row-${i?c}-arrayindex"></td>
				<td id="future-row-${i?c}-controls"></td>
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
				<th style="width: 50px;">#</th><#if isAdmin || isInstructor><th style="width: 135px;">Controls</th><th>No.</th></#if><th>Date</th><th>Start</th><th>End</th><th>Attendee</th><th>Instructor</th><th style="width: 265px;">Status</th>
			</tr>
			<#list 0..14 as i>
			<tr id="past-row-${i?c}">
				<td id="past-row-${i?c}-arrayindex"></td>
				<td id="past-row-${i?c}-controls"></td>
				<#if isAdmin || isInstructor><td id="past-row-${i?c}-recID"></td></#if>
				<td id="past-row-${i?c}-date"></td>
				<td id="past-row-${i?c}-startTime"></td>
				<td id="past-row-${i?c}-endTime"></td>
				<td><a id="past-row-${i?c}-student"></a></td>
				<td><a id="past-row-${i?c}-instructor"></a></td>
				<td id="past-row-${i?c}-status"></td>
			</tr>
			</#list>
		</table>
</#if>
	</div>
</body>
<#if userId gt 0>
<script>
	const isAdmin = <#if isAdmin>true<#else>false</#if>;
	const isInstructor = <#if isInstructor>true<#else>false</#if>;
	let allFutureData = null;
	let filteredFutureData = null;
	let futureAppointmentPage = 0;
	const futurePageNumberElem = document.getElementById('current-future-page');
	const futureRows = 10;
	let allPastData = null;
	let filteredPastData = null;
	let pastAppointmentPage = 0;
	const pastPageNumberElem = document.getElementById('current-past-page');
	const pastRows = 15;
	fetch('https://funteachers.org/appointments?json').then(response => response.json()).then(data => {
		[ allFutureData, allPastData ] = data;
		[ filteredFutureData, filteredPastData] = [ allFutureData, allPastData ];		
		refreshAll();
	}).catch(err => console.error(err.message));

	const renderRow = (isPast, appointment, row) => {
		const futurePast = isPast ? 'past' : 'future';
		const tableRow = document.getElementById(futurePast + '-row-' + row);
		const arrayIndex = document.getElementById(futurePast + '-row-' + row + '-arrayindex');
		const controls = document.getElementById(futurePast + '-row-' + row + '-controls');
		while (controls.firstChild) {
			controls.removeChild(controls.firstChild);
		}
		const recId = document.getElementById(futurePast + '-row-' + row + '-recID');
		const date = document.getElementById(futurePast + '-row-' + row + '-date');
		const startTime = document.getElementById(futurePast + '-row-' + row + '-startTime');
		const endTime = document.getElementById(futurePast + '-row-' + row + '-endTime');
		const student = document.getElementById(futurePast + '-row-' + row + '-student');
		const instructor = document.getElementById(futurePast + '-row-' + row + '-instructor');
		const status = document.getElementById(futurePast + '-row-' + row + '-status');
		if (!appointment) {
			tableRow.classList.remove('soft-highlight');
			arrayIndex.innerHTML = '';
			controls.innerHTML = '';
			if (recId) recId.innerHTML = '';
			date.innerHTML = '';
			startTime.innerHTML = '';
			endTime.innerHTML = '';
			student.innerHTML = '';
			student.href = '';
			instructor.innerHTML = '';
			instructor.href = '';
			status.innerHTML = '';
		} else {
			if (isAdmin && appointment.isMyAppointment) {
				tableRow.classList.add('soft-highlight');
			} else {
				tableRow.classList.remove('soft-highlight');
			}
			arrayIndex.innerHTML = 1 + row + (isPast ? pastRows * pastAppointmentPage : futureRows * futureAppointmentPage);
			if (isAdmin || !isPast || appointment.completionState == 0) {
				const child = document.createElement('a');
				child.classList.add('red');
				child.classList.add('bold');
				if (isPast) child.classList.add('slightly-bigger');
				child.href = 'javascript:confirmDeleteAppointment(' + isPast + ', ' + appointment.id + ', ' + row + ');';
				child.innerHTML = 'X';
				controls.appendChild(child);
			}
			if (isPast && (isAdmin || appointment.completionState == -1)) {
				if (controls.firstChild) {
					controls.appendChild(document.createTextNode('   '));
				}
				const child1 = document.createElement('a');
				child1.classList.add('blue');
				child1.classList.add('bigger');
				child1.href = 'javascript:confirmMissAppointment(' + isPast + ', ' + appointment.id + ', ' + row + ');';
				child1.innerHTML = '&#9746;';
				controls.appendChild(child1);
				controls.appendChild(document.createTextNode('   '));
				const child2 = document.createElement('a');
				child2.classList.add('green');
				child2.classList.add('bigger');
				child2.href = 'javascript:confirmCompleteAppointment(' + isPast + ', ' + appointment.id + ', ' + row + ');';
				child2.innerHTML = '&#9745;';
				controls.appendChild(child2);
			}
			if (recId) recId.innerHTML = appointment.id;
			date.innerHTML = appointment.dateFormatted;
			startTime.innerHTML = appointment.startTimeFormatted;
			endTime.innerHTML = appointment.endTimeFormatted;
			student.innerHTML = appointment.studentName;
			student.href = '/profile?memberId=' + appointment.studentID;
			instructor.innerHTML = appointment.instructorName;
			instructor.href = '/profile?memberId=' + appointment.instructorID;
			if (!isPast) {
				status.innerHTML = appointment.schedulingVerified ? 'verified' : 'NOT VERIFIED';
			} else {
				switch (appointment.completionState) {
					case -1:
						status.innerHTML = 'NEEDS COMPLETION CONFIRMATION';
						break;
					case 0:
						status.innerHTML = 'CANCELLED';
						break;
					case 1:
						status.innerHTML = 'completed';
						break;
				}
			}
		}
	};

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
		if (futureAppointmentPage < Math.trunc((filteredFutureData.length - 1) / futureRows)) {
			futureAppointmentPage++;
			renderFutureAppointments();
		} else {
			// This is annoying, it should just disable/reenable.
			//alert('Already at last page!');
		}
	};
	const getFutureAppointmentFromRow = (row) => filteredFutureData[row + futureRows * futureAppointmentPage];
	const renderFutureAppointments = _ => {
		futurePageNumberElem.innerHTML = futureAppointmentPage + 1;
		for (let i = 0; i < futureRows; i++) {
			const appointment = getFutureAppointmentFromRow(i);
			renderRow(false, appointment, i);
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
		if (pastAppointmentPage < Math.trunc((filteredPastData.length - 1) / pastRows)) {
			pastAppointmentPage++;
			renderPastAppointments();
		} else {
			// This is annoying, it should just disable/reenable.
			//alert('Already at last page!');
		}
	};
	const getPastAppointmentFromRow = (row) => filteredPastData[row + pastRows * pastAppointmentPage];
	const renderPastAppointments = _ => {
		pastPageNumberElem.innerHTML = pastAppointmentPage + 1;
		for (let i = 0; i < pastRows; i++) {
			const appointment = getPastAppointmentFromRow(i);
			renderRow(true, appointment, i);
		}
	};

	const refreshAll = _ => {
		renderFutureAppointments();
		renderPastAppointments();
	};

	const updateControlAndStatusElements = (isPast, row) => {
		const futurePast = isPast ? 'past' : 'future';
		const controlElement = document.getElementById(futurePast + '-row-' + row + '-controls');
		while (controlElement.firstChild) {
			controlElement.removeChild(controlElement.firstChild);
		}
		controlElement.innerHTML = '...';
		const statusElement = document.getElementById(futurePast + '-row-' + row + '-status');
		statusElement.innerHTML = '...updating...';
		return { controlElement, statusElement };
	};
	const filterAppointments = _ => {
		const filter = prompt('Search criteria:\n(Student/instructor name or student/instructor ID)').toLowerCase();
		if (!filter) {
			filteredFutureData = allFutureData;
			filteredPastData = allPastData;
		} else {
			filteredFutureData = allFutureData.filter(a => a.studentName.toLowerCase().includes(filter) || a.instructorName.toLowerCase().includes(filter) || a.studentId == filter || a.instructorId == filter || a.statusText.toLowerCase().includes(filter));
			filteredPastData = allPastData.filter(a => a.studentName.toLowerCase().includes(filter) || a.instructorName.toLowerCase().includes(filter) || a.studentId == filter || a.instructorId == filter || a.statusText.toLowerCase().includes(filter));
		}
		futureAppointmentPage = 0;
		pastAppointmentPage = 0;
		refreshAll();
	}

	const handleDeleteResponse = (xhr, controlElement, statusElement) => {
		if (xhr.status === 200) {
			controlElement.innerHTML = '';
			const parser = new DOMParser();
			const data = parser.parseFromString(xhr.response, 'text/html');
			const messageBanner = data.getElementById('message-banner');
			statusElement.innerHTML = messageBanner.innerHTML;
		} else {
			controlElement.innerHTML = 'ERROR';
		}
	};
	const confirmDeleteAppointment = (isPast, appointmentId, row) => {
		if (confirm('Are you sure you want to delete appointment #' + appointmentId + ' ?')) {
			const { controlElement, statusElement } = updateControlAndStatusElements(isPast, row);
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			//xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
			xhr.onload = () => handleDeleteResponse(xhr, controlElement, statusElement);
			xhr.send('action=delete_appointment&appointmentId=' + appointmentId);
		}
	};
	const handleCompleteMissResponse = (xhr, controlElement, statusElement, statusText, appointmentId, isPast, row) => {
		if (xhr.status === 200) {
			if (statusText.includes('CANCELLED')) {
				controlElement.innerHTML = '<a href="javascript:confirmDeleteAppointment(' + isPast + ', ' + appointmentId + ', ' + row + ');" class="red slightly-bigger bold">X</a>';
			} else {
				controlElement.innerHTML = '';
			}
			statusElement.innerHTML = statusText;
		} else {
			controlElement.innerHTML = 'ERROR';
		}
	};
	const confirmMissAppointment = (isPast, appointmentId, row) => {
		if (confirm('You\'re sure you did not complete appointment #' + appointmentId + ' ?')) {
			const { controlElement, statusElement } = updateControlAndStatusElements(isPast, row);
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			//xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
			xhr.onload = () => handleCompleteMissResponse(xhr, controlElement, statusElement, 'CANCELLED', appointmentId, isPast, row);
			xhr.send('action=miss_appointment&appointmentId=' + appointmentId);
		}
	}
	const confirmCompleteAppointment = (isPast, appointmentId, row) => {
		if (confirm('You\'re sure you completed appointment #' + appointmentId + ' ?')) {
			const { controlElement, statusElement } = updateControlAndStatusElements(isPast, row);
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			//xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
			xhr.onload = () => handleCompleteMissResponse(xhr, controlElement, statusElement, 'completed', appointmentId, isPast, row);
			xhr.send('action=complete_appointment&appointmentId=' + appointmentId);
		}
	}
</script>
</#if>
</html>