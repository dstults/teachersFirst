
const sendPostFetch = async data => {
	try {
		const response = await fetch('/', {
			method: 'POST',
			cache: 'no-cache',
			body: data
		});

		if (!response.ok) throw new Error('Post failed, status code: [' + response.status + ']');
		
		return await response.json();
	} catch (err) {
		return { message: err.message, success: false };
	}
};
