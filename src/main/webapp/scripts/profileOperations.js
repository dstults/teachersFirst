
window.onload = _ => {
	populateData();
	tryReplaceProfilePicture();
}

const parseQuery = (queryString) => {
	if (typeof queryString !== 'string') return {};
	if (queryString.charAt(0) === '?') queryString = queryString.substring(1);
	if (queryString.length === 0) return {};

	const s = queryString.split('&');
	const sLength = s.length;
	const queryObject = {}
	let bit, first, second;
	for(let i = 0; i < sLength; i++) {
		bit = s[i].split('='); //TODO: Switch to indexOf for first equals sign
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
let memberId = windowQuery.memberId ? parseInt(windowQuery.memberId) : userId;
let memberData;
const isSelf = _ => userId === memberId;

const populateData = async _ => {
	try {
		const memberIdString = memberId ? 'memberId=' + memberId + '&' : '';
		const response = await fetch('/profile?' + memberIdString + 'json');

		if (!response.ok) throw new Error('Status [' + response.status + ']' + (response.statusText ? ': [' + response.statusText + ']' : ''));

		const json = await response.json();

		if (json.message) throw new Error(json.message);

		memberData = json;
	} catch (err) {
		if (typeof addError === 'function') {
			addError(err.message);
		} else {
			console.error(err.message);
		}
	}
	refreshAll();
};

const tryReplaceProfilePicture = async _ => {
	if (!memberId) return; // obtained during page load process
	
	try {
		
		// TODO: Needs server-side support to avoid error spam.
		//       Can be addressed in issue to "make secure".
		
		// TRY ".png"
		let imagePath = '/custom/profiles/u' + memberId + '/profile.png';
		const response1 = await fetch(imagePath);
		if (response1.ok) {
			// Will automatically refresh with cached data
			profilePicture.src = imagePath;
			return;
		}
		
		// TRY ".jpg"
		imagePath = '/custom/profiles/u' + memberId + '/profile.jpg';
		const response2 = await fetch(imagePath);
		if (response2.ok) {
			profilePicture.src = imagePath;
			return;
		}
		
		// TRY ".gif"
		imagePath = '/custom/profiles/u' + memberId + '/profile.gif';
		const response3 = await fetch(imagePath);
		if (response3.ok) {
			profilePicture.src = imagePath;
			return;
		}

		console.log('User does not exist or does not have a profile image.');
	} catch (err) {
		// do nothing
	}
};

const profilePicture = document.getElementById('profile-picture');
const displayNameBox = document.getElementById('display-name');

const creditsRow = document.getElementById('credits-row');
const creditsBox = document.getElementById('credits');
const creditsButton = document.getElementById('credits-button');

const memberIdRow = document.getElementById('member-id-row');
const memberIdBox = document.getElementById('member-id');

const loginNameRow = document.getElementById('login-name-row');
const loginNameBox = document.getElementById('login-name');

const genderRow = document.getElementById('gender-row');
const genderBox = document.getElementById('gender');
const genderButton = document.getElementById('gender-button');

const birthdateRow = document.getElementById('birthdate-row');
const birthdateBox = document.getElementById('birthdate');
const birthdateButton = document.getElementById('birthdate-button');

const ageRow = document.getElementById('age-row');
const ageBox = document.getElementById('age');

const phone1Row = document.getElementById('phone1-row');
const phone1Box = document.getElementById('phone1');
const phone1Button = document.getElementById('phone1-button');

const phone2Row = document.getElementById('phone2-row');
const phone2Box = document.getElementById('phone2');
const phone2Button = document.getElementById('phone2-button');

const emailRow = document.getElementById('email-row');
const emailBox = document.getElementById('email');
const emailButton = document.getElementById('email-button');

const introRow = document.getElementById('introduction-row');
const introBox = document.getElementById('introduction');
const introButton = document.getElementById('introduction-button');

const notesRow = document.getElementById('instructor-notes-row');
const notesBox = document.getElementById('instructor-notes');
const notesButton = document.getElementById('instructor-notes-button');

const refreshAll = _ => {
	if (!memberData) {
		// visibilities
		creditsRow.style.display = 'none';
		// buttons
		creditsButton.style.display = 'none';
		genderButton.style.display = 'none';
		birthdateButton.style.display = 'none';
		phone1Button.style.display = 'none';
		phone2Button.style.display = 'none';
		emailButton.style.display = 'none';
		introButton.style.display = 'none';
		notesButton.style.display = 'none';
		// values
		displayNameBox.innerText = '';
		creditsBox.innerHTML = '';
		memberIdBox.innerText = '';
		loginNameBox.innerText = '';
		genderBox.innerText = '';
		birthdateBox.innerText = '';
		ageBox.innerText = '';
		phone1Box.innerText = '';
		phone2Box.innerText = '';
		emailBox.innerText = '';
		introBox.innerText = '';
		notesBox.innerText = '';
	} else {
		if (isAdmin || isInstructor || isSelf()) {
			// visibilities
			creditsRow.style.display = 'block';
			loginNameRow.style.display = 'grid';
			birthdateRow.style.display = 'grid';
			phone1Row.style.display = 'grid';
			phone2Row.style.display = 'grid';
			emailRow.style.display = 'grid';
			// buttons
			genderButton.style.display = 'block';
			introButton.style.display = 'block';
			phone1Button.style.display = 'block';
			phone2Button.style.display = 'block';
			emailButton.style.display = 'block';
		} else {
			// visibilities
			creditsRow.style.display = 'none';
			loginNameRow.style.display = 'none';
			birthdateRow.style.display = 'none';
			phone1Row.style.display = 'none';
			phone2Row.style.display = 'none';
			emailRow.style.display = 'none';		
			// buttons
			genderButton.style.display = 'none';
			birthdateButton.style.display = 'none';
			phone1Button.style.display = 'none';
			phone2Button.style.display = 'none';
			emailButton.style.display = 'none';
			introButton.style.display = 'none';
		}
		if (isAdmin || isInstructor) {
			notesRow.style.display = 'table-row';
			creditsButton.style.display = 'block';
			notesButton.style.display = 'block';
		} else {
			notesRow.style.display = 'none';
			creditsButton.style.display = 'none';
			notesButton.style.display = 'none';
		}
		// values
		displayNameBox.innerText = memberData.displayName;
		creditsBox.innerText = memberData.credits;
		memberIdBox.innerText = memberData.id;
		loginNameBox.innerText = memberData.loginName ? memberData.loginName : '-';
		genderBox.innerText = memberData.gender ? memberData.gender : '-';
		birthdateBox.innerText = memberData.birthdate ? memberData.birthdate : '-';
		ageBox.innerText = memberData.ageClass + (memberData.age ? ' (' + memberData.age + ')' : '');
		phone1Box.innerText = memberData.phone1 ? memberData.phone1 : '-';
		phone2Box.innerText = memberData.phone2 ? memberData.phone2 : '-';
		emailBox.innerText = memberData.email ? memberData.email : '-';
		introBox.innerText = memberData.selfIntroduction;
		notesBox.innerText = memberData.instructorNotes;
	}
};

const sendPostData = async (varName, varValue) => {
	const data = new URLSearchParams();
	data.append('action', 'update_member');
	data.append('memberId', memberId);
	data.append(varName, encodeURIComponent(varValue));

	return sendPostFetch(data);
};

// Set up credits
const editCredits = async _ => {
	if (!isAdmin && !isInstructor) return;
	if (!memberData) return;

	const warningMsg = memberData.isStudent ? '' : '\n\nWARNING: USER IS NOT A STUDENT. THIS WOULD NOT MAKE SENSE.';
	if (!confirm(memberData.displayName + ' currently has ' + memberData.credits + ' credits, would you like to update?' + warningMsg)) {
		//alert('Operation cancelled.');
		return;
	}
	const input = prompt('Enter the updated number of credits you think user ' + memberData.displayName + ' should have:', memberData.credits);
	if (input === null) {
		//alert('Operation cancelled.');
		return;
	}
	const parsed = parseFloat(input);
	if (isNaN(parsed)) {
		alert('Could not understand your input. Operation cancelled.');
		return;
	}
	const difference = parsed - memberData.credits;
	if (difference === 0) {
		alert('Target value same as original value. Operation cancelled.');
		return;
	}
	const increaseDecrease = difference > 0 ? 'Increase' : 'Decrease';
	if (!confirm(increaseDecrease + ' ' + memberData.displayName + '\'s credits by ' + difference + ' to ' + parsed + ', is this correct?\n\nOperator ID: [ ' + userId + ' ]\nDate-Time: ' + new Date())) {
		//alert('Operation cancelled.');
		return;
	}
	let proposedCredits = parsed;	
	
	const response = await sendPostData('credits', proposedCredits);
	
	if (!response.success) addError(response.message);

	populateData();
};

const getFormattedString = (unformattedString) => !unformattedString ? '(unset)' : unformattedString;
const getStringPromptChain = async (dataType, postVarName, initialValue, maxLength) => {
	if (!isAdmin && !isInstructor && !isSelf()) return;
	if (!memberData) return;

	const input = prompt('Enter new ' + dataType + ' for ' + memberData.displayName + ':\n\nMax Length: ' + maxLength + ' chars', initialValue);
	if (input === null) {
		//alert('Operation cancelled!');
		return null;
	}
	const inputTrimmed = input.trim();
	const regEx = new RegExp(/^[\+\-\=\@\.\'\"\ \:\!\?\,\:\;\_a-zA-Z\d]*$/);
	const parseResult = regEx.exec(inputTrimmed);
	const parsed = parseResult !== null ? parseResult[0] : null;
	if (parsed === null) {
		alert('Could not understand user input: [ ' + input + ' ]');
		return null;
	}

	// This is the final filtering process
	const shortenedValue = parsed.length <= maxLength ? parsed : parsed.substr(0, maxLength - 1);

	const response = await sendPostData(postVarName, shortenedValue);
	if (!response.success) {
		addError(response.message);
		return null;
	}

	return shortenedValue;
}
const sharedReportBack = (fieldName, newText) => {
	addMessage(fieldName + ' updated to: [ ' + newText + ' ]');
	populateData();
};

const editPhone1 = async _ => {
	const updatedValue = await getStringPromptChain('phone number (#1)', 'phone1', memberData.phone1, 20);
	if (updatedValue === null) return;
	sharedReportBack('Phone number (#1)', updatedValue);
};

const editPhone2 = async _ => {
	const updatedValue = await getStringPromptChain('phone number (#2)', 'phone2', memberData.phone2, 20);
	if (updatedValue === null) return;
	sharedReportBack('Phone number (#2)', updatedValue);
};

const editEmail = async _ => {
	const updatedValue = await getStringPromptChain('email', 'email', memberData.email, 40);
	if (updatedValue === null) return;
	sharedReportBack('Email', updatedValue);
};

const editIntro = async _ => {
	const updatedValue = await getStringPromptChain('self-introduction', 'selfIntroduction', memberData.selfIntroduction, 800);
	if (updatedValue === null) return;
	sharedReportBack('Self-introduction', updatedValue);
};

const editNotes = async _ => {
	const updatedValue = await getStringPromptChain('instructor notes', 'instructorNotes', memberData.instructorNotes, 800);
	if (updatedValue === null) return;
	sharedReportBack('Instructor notes', updatedValue);
};
