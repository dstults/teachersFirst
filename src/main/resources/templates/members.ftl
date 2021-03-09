<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<table>
	<tr>
		<th>Rec ID</th><th>Username</th><th>Teacher Notes</th><th>Birthdate</th><th>Gender</th><th>Phone</th><th>Email</th>
	</tr>
	<#list members as member>
		<tr>
			<td>${member.recID}</td>
			<td>${member.loginName}</td>
			<td>${member.teacherNotes}</td>
			<td>${member.birthdate}</td>
			<td>${member.gender}</td>
			<td>${member.phone1}</td>
			<td>${member.email}</td>
		</tr>
	</#list>
</table>
</body>