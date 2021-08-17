let allServices = null;
const serviceHolder = document.getElementById('services-content');

const getServices = async _ => {
	//addMessage('Fetching service data.');
	try {
		const response = await fetch('/custom/services.html');
		if (response.ok) {
			document.temp = response;
			const content = await response.text();
			serviceHolder.innerHTML = content;
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
getServices();
