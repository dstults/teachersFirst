
const parseQuery = (queryString) => {
	if (typeof queryString !== 'string') return {};
	if (queryString.charAt(0) === '?') queryString = queryString.substring(1);
	if (queryString.length === 0) return {};
	
	const s = queryString.split('&');
	const sLength = s.length;
	const queryObject = {}
	let bit, first, second;
	for(let i = 0; i < sLength; i++) {
		bit = s[i].split('='); //TODO: Switch to indexOf first '='
		first = decodeURIComponent(bit[0]);
		if (first.length == 0) continue;
		second = decodeURIComponent(bit[1]);
		if (typeof queryObject[first] === 'undefined') {
			queryObject[first] = second;
		} else if (queryObject[first] instanceof Array) {
			queryObject[first].push(second);
		} else {
			queryObject[first] = [queryObject[first], second];
		}
	}
	return queryObject;
};

let windowQuery = parseQuery(window.location.search);
let memberId = windowQuery.memberId ? windowQuery.memberId : null;
// These vars are defined in the dynamic page-load script:
//let operatorId = ###;
//let memberId = ###;
let memberData;
const memberName = _ => memberData;
const memberIsAdmin = _ => memberData.isAdmin;
const memberIsInstructor = _ => memberData.isInstructor;
const memberIsStudent = _ => memberData.isStudent;
const credits = _ => memberData.credits;
const memberPhone1 = _ => memberData.phone1;
const memberPhone2 = _ => memberData.phone2;
const memberEmail = _ => memberData.email;
const introText = _ => memberData.selfIntroduction;
const notesText = _ => memberData.instructorNotes;

const populateData = async _ => {
	//addMessage('Fetching profile data for user [' + memberId + '].');
	try {
		const memberIdString = memberId ? 'memberId=' + memberId + '&' : '';
		const response = await fetch('https://funteachers.org/profile?' + memberIdString + 'json');
		if (response.ok) {
			const json = await response.json();
			memberData = json;
			console.log(memberData);
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

const creditsBox = document.getElementById('credits');
const memberIdBox = document.getElementById('member-id');
const loginNameBox = document.getElementById('login-name');
const genderBox = document.getElementById('gender');
const birthdateBox = document.getElementById('birthdate');
const ageBox = document.getElementById('age');
const phone1Box = document.getElementById('phone1');
const phone2Box = document.getElementById('phone2');
const emailBox = document.getElementById('email');
const introBox = document.getElementById('introduction');
const notesBox = document.getElementById('instructor-notes');

const refreshAll = _ => {
	if (!memberData) {
		creditsBox.innerHTML = '';
		memberIdBox.innerHTML = '';
		loginNameBox.innerHTML = '';
		genderBox.innerHTML = '';
		birthdateBox.innerHTML = '';
		ageBox.innerHTML = '';
		phone1Box.innerText = '';
		phone2Box.innerText = '';
		emailBox.innerText = '';
		introBox.innerText = '';
		notesBox.innerText = '';
	} else {
		creditsBox.innerHTML = 'Credit-Hours:&nbsp;&nbsp;' + memberData.credits + '<img class="right-float-img-button" src="/images/edit-box.svg" onclick="editCredits();">';
		memberIdBox.innerHTML = memberData.id;
		loginNameBox.innerHTML = memberData.loginName;
		genderBox.innerHTML = memberData.gender;
		birthdateBox.innerHTML = memberData.birthdate;
		ageBox.innerHTML = memberData.age;
		phone1Box.innerText = memberData.phone1;
		phone2Box.innerText = memberData.phone2;
		emailBox.innerText = memberData.email;
		introBox.innerText = memberData.selfIntroduction;
		notesBox.innerText = memberData.instructorNotes;
	}
};

/*
const sendPostData = async (varName, varValue) => {
	const data = new URLSearchParams();
	data.append('action', 'update_member');
	data.append('memberId', memberId);
	data.append(varName, varValue);

	let jsonReply;
	try {
		const response = await fetch('/', {
			method: 'POST',
			cache: 'no-cache',
			body: data
		});

		if (response.ok) {
			return await response.json();
		} else {
			throw new Error('Status code [' + response.status + ']: ' + response.statusText);
		}
	} catch (err) {
		addError(err.message);
	}

	return undefined;
};

// Set up credits
const editCredits = async _ => {
	const warningMsg = memberIsStudent ? '' : '\n\nWARNING: USER IS NOT A STUDENT. THIS WOULD NOT MAKE SENSE.';
	if (!confirm(memberName + ' currently has ' + credits + ' credits, would you like to update?' + warningMsg)) {
		//alert('Operation cancelled.');
		return;
	}
	const input = prompt('Enter the updated number of credits you think user ' + memberName + ' should have:', credits);
	const parsed = parseFloat(input);
	if (isNaN(parsed)) {
		alert('Could not understand your input. Operation cancelled.');
		return;
	}
	const difference = parsed - credits;
	if (difference === 0) {
		alert('Target value same as original value. Operation cancelled.');
		return;
	}
	const increaseDecrease = difference > 0 ? 'Increase' : 'Decrease';
	if (!confirm(increaseDecrease + ' ' + memberName + '\'s credits by ' + difference + ' to ' + parsed + ', is this correct?\n\nOperator ID: [ ' + operatorId + ' ]\nDate-Time: ' + new Date())) {
		//alert('Operation cancelled.');
		return;
	}
	let proposedCredits = parsed;
	creditsBox.innerHTML = 'Updating ...';
	
	const response = await sendPostData('credits', proposedCredits);

	if (response.message.includes('Success!')) {
		credits = proposedCredits;
	} else {
		alert(response.message);
	}
	refreshAll();
};

const getFormattedString = (unformattedString) => !unformattedString ? '(unset)' : unformattedString;
const getStringPromptChain = async (dataType, elementBox, postVarName, initialValue, maxLength) => {
	//if (!confirm('Change ' + memberName + '\'s ' + dataType + '?\n\nCurrently: ' + getFormattedString(initialValue))) {
	//	return null;
	//}
	const regEx = new RegExp(/^[\+\-\=\@\.\'\"\ \:\!\?\,\:\;\_a-zA-Z\d]+$/);
	const input = prompt('Enter new ' + dataType + ' for ' + memberName + ':\n\nMax Length: ' + maxLength + ' chars', initialValue);
	if (!input) {
		//alert('Operation cancelled!');
		return null;
	}
	const inputTrimmed = input.trim();
	if (!inputTrimmed) {
		//alert('Operation cancelled!');
		return null;
	}
	const parseResult = regEx.exec(inputTrimmed);
	const parsed = inputTrimmed.length === 0 || (parseResult !== null && parseResult.length > 0) ? parseResult[0] : null;
	if (parsed === null) {
		alert('Could not understand user input: [ ' + input + ' ]');
		return null;
	}

	// This is the final filtering process
	const shortenedValue = parsed.length <= maxLength ? parsed : parsed.substr(0, maxLength - 1);
	elementBox.innerText = 'Updating ...';

	const response = await sendPostData(postVarName, shortenedValue);
	if (!response.message.includes('Success!')) {
		addError(response.message);
		return null;
	}

	return shortenedValue;
}
const sharedReportBack = (fieldName, newText) => {
	addMessage(fieldName + ' updated to: [ ' + newText + ' ]');
};

const editPhone1 = async _ => {
	const updatedValue = await getStringPromptChain('phone number (#1)', phone1Box, 'phone1', memberPhone1, 20);
	if (updatedValue === null) return; // alert message handled by shared function
	memberPhone1 = updatedValue;
	phone1Box.innerText = memberPhone1;
	sharedReportBack('Phone number (#1)', memberPhone1);
};

const editPhone2 = async _ => {
	const updatedValue = await getStringPromptChain('phone number (#2)', phone2Box, 'phone2', memberPhone2, 20);
	if (updatedValue === null) return; // alert message handled by shared function
	memberPhone2 = updatedValue;
	sharedReportBack('Phone number (#2)', memberPhone2);
};

const editEmail = async _ => {
	const updatedValue = await getStringPromptChain('email', emailBox, 'email', memberEmail, 50);
	if (updatedValue === null) return; // alert message handled by shared function
	memberEmail = updatedValue;
	sharedReportBack('Email', memberEmail);
};

const editIntro = async _ => {
	const updatedValue = await getStringPromptChain('self-introduction', introBox, 'selfIntroduction', introText, 400);
	if (updatedValue === null) return; // alert message handled by shared function
	introText = updatedValue;
	sharedReportBack('Self-introduction', introText);
};

const editNotes = async _ => {
	const updatedValue = await getStringPromptChain('instructor notes', notesBox, 'instructorNotes', notesText, 1000);
	if (updatedValue === null) return; // alert message handled by shared function
	notesText = updatedValue;
	sharedReportBack('Instructor notes', notesText);
};
*/