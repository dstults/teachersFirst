<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#elseif !member?has_content>
	<div class="profile-page">
		<p>No user data to show.</p>
	</div>
<#else>

	<div class="fixed-width-subpage-wide">
		<table class="profile-outer">
		<tr><td><img src="/images/profileNeutral.png"></td>
			<td>
				<table class="profile-inner">
					<tr><td colspan=2><h1>${member.displayName}</h1></tr>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left" style="width: 33%;">Member ID:</td><td style="width: 67%;">${member.recID}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left">Login name:</td><td>${member.loginName}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left">Gender:</td><td>${member.genderWord}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left">Birthdate:</td><td>${member.birthDateFormatted}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left">Age:</td><td>${member.age}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left">Phone 1:</td><td>${member.phone1}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left">Phone 2:</td><td>${member.phone2}</td></tr></#if>
					<#if isAdmin || isInstructor || isSelf><tr><td class="bold-left">E-Mail:</td><td>${member.email}</td></tr></#if>
				</table>
			</td>
		</tr>
		<tr>
			<tr><td colspan=2 class="extra-side-padding">
				<p class="bold-left">Self-Introduction:</p>
				<p class="normal-paragraph">I really like studying and exercise! This gym/school is so much fun!</p>
			</td></tr>
			<#if isAdmin || isInstructor>
			<tr><td colspan=2 class="extra-side-padding">
				<p class="bold-left">Instructor notes:</p>
				<p class="normal-paragraph italic"><#if member.instructorNotes?has_content>${member.instructorNotes}<#else>No instructors' comments.</#if></p>
			</td></tr>
			</#if>
		</tr>
		</table>
		<br><br>
		<#if isAdmin || isInstructor || isSelf><p><a href="/edit_profile">Edit profile info</a></p></#if>
	</div>

</#if>

</body>
</html>