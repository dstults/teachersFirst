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

	<div class="profile-page">
		<#if isAdmin><p>Record ID: ${member.recID}</p></#if>
		<p>Username: ${member.loginName}</p>
		<p>Your name: ${member.displayName}</p>
		<#if isAdmin><p>${member.birthdate}</p></#if>
		<p>Gender: ${member.gender}</p>
		<p>Teacher notes: ${member.teacherNotes}</p>
		<p>Phone number: ${member.phone1}</p>
		<p>Second phone number: ${member.phone2}</p>
		<p>Email: ${member.email}</p>
		<p><a href="#edit_profile" style="color: blue;">Edit profile info</a></p>
	</div>

</#if>

</body>
</html>