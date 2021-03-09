<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>

	<table>
		<tr>
			<th>Rec ID</th>
			<th>Username</th>
			<#if isAdmin><th>Birthdate</th><#else><th>Age</th></#if>
			<th>Gender</th>
			<th>Contact</th>
			<#if isAdmin || isInstructor><th>Notes</th></#if>
		</tr>
		<#list members as member>
			<tr>
				<td>${member.recID}</td>
				<td>${member.displayName}</td>
				<#if isAdmin>
				<td>${member.birthdate}</td>
				<#else>
					<#if member.age > 130><td>-</td><#else><td>${member.age}</td></#if>
				</#if>
				<td>${member.gender}</td>
				<td>${member.phone1}<br>${member.email}</td>
				<#if isAdmin || isInstructor><td>${member.teacherNotes}</td></#if>
			</tr>
		</#list>
	</table>

</#if>

</body>
</html>