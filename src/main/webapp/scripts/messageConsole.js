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

const writeMessageToConsole = (logMessage, animate) => {
	if (consoleMessageList.style.display === 'none') consoleMessageList.style.display = 'block';
	const newLine = document.createElement('li');
	newLine.innerText = logMessage;
	consoleMessageList.appendChild(newLine);
	consoleMessageList.scrollTop = consoleMessageList.scrollHeight;
	if (animate) {
		setTimeout(_ => newLine.style.animation = 'none', 1000);
	} else {
		newLine.style.animation = 'none';
	}
};

const addMessage = (text) => {
	if (!text) return;
	
	const timestamp = getDateTimeStamp();
	const logMessage = timestamp + ' - ' + text;
	persistentLog.push(logMessage);

	writeMessageToConsole(logMessage, true);
};

const addError = (text) => {
	if (!text) return;

	addMessage('ERROR: ' + text);
	console.error(text);
};

// Persistent logging between pages:

const persistentLog = sessionStorage.getItem('persistentLog') ? JSON.parse(sessionStorage.getItem('persistentLog')) : [];

const relogMessages = _ => {

	const welcomeUser = (persistentLog.length === 0);
	if (!welcomeUser) {
		// Re-populate past log messages without animating
		for(const logMessage of persistentLog) {
			writeMessageToConsole(logMessage, false);
		}
	}

	// Re-hide if it was shown despite having been minimized before
	const consoleVisible = consoleMessageList.style.display !== 'none';
	const consolePreviouslyVisible = sessionStorage.getItem('consoleVisible') === 'true';
	if (consoleVisible !== consolePreviouslyVisible) toggleVisiblity();

	// Move console to wherever you had it
	const consoleLocation = sessionStorage.getItem('consoleLocation') ? JSON.parse(sessionStorage.getItem('consoleLocation')) : {x: consoleFrame.style.left, y: consoleFrame.style.top};
	consoleFrame.style.left = consoleLocation.x;
	consoleFrame.style.top = consoleLocation.y;

	if (welcomeUser) addMessage("Console loaded.");
};
relogMessages();

// Make it save to session when leaving the current window
window.onbeforeunload = _ => {
	const maxStoredLines = 20;
	const storedLog = persistentLog.splice(persistentLog.length > maxStoredLines ? persistentLog.length - maxStoredLines : 0);
	sessionStorage.setItem('persistentLog', JSON.stringify(storedLog));
	sessionStorage.setItem('consoleVisible', consoleMessageList.style.display !== 'none');
	sessionStorage.setItem('consoleLocation', JSON.stringify({x: consoleFrame.style.left, y: consoleFrame.style.top}));
};

const clearResetLog = _ => {
	sessionStorage.removeItem('persistentLog');
	sessionStorage.removeItem('consoleVisible');
	sessionStorage.removeItem('consoleLocation');
}
