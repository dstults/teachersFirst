const STATE = {REFUNDED: -2, MISSED: -1, UNKNOWN: 0, COMPLETED: 1, CANCELLED: 2};

let lastFilter = '';
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

const populateData = _ => {
	fetch('https://funteachers.org/appointments?json').then(response => response.json()).then(data => {
		[ allFutureData, allPastData ] = data;
		filterAppointments();
	}).catch(err => console.error(err.message));
};
//populateData();

const checkAddControl_Delete = (isPast, controls, appointment, row) => {
	if (isAdmin || appointment.completionState == STATE.CANCELLED && myId == appointment.instructorId) {
		if (controls.firstChild) controls.appendChild(document.createTextNode('   '));
		const child = document.createElement('a');
		child.classList.add('red');
		child.classList.add('size2p2');
		child.classList.add('bold');
		child.href = 'javascript:confirmDeleteAppointment(' + isPast + ', ' + appointment.id + ', ' + row + ');';
		child.innerHTML = 'X';
		controls.appendChild(child);
	}
};

const checkAddControl_Cancel = (isPast, controls, appointment, row) => {
	if (!isPast && appointment.completionState == STATE.UNKNOWN && (isAdmin || myId == appointment.instructorId || myId == appointment.studentId)) {
		if (controls.firstChild) controls.appendChild(document.createTextNode('   '));
		const child = document.createElement('a');
		child.classList.add('red');
		child.classList.add('size2p2');
		child.classList.add('bold');
		child.href = 'javascript:confirmCancelAppointment(' + isPast + ', ' + appointment.id + ', ' + row + ');';
		child.innerHTML = 'C';
		controls.appendChild(child);
	}
};

const checkAddControl_MissComplete = (isPast, controls, appointment, row) => {
	if (isPast && appointment.completionState == STATE.UNKNOWN && (isAdmin || myId == appointment.instructorId)) {
		if (controls.firstChild) controls.appendChild(document.createTextNode('   '));

		const child1 = document.createElement('a');
		child1.classList.add('blue');
		child1.classList.add('size2p4');
		child1.href = 'javascript:confirmMissAppointment(' + isPast + ', ' + appointment.id + ', ' + row + ');';
		child1.innerHTML = '&#9746;';
		controls.appendChild(child1);
		controls.appendChild(document.createTextNode('   '));
		const child2 = document.createElement('a');
		child2.classList.add('green');
		child2.classList.add('size2p4');
		child2.href = 'javascript:confirmCompleteAppointment(' + isPast + ', ' + appointment.id + ', ' + row + ');';
		child2.innerHTML = '&#9745;';
		controls.appendChild(child2);
	}
};

const checkAddControl_Refund = (isPast, controls, appointment, row) => {
	if (appointment.completionState == STATE.MISSED && (isAdmin || myId == appointment.instructorId)) {
		if (controls.firstChild) controls.appendChild(document.createTextNode('   '));
		const child = document.createElement('a');
		child.classList.add('green');
		child.classList.add('size2p4');
		child.href = 'javascript:confirmRefundAppointment(' + isPast + ', ' + appointment.id + ', ' + row + ');';
		child.innerHTML = 'R';
		controls.appendChild(child);
	}
};

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
		const isMyAppointment = myId == appointment.instructorId || myId == appointment.studentId;
		if (isAdmin && isMyAppointment) {
			tableRow.classList.add('soft-highlight');
		} else {
			tableRow.classList.remove('soft-highlight');
		}
		arrayIndex.innerHTML = 1 + row + (isPast ? pastRows * pastAppointmentPage : futureRows * futureAppointmentPage);
		checkAddControl_Delete(isPast, controls, appointment, row);
		checkAddControl_Cancel(isPast, controls, appointment, row);
		checkAddControl_MissComplete(isPast, controls, appointment, row);
		checkAddControl_Refund(isPast, controls, appointment, row);
		if (recId) recId.innerHTML = appointment.id;
		date.innerHTML = appointment.dateFormatted;
		startTime.innerHTML = appointment.startTimeFormatted;
		endTime.innerHTML = appointment.endTimeFormatted;
		student.innerHTML = appointment.studentName;
		student.href = '/profile?memberId=' + appointment.studentId;
		instructor.innerHTML = appointment.instructorName;
		instructor.href = '/profile?memberId=' + appointment.instructorId;
		
		switch (appointment.completionState) {
			case STATE.UNKNOWN:
				if (isPast) {
					status.innerHTML = '? NEEDS CONFIRMATION ?';
				} else {
					status.innerHTML = appointment.schedulingVerified ? 'verified' : 'NOT VERIFIED';
				}
				break;
			case STATE.MISSED:
				status.innerHTML = '! MISSED !';
				break;
			case STATE.REFUNDED:
				status.innerHTML = 'REFUNDED';
				break;
			case STATE.COMPLETED:
				status.innerHTML = 'completed';
				break;
			case STATE.CANCELLED:
				status.innerHTML = 'x CANCELLED x';
				break;
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

const updateFilter = _ => {
	lastFilter = prompt('Search criteria:\n(Student/instructor name or student/instructor ID)').toLowerCase();
	filterAppointments();
};
const filterAppointments = _ => {
	if (!lastFilter) {
		filteredFutureData = allFutureData;
		filteredPastData = allPastData;
	} else {
		filteredFutureData = allFutureData.filter(a => a.studentName.toLowerCase().includes(lastFilter) || a.instructorName.toLowerCase().includes(lastFilter) || a.studentId == lastFilter || a.instructorId == lastFilter || a.statusText.toLowerCase().includes(lastFilter));
		filteredPastData = allPastData.filter(a => a.studentName.toLowerCase().includes(lastFilter) || a.instructorName.toLowerCase().includes(lastFilter) || a.studentId == lastFilter || a.instructorId == lastFilter || a.statusText.toLowerCase().includes(lastFilter));
	}
	futureAppointmentPage = 0;
	pastAppointmentPage = 0;
	refreshAll();
};

// Old Method:
//const parser = new DOMParser();
//const data = parser.parseFromString(xhr.response, 'text/html');
//const messageBanner = data.getElementById('message-banner');
//alert(messageBanner.innerHTML);
const handleResponse = (xhr) => {
	if (xhr.status === 200) {
		window.temp = xhr;
		const { message } = JSON.parse(xhr.responseText);
		//alert(message);
		populateData();
	} else {
		controlElement.innerHTML = 'ERROR';
	}
};

// Olde response method (inside each xhr creation):
//xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
const confirmDeleteAppointment = (isPast, appointmentId, row) => {
	if (!confirm('Are you sure you want to delete appointment #' + appointmentId + ' ?')) return;

	const { controlElement, statusElement } = updateControlAndStatusElements(isPast, row);
	const xhr = new XMLHttpRequest();
	xhr.open('POST', '/');
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.onload = () => handleResponse(xhr);
	xhr.send('action=delete_appointment&appointmentId=' + appointmentId);
};
const confirmMissAppointment = (isPast, appointmentId, row) => {
	if (!confirm('You\'re sure you did not complete appointment #' + appointmentId + ' ?')) return;

	const { controlElement, statusElement } = updateControlAndStatusElements(isPast, row);
	const xhr = new XMLHttpRequest();
	xhr.open('POST', '/');
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.onload = () => handleResponse(xhr);
	xhr.send('action=miss_appointment&appointmentId=' + appointmentId);
};
const confirmCompleteAppointment = (isPast, appointmentId, row) => {
	if (!confirm('You\'re sure you completed appointment #' + appointmentId + ' ?')) return;

	const { controlElement, statusElement } = updateControlAndStatusElements(isPast, row);
	const xhr = new XMLHttpRequest();
	xhr.open('POST', '/');
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.onload = () => handleResponse(xhr);
	xhr.send('action=complete_appointment&appointmentId=' + appointmentId);
};
const confirmRefundAppointment = (isPast, appointmentId, row) => {
	if (!confirm('You\'re sure you want to refund appointment #' + appointmentId + ' ?')) return;

	const { controlElement, statusElement } = updateControlAndStatusElements(isPast, row);
	const xhr = new XMLHttpRequest();
	xhr.open('POST', '/');
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.onload = () => handleResponse(xhr);
	xhr.send('action=refund_appointment&appointmentId=' + appointmentId);
};
const confirmCancelAppointment = (isPast, appointmentId, row) => {
	if (!confirm('You\'re sure you want to cancel appointment #' + appointmentId + ' ?')) return;

	const { controlElement, statusElement } = updateControlAndStatusElements(isPast, row);
	const xhr = new XMLHttpRequest();
	xhr.open('POST', '/');
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.onload = () => handleResponse(xhr);
	xhr.send('action=cancel_appointment&appointmentId=' + appointmentId);
};
