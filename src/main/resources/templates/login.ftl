<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

	<#if userId lte 0>
	<form method="post" action="/">
		<input type="hidden" name="action" value="log_in">
		<label for="name">Name:</label>
		<input type="text" name="name" value="${name}"><br><br>
		<label for="password">Password:</label>
		<input type="text" name="password"><br><br>
		<input type="submit">
	</form>
	<#else>
	<p>You're already logged in! Click <a href="/logout" style="color: blue;">here</a> to <a href="/logout" style="color: blue;">log out</a>.</p>
	</#if>

</body>