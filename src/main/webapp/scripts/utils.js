
const sendPostFetch = async data => {
	try {
		const response = await fetch('/', {
			method: 'POST',
			cache: 'no-cache',
			body: data
		});

		if (!response.ok) throw new Error('Post failed, status: [' + response.status + ']');
		
		return await response.json();
	} catch (err) {
		addError(err.message);
		return undefined;
	}
};
