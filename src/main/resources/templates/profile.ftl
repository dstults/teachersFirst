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
					<#if isAdmin || isInstructor && member.isStudent><div style="background-color: var(--primaryHighlight);" class="full-width-grid h3-like data-view" id="credits">Credit-Hours:&nbsp;&nbsp;${member.credits?c}<img class="right-float-img-button" src="/images/edit-box.svg" onclick="editCredits();"></div></#if>
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
</script>
<script src="/scripts/profileOperations.js"></script>
</#if></#if>
</html>