<!DOCTYPE html>
<#include "head.ftl">

<body>
<#include "header.ftl">

<table class="info-list">
	<tr>
		<th>Service Name</th><th>Description</th><th>Teachers</th>
	</tr>
	<#list services as service>
	<tr>
		<td><a href="/openings">${service.name}</a></td>
		<td>${service.description}</td>
		<td>
			<#assign comma=""><#list service.instructorList as instructor>${comma}<a href="/openings?instructorName=${instructor}">${instructor}</a><#assign comma=", "></#list>
		</td>
	</tr>
	</#list>
</table>
</body>
</html>