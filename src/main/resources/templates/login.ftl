<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="fixed-width-subpage">
	<#if userId lte 0>
	<form method="post" action="/">
		<input type="hidden" name="action" value="log_in">
		<label for="name">Name:</label>
		<input type="text" name="loginName" value="${loginName}" placeholder="Enter your username"><br><br>

		<label for="password">Password:</label>
		<input type="password" name="password" placeholder="Enter password"><br><br>
		
		<input type="submit" value="Login">
	</form>
	<#else>
		<p>You're already logged in!<br> <a href="/logout" style="color: blue;">Click here to log out</a>.</p>
	</#if>
</div>
</body>
</html>