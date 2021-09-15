
let allMembers;

const instructorIdSelector = document.getElementById('instructorId');

fetch('/members?json').then(response => response.json()).then(data => {
	allMembers = data;
	populateSelectors();
}).catch(err => console.error(err.message));

const populateSelectors = _ => {
	for (const member of allMembers) {
		if (!member.id || !member.displayName) continue; // Must have valid data
		if (!member.isInstructor && !member.isAdmin) continue; // Must be an instructor (or admin -- meetings!)
		if (!isAdmin && userId !== member.id) continue; // Must be an admin or self
		const memberOption = document.createElement('option');
		memberOption.value = member.id;
		memberOption.innerText = member.id + ' | ' + member.displayName;
		instructorIdSelector.appendChild(memberOption);
		if (userId && userId === member.id) {
			instructorIdSelector.value = userId;
		}
	}
	// Remove the blank option if valid options exist
	if (instructorIdSelector.childElementCount > 1) instructorIdSelector.removeChild(instructorIdSelector.firstElementChild);
};

const handlePost = () => {
	const sunday = document.getElementById('sunday').checked ? document.getElementById('sunday').value : '';
	const monday = document.getElementById('monday').checked ? document.getElementById('monday').value : '';
	const tuesday = document.getElementById('tuesday').checked ? document.getElementById('tuesday').value : '';
	const wednesday = document.getElementById('wednesday').checked ? document.getElementById('wednesday').value : '';
	const thursday = document.getElementById('thursday').checked ? document.getElementById('thursday').value : '';
	const friday = document.getElementById('friday').checked ? document.getElementById('friday').value : '';
	const saturday = document.getElementById('saturday').checked ? document.getElementById('saturday').value : '';
	const daysOfWeek = sunday + monday + tuesday + wednesday + thursday + friday + saturday; 

	if (daysOfWeek == '') {
		alert('Please select which days of the week will be enabled.');
		return;
	}

	const instructorId = document.getElementById('instructorId').value;
	const startDate = document.getElementById('startDate').value;
	const startTime = document.getElementById('startTime').value;
	const endDate = document.getElementById('endDate').value;
	const endTime = document.getElementById('endTime').value;

	if (confirm('Please confirm:' + 
			'\nInstructor ID: ' + instructorId + '   Days of the Week: ' + daysOfWeek +
			'\nDates: ' + startDate + ' - ' + endDate +
			'\nTimes: ' + startTime + ' - ' + endTime)) {

		const xhr = new XMLHttpRequest();
		xhr.open('POST', '/');
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
		xhr.send('action=make_openings&instructorId=' + instructorId + '&daysOfWeek=' + daysOfWeek + '&startDate=' + startDate + '&startTime=' + startTime + '&endDate=' + endDate + '&endTime=' + endTime);
	}
}
