<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

	<p>User ID: ${userId}</p>
	<p>User Name: ${userName}</p>

	<#if userId lte 0>
	<p>You're not logged in!</p>
	<#else>
	<p>
		Click here:
		<form method="post" action="/"><input type="hidden" name="action" value="log_out"><input type="submit"></form>
	</p>
	</#if>

</body>