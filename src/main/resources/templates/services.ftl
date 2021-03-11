<!DOCTYPE html>
<#include "head.ftl">

<body>
<#include "header.ftl">

<table>
	<tr>
		<th>Service Name</th><th>Description</th><th>Teachers</th>
	</tr>
	<#list services as service>
		<tr>
			<td><a href="/openings">${service.name}</a></td>
			<td>${service.description}</td>
			<td><a href="/openings">${service.teachers}</a></td>
		</tr>
	</#list>
</table>
</body>
</html>