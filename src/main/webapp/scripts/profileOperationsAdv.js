
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

	console.log('Attempting to delete user...');

	const data = new URLSearchParams();
	data.append('action', 'delete_member');
	data.append('memberID', memberData.id);

	const response = await sendPostFetch(data);
	addMessage(response);
	console.log(response);
};
