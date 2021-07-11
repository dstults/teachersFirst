<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#elseif !member?has_content>
	<div class="profile-page-nodata">
		<p class="centered">No user data to show.</p>
	</div>
<#else>
	<div class="page-content-750-1000">
		<table class="profile-table">
		<tr><td style="width: 35%;"><img class="profile-image" src="/images/profileNeutral.png"></td>
			<td>
				<div class="profile-basic-stats">
					<div class="full-width-grid h1-like">${member.displayName}</div>
					<#if isAdmin || isInstructor && member.isStudent><div style="background-color: var(--primaryHighlight);" class="full-width-grid h3-like data-view" id="credits">Credit-Hours:&nbsp;&nbsp;${member.credits}<img class="right-float-img-button" src="/images/edit-box.svg" onclick="editCredits();"></div></#if>
					<#if isAdmin || isInstructor || isSelf><div class="bold-left">Member ID:</div><div class="data-view">${member.recID}</div><div></div></#if>
					<#if isAdmin || isInstructor || isSelf><div class="bold-left">Login name:</div><div class="data-view" id="login-name">${member.loginName}</div><div></div></#if>
					<#if isAdmin || isInstructor || isSelf><div class="bold-left">Gender:</div><div class="data-view" id="gender">${member.genderWord}</div><img src="/images/edit-box.svg" class="disabled" title="Cannot update gender here, talk to your system administrator."></#if>
					<#if isAdmin || isInstructor || isSelf><div class="bold-left">Birthdate:</div><div class="data-view" id="birthdate">${member.birthDateView}</div><img src="/images/edit-box.svg" class="disabled" title="Cannot update birthdate here, talk to your system administrator."></#if>
					<#if isAdmin || isInstructor || isSelf><div class="bold-left">Age:</div><div class="data-view" id="age">${member.age}</div><img src="/images/edit-box.svg" class="disabled" title="Cannot update birthdate here, talk to your system administrator."></#if>
					<#if isAdmin || isInstructor || isSelf><div class="bold-left">Phone 1:</div><div class="data-view size1p5h2" id="phone1">${member.phone1}</div><img src="/images/edit-box.svg" onclick="editPhone1();"></#if>
					<#if isAdmin || isInstructor || isSelf><div class="bold-left">Phone 2:</div><div class="data-view size1p5h2" id="phone2">${member.phone2}</div><img src="/images/edit-box.svg" onclick="editPhone2();"></#if>
					<#if isAdmin || isInstructor || isSelf><div class="bold-left">E-Mail:</div><div class="data-view size1p4h2" id="email">${member.email}</div><img src="/images/edit-box.svg" onclick="editEmail();"></#if>
				</div>
			</td>
		</tr>
		<tr>
			<tr><td colspan=2 class="extra-side-padding">
				<img class="right-float-img-button" src="/images/edit-box.svg" onclick="editIntro();">
				<p class="bold-left">Introduction:</p>
				<p class="normal-paragraph data-view italic" id="introduction"><#if member.selfIntroduction?has_content>${member.selfIntroduction}<#else>No self-introduction.</#if></p>
			</td></tr>
			<#if isAdmin || isInstructor>
			<tr><td colspan=2 class="extra-side-padding">
				<img class="right-float-img-button" src="/images/edit-box.svg" onclick="editNotes();">
				<p class="bold-left">Instructor Notes:</p>
				<p class="normal-paragraph data-view italic" id="instructor-notes"><#if member.instructorNotes?has_content>${member.instructorNotes}<#else>No instructors' comments.</#if></p>
			</td></tr>
			</#if>
		</tr>
		</table>
	</div>

</#if>

</body>
<#if userId gt 0><#if isAdmin || isInstructor || isSelf>
<script>
	const operatorId = ${userId};
	const memberId = ${member.recID};
	const memberName = '${member.displayName}';
	const memberIsStudent = ${member.isStudent?c};
	
	const postData = async (actionType, varName, varValue) => {
		const data = {};
		data.action = actionType;
		data[varName] = varValue;

		const response = await fetch('/', {
			method: 'POST',
			cache: 'no-cache',
			credentials: 'same-origin',
			headers: { 'Content-Type': 'application/json' },
			referrerPolicy: 'no-referrer',
			body: JSON.stringify(data)
		});

		return response;
	};

	<#if isAdmin || isInstructor && member.isStudent>
	// Set up credits
	const creditsBox = document.getElementById('credits');
	let credits = ${member.credits};
	const editCredits = _ => {
		const warningMsg = memberIsStudent ? '' : '\n\nWARNING: USER IS NOT A STUDENT. THIS WOULD NOT MAKE SENSE.';
		if (!confirm(memberName + ' currently has ' + credits + ' credits, would you like to update?' + warningMsg)) {
			//alert('Operation cancelled.');
			return;
		}
		const input = prompt('Enter the updated number of credits you think user ' + memberName + ' should have:', credits);
		const parsed = parseFloat(input);
		if (!parsed || isNaN(parsed)) {
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
		credits = parsed;
		creditsBox.innerHTML = 'Updating ...';
		
		const response = postData('update_member', 'credits', credits);
		
		// debug-use only:
		document.temp = response;

		alert(response.json());
		creditsBox.innerHTML = 'Credit-Hours:&nbsp;&nbsp;' + credits + '<img class="credits-img" src="/images/edit-box.svg" onclick="editCredits();">';
		//alert('Changes saved!\n\nNote: this is just a demo, no changes have been saved.');
	};
	</#if>

	const loginNameBox = document.getElementById('login-name');
	const genderBox = document.getElementById('gender');
	const birthdateBox = document.getElementById('birthdate');
	const ageBox = document.getElementById('age');

	const getFormattedString = (unformattedString) => !unformattedString ? '(unset)' : unformattedString;
	const getStringPromptChain = (dataType, defaultValue, maxLength) => {
		//if (!confirm('Change ' + memberName + '\'s ' + dataType + '?\n\nCurrently: ' + getFormattedString(defaultValue))) {
		//	return null;
		//}
		const regEx = new RegExp(/^[\+\-\@\.\ \:\!\?\_a-zA-Z\d]+$/);
		const input = prompt('Enter new ' + dataType + ' for ' + memberName + ':\n\nMax Length: ' + maxLength + ' chars', defaultValue);
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
		const parsed = parseResult ? parseResult[0] : null;
		if (!parsed) {
			alert('Could not understand user input: [ ' + input + ' ]');
			return null;
		}
		const shortened = parsed.length <= maxLength ? parsed : parsed.substr(0, maxLength - 1);
		return shortened;
	}
	const sharedReportBack = (fieldName, introText) => {
		alert(fieldName + ' updated to: [ ' + introText + ' ]\n\nNote: This is just a demo, no changes were made.');
	};

	const phone1Box = document.getElementById('phone1');
	let memberPhone1 = '${member.phone1}';
	const editPhone1 = _ => {
		const parsed = getStringPromptChain('phone number (#1)', memberPhone1, 20);
		if (!parsed) return; // alert message handled by shared function
		memberPhone1 = parsed;
		phone1Box.innerText = memberPhone1;
		sharedReportBack('Phone number (#1)', memberPhone1);
	};

	const phone2Box = document.getElementById('phone2');
	let memberPhone2 = '${member.phone2}';
	const editPhone2 = _ => {
		const parsed = getStringPromptChain('phone number (#2)', memberPhone2, 20);
		if (!parsed) return; // alert message handled by shared function
		memberPhone2 = parsed;
		phone2Box.innerText = memberPhone2;
		sharedReportBack('Phone number (#2)', memberPhone2);
	};

	const emailBox = document.getElementById('email');
	let memberEmail = '${member.email}';
	const editEmail = _ => {
		const parsed = getStringPromptChain('email', memberEmail, 50);
		if (!parsed) return; // alert message handled by shared function
		memberEmail = parsed;
		emailBox.innerText = memberEmail;
		sharedReportBack('Email', memberEmail);
	};

	const introBox = document.getElementById('introduction');
	let introText = '${member.selfIntroduction}';
	const editIntro = _ => {
		const parsed = getStringPromptChain('self-introduction', introText, 400);
		if (!parsed) return; // alert message handled by shared function
		introText = parsed;
		introBox.innerText = introText;
		sharedReportBack('Self-introduction', introText);
	};
	const notesBox = document.getElementById('instructor-notes');
	let notesText = '${member.instructorNotes}';
	const editNotes = _ => {
		const parsed = getStringPromptChain('instructor notes', notesText, 1000);
		if (!parsed) return; // alert message handled by shared function
		notesText = parsed;
		notesBox.innerText = notesText;
		sharedReportBack('Instructor notes', notesText);
	};
</script>
</#if></#if>
</html>