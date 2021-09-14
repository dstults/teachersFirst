
let allMembers;

const instructorIdSelector = document.getElementById('instructorId');
const studentIdSelector = document.getElementById('studentId');

fetch('/members?json').then(response => response.json()).then(data => {
	allMembers = data;
	populateSelectors();
}).catch(err => console.error(err.message));

const populateSelectors = _ => {
	for (const member of allMembers) {
		if (!member.id || !member.displayName) continue;
		const memberOption = document.createElement('option');
		memberOption.value = member.id;
		memberOption.innerText = member.id + ' | ' + member.displayName;
		instructorIdSelector.appendChild(memberOption);
		studentIdSelector.appendChild(memberOption.cloneNode(true));
		if (userId && userId === member.id) {
			if (isInstructor || isAdmin) instructorIdSelector.value = userId;
			if (isStudent || isAdmin) studentIdSelector.value = userId;
		}
	}
};

const getDaysOfWeek = _ => {
	const sunday = document.getElementById('sunday').checked ? document.getElementById('sunday').value : '';
	const monday = document.getElementById('monday').checked ? document.getElementById('monday').value : '';
	const tuesday = document.getElementById('tuesday').checked ? document.getElementById('tuesday').value : '';
	const wednesday = document.getElementById('wednesday').checked ? document.getElementById('wednesday').value : '';
	const thursday = document.getElementById('thursday').checked ? document.getElementById('thursday').value : '';
	const friday = document.getElementById('friday').checked ? document.getElementById('friday').value : '';
	const saturday = document.getElementById('saturday').checked ? document.getElementById('saturday').value : '';
	return sunday + monday + tuesday + wednesday + thursday + friday + saturday;
};

const handlePost = _ => {
	const daysOfWeekValidator = getDaysOfWeek();
	if (daysOfWeekValidator === '') {
		alert('Please select which days of the week will be enabled.');
		return;
	}

	const actionType = 'action=' + document.getElementById('actionType').value;
	const instructorId = '&instructorId=' + document.getElementById('instructorId').value;
	const studentId = document.getElementById('studentId') ? '&studentId=' + document.getElementById('studentId').value : '';
	const daysOfWeek = '&daysOfWeek=' + daysOfWeekValidator;
	const startDate = '&startDate=' + document.getElementById('startDate').value;
	const startTime = '&startTime=' + document.getElementById('startTime').value;
	const endDate = '&endDate=' + document.getElementById('endDate').value;
	const endTime = '&endTime=' + document.getElementById('endTime').value;

	if (confirm('Please confirm:' + 
			'\nInstructor ID: ' + instructorId + '       Days of the Week: ' + daysOfWeek +
			'\nStudent ID: ' + studentId +
			'\nDates: ' + startDate + ' - ' + endDate +
			'\nTimes: ' + startTime + ' - ' + endTime)) {

		const xhr = new XMLHttpRequest();
		xhr.open('POST', '/');
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
		xhr.send(actionType + instructorId + studentId + daysOfWeek + startDate + startTime + endDate + endTime);
	}
};
