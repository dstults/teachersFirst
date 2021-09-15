
let allMembers;

const setDefaultDates = _ => {
};

// Get inputs
const instructorIdSelector = document.getElementById('instructor-id');
const startDateInput = document.getElementById('start-date');
const startTimeInput = document.getElementById('start-time');
const endDateInput = document.getElementById('end-date');
const endTimeInput = document.getElementById('end-time');

// Set defaults
const nowDate = new Date().toISOString().substr(0, 10);
startDateInput.value = nowDate;
endDateInput.value = nowDate;

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

const handlePost = async _ => {
	// Ensure user has already gotten data before attempting this
	if (!allMembers) return;

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

	const instructorId = instructorIdSelector.value;
	const startDate = startDateInput.value;
	const startTime = startTimeInput.value;
	const endDate = endDateInput.value;
	const endTime = endTimeInput.value;

	if (confirm('Please confirm:' + 
			'\nInstructor: [' + instructorId + '] ' + allMembers.find(m => m.id === parseInt(instructorId)).displayName +
			'\nDays of the Week: ' + daysOfWeek +
			'\nDates: ' + startDate + ' - ' + endDate +
			'\nTimes: ' + startTime + ' - ' + endTime)) {

		const data = new URLSearchParams();
		data.append('action', 'make_openings');
		data.append('instructorId', instructorId);
		data.append('daysOfWeek', daysOfWeek);
		data.append('startDate', startDate);
		data.append('startTime', startTime);
		data.append('endDate', endDate);
		data.append('endTime', endTime);
	
		const response = await sendPostFetch(data);
		if (response) {
			window.location.href = response.responseURL;
		}
	}

}
