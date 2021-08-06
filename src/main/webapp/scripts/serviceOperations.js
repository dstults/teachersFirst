let allServices = null;
const serviceListTable = document.getElementById('service-list');

const populateData = async _ => {
	//addMessage('Fetching service data.');
	try {
		const response = await fetch('https://funteachers.org/services?json');
		if (response.ok) {
			const json = await response.json();
			allServices = json;
			refreshAll();
			return;
		} else {
			throw new Error('Status [' + response.status + '] ' + response.statusText);
		}
	} catch (err) {
		if (typeof addError === 'function') {
			addError(err.message);
		} else {
			console.error(err.message);
		}
	}
};
populateData();

const refreshAll = _ => {
	// Clear child elements just in case
	while (serviceListTable.firstChild) serviceListTable.removeChild(serviceListTable.firstChild);
	const tbody = document.createElement('tbody');
	serviceListTable.appendChild(tbody);

	// Draw header row
	const header = document.createElement('tr');
	header.innerHTML = '<th>Service Name</th><th>Description</th><th>Instructors</th>';
	tbody.appendChild(header);

	// Draw each loaded service row
	for (const service of allServices) {
		tbody.appendChild(getServiceRow(service));
	}
};

const getServiceRow = (service) => {
	const serviceRow = document.createElement('tr');
	const serviceName = '<td><a href="/openings">' + service.name + '</a></td>';
	const serviceDescription = '<td>' + service.description + '</td>';
	const instructorList = service.instructors.split(',')
		.map(i => '<a href="/openings?instructorName=' + i + '">' + i + '</a>')
		.join(', ');
	const serviceInstructors = '<td>' + instructorList + '</td>';
	serviceRow.innerHTML = serviceName + serviceDescription + serviceInstructors;
	return serviceRow;
};
