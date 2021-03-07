<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="login">
	<#if userId lte 0>
	<form method="post" action="/">
		<input type="hidden" name="action" value="log_in">
		<label for="name">Name:</label>
		<input type="text" name="name" value="${name}" placeholder="Enter your username"><br><br>

		<label for="password">Password:</label>
		<input type="password" name="password" placeholder="Enter password"><br><br>
		
		<input type="submit" value="Login">
	</form>
	<#else>
		<p>You're already logged in! Click <a href="/logout" style="color: blue;">here</a> to <a href="/logout" style="color: blue;">log out</a>.</p>
	</#if>
</div>
</body>