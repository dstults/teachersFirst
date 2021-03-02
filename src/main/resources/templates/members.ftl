<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<table>
	<tr>
		<th>Record ID</th><th>Member</th><th>Age</th><th>Gender</th><th>Favorite Color</th><th>Favorite Food</th>
	</tr>
	<#list members as member>
		<tr>
			<td>${member.recID}</td>
			<td>${member.name}</td>
			<td>${member.age}</td>
			<td>${member.gender}</td>
			<td>${member.favoriteColor}</td>
			<td>${member.favoriteFood}</td>
		</tr>
	</#list>
</table>
</body>