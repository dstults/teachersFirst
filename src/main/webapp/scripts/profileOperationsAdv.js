
const refreshButtons = _ => {
	if (memberData) {
		const deleteButton = document.getElementById('delete-member');
		if (memberData.isDeleted) {
			deleteButton.innerText = 'Undelete';
			deleteButton.classList.remove('red');
			deleteButton.classList.add('green');
		} else {
			deleteButton.innerText = 'Delete\nMember';
			deleteButton.classList.remove('green');
			deleteButton.classList.add('red');
		}
	}
}

const addCredits = async _ => {
	console.debug('TODO');
};

const subtractCredits = async _ => {
	console.debug('TODO');
};

const adminAction = async (verbCommand, verbPresent, verbPast) => {
	if (!memberData) {
		console.error(`No user loaded, cannot ${verbPresent}.`);
		return;
	}

	const data = new URLSearchParams();
	data.append('action', verbCommand + '_member');
	data.append('memberID', memberData.id);

	const response = await sendPostFetch(data);
	//console.log(response);
	if (response.success) {
		addMessage(`User successfully ${verbPast}!`);
		await populateData();
	} else {
		addMessage(`Failed to ${verbPresent} user. ${response.message}`);
	}
};

const toggleStudent = async _ => {
	if (memberData.isStudent) {
		await adminAction('unstudent', 'unmake student', 'unmade student');
	} else {
		await adminAction('student', 'make student', 'made student');
	}	
};

const toggleInstructor = async _ => {
	if (memberData.isInstructor) {
		await adminAction('uninstructor', 'unmake instructor', 'unmade instructor');
	} else {
		await adminAction('instructor', 'make instructor', 'made instructor');
	}
};

const toggleAdmin = async _ => {
	if (memberData.isAdmin) {
		await adminAction('unadmin', 'unmake admin', 'unmade admin');
	} else {
		await adminAction('admin', 'make admin', 'made admin');
	}
};

const toggleDelete = async _ => {
	if (memberData.isDeleted) {
		await adminAction('undelete', 'undelete', 'undeleted');
	} else {
		await adminAction('delete', 'delete', 'deleted');
	}
};
