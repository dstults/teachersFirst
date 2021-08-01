const consoleFrame = document.getElementById('console-frame');
const consoleDragBar = document.getElementById('console-drag-bar');
const consoleCloser = document.getElementById('console-closer');
const consoleMessageList = document.getElementById('console-message-list');

dragElement(consoleDragBar, consoleFrame);

const toggleVisiblity = _ => {
	if (consoleMessageList.style.display === 'none') {
		consoleMessageList.style.display = 'block';
	} else {
		consoleMessageList.style.display = 'none';
	}
};

consoleCloser.onclick = _ => toggleVisiblity();

const getDateTimeStamp = _ => {
	const dt = new Date();
	const mo = dt.getMonth() < 10 ? '0' + dt.getMonth() : dt.getMonth();
	const d = dt.getDate() < 10 ? '0' + dt.getDate() : dt.getDate();
	const h = dt.getHours() < 10 ? '0' + dt.getHours() : dt.getHours();
	const m = dt.getMinutes() < 10 ? '0' + dt.getMinutes() : dt.getMinutes();

	return `${mo}/${d} ${h}:${m}`;
};

const addMessage = (text) => {
	if (!text) return;
	
	if (consoleMessageList.style.display === 'none') consoleMessageList.style.display = 'block';
	
	const newLine = document.createElement('li');
	newLine.innerText = getDateTimeStamp() + ' - ' + text;
	consoleMessageList.appendChild(newLine);

	consoleMessageList.scrollTop = consoleMessageList.scrollHeight;
	setTimeout(_ => newLine.style.animation = 'none', 1000);
};
