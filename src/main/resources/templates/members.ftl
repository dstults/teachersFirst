<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>

	<table class="info-list">
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Credits</th>
			<th>Gender / Age</th>
			<th>Contact</th>
			<th>Self-Introduction</th>
			<#if isAdmin || isInstructor><th>Notes</th></#if>
		</tr>
		<#list members as member>
			<tr>
				<td>${member.recID?c}</td>
				<td><a href="/profile?memberId=${member.recID?c}">${member.displayName}</a></td>
				<td><#if member.isStudent || isAdmin>${member.credits} hours<#else>-</#if></td>
				<td>${member.gender} / <#if member.age gt 130>-<#else>${member.age}</#if><#if (isAdmin || isteacher) && member.birthDateView != "unset"> ( ${member.birthDateView} )</#if></td>
				<td><#if member.phone1?has_content> - Phone: ${member.phone1} </#if><#if member.phone2?has_content> - Phone: ${member.phone2} </#if><#if member.email?has_content> - Email: ${member.email} </#if></td>
				<td>${member.selfIntroduction}</td>
				<#if isAdmin || isInstructor><td>${member.instructorNotes}</td></#if>
			</tr>
		</#list>
	</table>

</#if>

</body>
</html>