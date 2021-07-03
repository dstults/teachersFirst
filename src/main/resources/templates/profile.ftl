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
		<tr><td><img src="/images/profileNeutral.png"></td>
			<td>
				<table class="profile-basic-stats">
					<tr><td colspan=2><h1>${member.displayName}</h1></tr>
					<#if isAdmin || member.isStudent><tr><td style="background-color: var(--primaryHighlight);" class="data-view" colspan=2><h3 id="credits" style="line-height: 200%;">Credit-Hours:&nbsp;&nbsp;${member.credits}</h3></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left" style="width: 33%;"><p>Member ID:</p></td><td style="width: 67%;" class="data-view">${member.recID}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Login name:</p></td><td class="data-view" id="login-name">${member.loginName}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Gender:</p></td><td class="data-view" id="gender">${member.genderWord}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Birthdate:</p></td><td class="data-view" id="birthdate">${member.birthDateView}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Age:</p></td><td class="data-view" id="age">${member.age}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Phone 1:</p></td><td class="data-view" id="phone1">${member.phone1}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Phone 2:</p></td><td class="data-view" id="phone2">${member.phone2}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>E-Mail:</p></td><td class="data-view" id="email">${member.email}</td></tr></#if>
				</table>
			</td>
		</tr>
		<tr>
			<tr><td colspan=2 class="extra-side-padding">
				<p class="bold-left">Introduction:</p>
				<p class="normal-paragraph data-view" id="introduction"><#if member.selfIntroduction?has_content>${member.selfIntroduction}<#else>No self-introduction.</#if></p>
			</td></tr>
			<#if isAdmin || isInstructor>
			<tr><td colspan=2 class="extra-side-padding">
				<p class="bold-left">Instructor Notes:</p>
				<p class="normal-paragraph data-view" id="instructor-notes"><#if member.instructorNotes?has_content>${member.instructorNotes}<#else>No instructors' comments.</#if></p>
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
	
	<#if isAdmin || isInstructor>
	// Set up credits
	const creditsBox = document.getElementById('credits');
	let credits = ${member.credits};
	creditsBox.ondblclick = (mouseEvent) => {
		if (!confirm(memberName + ' currently has ' + credits + ' credits, would you like to update?')) {
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
			alert('Operation cancelled.');
			return;
		}
		credits = parsed;
		creditsBox.innerText = 'Credit-Hours:  ' + credits;
		alert('Changes saved!\n\nNote: this is just a demo, no changes have been saved.');
	};
	</#if>

	const loginNameBox = document.getElementById('login-name');
	loginNameBox.ondblclick = _ => {
		alert('Cannot update login handle here, talk to your system administrator.');
	};

	const genderBox = document.getElementById('gender');
	genderBox.ondblclick = _ => {
		alert('Cannot update gender here, talk to your system administrator.');
	};

	const birthdateBox = document.getElementById('birthdate');
	const ageBox = document.getElementById('age');
	const bdayHandler = _ => {
		alert('Cannot update age here, talk to your system administrator.');
	};
	birthdateBox.ondblclick = bdayHandler;
	ageBox.ondblclick = bdayHandler;

	const getFormattedString = (unformattedString) => !unformattedString ? '(unset)' : unformattedString;
	const getStringPromptChain = (dataType, defaultValue, maxLength) => {
		if (!confirm('Change ' + memberName + '\'s dataType?\n\nCurrently: ' + getFormattedString(defaultValue))) {
			return null;
		}		
		const regEx = new RegExp(/^[\+\-\@\.\ \:\!\?a-zA-Z\d]+$/);
		const input = prompt('Enter new ' + dataType + ' for ' + memberName + ':\n\nMax Length: ' + maxLength + ' chars', defaultValue);
		if (!input) {
			alert('Operation cancelled!');
			return null;
		}
		const inputTrimmed = input.trim();
		if (!inputTrimmed) {
			alert('Operation cancelled!');
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
	phone1Box.ondblclick = () => {
		const parsed = getStringPromptChain('phone number (#1)', memberPhone1, 20);
		if (!parsed) return; // alert message handled by shared function
		memberPhone1 = parsed;
		phone1Box.innerText = memberPhone1;
		sharedReportBack('Phone number (#1)', memberPhone1);
	};
	const phone2Box = document.getElementById('phone2');
	let memberPhone2 = '${member.phone2}';
	phone2Box.ondblclick = () => {
		const parsed = getStringPromptChain('phone number (#2)', memberPhone2, 20);
		if (!parsed) return; // alert message handled by shared function
		memberPhone2 = parsed;
		phone2Box.innerText = memberPhone2;
		sharedReportBack('Phone number (#2)', memberPhone2);
	};
	const emailBox = document.getElementById('email');
	let memberEmail = '${member.email}';
	emailBox.ondblclick = () => {
		const parsed = getStringPromptChain('email', memberEmail, 50);
		if (!parsed) return; // alert message handled by shared function
		memberEmail = parsed;
		emailBox.innerText = memberEmail;
		sharedReportBack('Email', memberEmail);
	};

	const introBox = document.getElementById('introduction');
	let introText = '${member.selfIntroduction}';
	introBox.ondblclick = () => {
		const parsed = getStringPromptChain('self-introduction', introText, 400);
		if (!parsed) return; // alert message handled by shared function
		introText = parsed;
		introBox.innerText = introText;
		sharedReportBack('Self-introduction', introText);
	};
	const notesBox = document.getElementById('introduction');
	let notesText = '${member.instructorNotes}';
	notesBox.ondblclick = () => {
		const parsed = getStringPromptChain('instructor notes', notesText, 1000);
		if (!parsed) return; // alert message handled by shared function
		notesText = parsed;
		notesBox.innerText = notesText;
		sharedReportBack('Instructor notes', notesText);
	};
</script>
</#if></#if>
</html>