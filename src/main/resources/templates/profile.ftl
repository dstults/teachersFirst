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
		<p>Record ID: ${member.recID}</p>
		<p>Username: ${member.loginName}</p>
		<p>Your name: ${member.displayName}</p>
		<p>Birthdate: ${member.birthdate}</p>
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