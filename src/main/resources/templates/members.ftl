<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>

	<table class="info-list">
		<tr>
			<th>Rec ID</th>
			<th>Username</th>
			<#if isAdmin><th>Birthdate</th></#if>
			<th>Age</th>
			<th>Gender</th>
			<th>Contact</th>
			<#if isAdmin || isInstructor><th>Notes</th></#if>
		</tr>
		<#list members as member>
			<tr>
				<td>${member.recID?c}</td>
				<td><a href="/profile?memberId=${member.recID?c}">${member.displayName}</a></td>
				<#if isAdmin>
				<td>${member.birthdate}</td>
				</#if>
				<#if member.age gt 130><td>-</td><#else><td>${member.age}</td></#if>
				<td>${member.gender}</td>
				<td>${member.phone1}<br>${member.email}</td>
				<#if isAdmin || isInstructor><td>${member.instructorNotes}</td></#if>
			</tr>
		</#list>
	</table>

</#if>

</body>
</html>