
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
