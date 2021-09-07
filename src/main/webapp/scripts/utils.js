
const sendPost = async data => {

	try {
		const response = await fetch('/', {
			method: 'POST',
			cache: 'no-cache',
			body: data
		});

		if (!response.ok) throw new Error('Post failed: [' + response.status + ']');
	
		addMessage(await response.json());
	} catch (err) {
		addError(err.message);
	}
	
};
