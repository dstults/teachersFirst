
const refreshButtons = _ => {
	if (memberData) {
		const deleteButton = document.getElementById('delete-member');
		if (memberData.isDeleted) {
			deleteButton.innerText = 'Undelete';
			deleteButton.classList.remove('red');
			deleteButton.classList.add('green');
			deleteButton.href = 'javascript:undeleteMember()';
		} else {
			deleteButton.innerText = 'Delete\nMember';
			deleteButton.classList.remove('green');
			deleteButton.classList.add('red');
			deleteButton.href = 'javascript:deleteMember()';
		}
	}
}

const addCredits = async _ => {
	console.debug('TODO');
};

const subtractCredits = async _ => {
	console.debug('TODO');
};

const toggleStudent = async _ => {
	console.debug('TODO');
};

const toggleInstructor = async _ => {
	console.debug('TODO');
};

const toggleAdmin = async _ => {
	console.debug('TODO');
};

const deleteMember = async _ => {
	if (!memberData) {
		console.error('No user loaded, cannot delete.');
		return;
	}

	//console.log('Attempting to delete user...');

	const data = new URLSearchParams();
	data.append('action', 'delete_member');
	data.append('memberID', memberData.id);

	const response = await sendPostFetch(data);
	console.log(response);
	if (response.success) {
		memberData.isDeleted = true;
		addMessage('User successfully undeleted: ' + response.message);
		refreshButtons();
	} else {
		addMessage('Failed to undelete user: ' + response.message);
	}
};

const undeleteMember = async _ => {
	if (!memberData) {
		console.error('No user loaded, cannot undelete.');
		return;
	}

	console.log('Attempting to undelete user...');

	const data = new URLSearchParams();
	data.append('action', 'undelete_member');
	data.append('memberID', memberData.id);

	const response = await sendPostFetch(data);
	console.log(response);
	if (response.success) {
		memberData.isDeleted = false;
		addMessage('User successfully undeleted: ' + response.message);
		refreshButtons();
	} else {
		addMessage('Failed to undelete user: ' + response.message);
	}
};
