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
			<td>${service.name}</td>
			<td>${service.description}</td>
			<td>${service.teachers}</td>
		</tr>
	</#list>
</table>
</body>