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
					<#if isAdmin || member.isStudent><tr><td style="background-color: var(--primaryHighlight);" class="data-view" colspan=2><h3 style="line-height: 200%;">Credit-Hours:&nbsp;&nbsp;${member.credits}</h3></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left" style="width: 33%;"><p>Member ID:</p></td><td style="width: 67%;" class="data-view">${member.recID}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Login name:</p></td><td class="data-view">${member.loginName}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Gender:</p></td><td class="data-view">${member.genderWord}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Birthdate:</p></td><td class="data-view">${member.birthDateView}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Age:</p></td><td class="data-view">${member.age}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Phone 1:</p></td><td class="data-view">${member.phone1}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>Phone 2:</p></td><td class="data-view">${member.phone2}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left"><p>E-Mail:</p></td><td class="data-view">${member.email}</td></tr></#if>
				</table>
			</td>
		</tr>
		<tr>
			<tr><td colspan=2 class="extra-side-padding">
				<p class="bold-left">Introduction:</p>
				<p class="normal-paragraph data-view"><#if member.selfIntroduction?has_content>${member.selfIntroduction}<#else>No self-introduction.</#if></p>
			</td></tr>
			<#if isAdmin || isInstructor>
			<tr><td colspan=2 class="extra-side-padding">
				<p class="bold-left">Instructor Notes:</p>
				<p class="normal-paragraph data-view"><#if member.instructorNotes?has_content>${member.instructorNotes}<#else>No instructors' comments.</#if></p>
			</td></tr>
			</#if>
		</tr>
		</table>
		<br><br>
		<#if isAdmin || isInstructor || isSelf><p><a href="/edit_profile?memberId=${member.recID}">Edit Profile</a></p></#if>
	</div>

</#if>

</body>
</html>