<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>
	<div class="page-content-750-1000">
		<table class="profile-table">
		<tr><td style="width: 35%;"><img class="profile-image" src="/images/profileNeutral.png"></td>
			<td>
				<div class="profile-basic-stats">
					<div class="h1-like" id="display-name"></div>
					<div style="background-color: var(--primaryHighlight);" class="h3-like data-view" id="credits-row">Credit-Hours:&nbsp;&nbsp;<span id="credits">##</span><img id="credits-button" class="right-float-img-button" src="/images/edit-box.svg" onclick="editCredits();"></div>
					<div class="profile-stat-row" id="member-id-row">
						<div class="bold-left">Member ID:</div><div class="data-view" id="member-id"></div><div></div>
					</div>
					<div class="profile-stat-row" id="login-name-row">
						<div class="bold-left">Login Name:</div><div class="data-view" id="login-name"></div><div></div>
					</div>
					<div class="profile-stat-row" id="gender-row">
						<div class="bold-left">Gender:</div><div class="data-view" id="gender"></div><img id="gender-button" src="/images/edit-box.svg" class="disabled" title="Cannot update gender here, talk to your system administrator.">
					</div>
					<div class="profile-stat-row" id="birthdate-row">
						<div class="bold-left">Birthdate:</div><div class="data-view" id="birthdate"></div><img id="birthdate-button" src="/images/edit-box.svg" class="disabled" title="Cannot update birthdate here, talk to your system administrator.">
					</div>
					<div class="profile-stat-row" id="age-row">
						<div class="bold-left">Age:</div><div class="data-view" id="age"></div><div></div>
					</div>
					<div class="profile-stat-row" id="phone1-row">
						<div class="bold-left">Phone 1:</div><div class="data-view size1p5h2" id="phone1"></div><img id="phone1-button" src="/images/edit-box.svg" onclick="editPhone1();">
					</div>
					<div class="profile-stat-row" id="phone2-row">
						<div class="bold-left">Phone 2:</div><div class="data-view size1p5h2" id="phone2"></div><img id="phone2-button" src="/images/edit-box.svg" onclick="editPhone2();">
					</div>
					<div class="profile-stat-row" id="email-row">
						<div class="bold-left">E-Mail:</div><div class="data-view size1p4h2" id="email"></div><img id="email-button" src="/images/edit-box.svg" onclick="editEmail();">
					</div>
				</div>
			</td>
		</tr>
		<tr id="introduction-row">
			<td colspan=2 class="extra-side-padding">
				<img id="introduction-button" class="right-float-img-button" src="/images/edit-box.svg" onclick="editIntro();">
				<p class="bold-left">Introduction:</p>
				<p class="normal-paragraph data-view italic" id="introduction"></p>
			</td>
		</tr>
		<tr id="instructor-notes-row">
			<td colspan=2 class="extra-side-padding">
				<img id="instructor-notes-button" class="right-float-img-button" src="/images/edit-box.svg" onclick="editNotes();">
				<p class="bold-left">Instructor Notes:</p>
				<p class="normal-paragraph data-view italic" id="instructor-notes"></p>
			</td>
		</tr>
		</table>
	</div>
</#if>

</body>
<#if userId gt 0>
<script>
	const userId = ${userId?c};
	const isAdmin = ${isAdmin?c};
	const isInstructor = ${isInstructor?c};
	const isStudent = ${isStudent?c};
</script>
<script src="/scripts/profileOperations.js"></script>
</#if>
</html>