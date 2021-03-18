<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

	<#if userId lte 0>
	<form method="post" action="/">
		<input type="hidden" name="action" value="register_new_member">
		<label for="name">Name:<input type="text" name="name"></label>
		<br><br>
		<label for="password">Password:<input type="text" name="password"></label>
		<br><br>
		<label for="confirm_password">Confirm Password:<input type="text" name="confirm_password"></label>
		<br><br>
		<label for="age">Age: <input type="text" name="age"></label>
		<br><br>
		<label for="gender">Gender:
			<label for="Male">Male<input type="radio" name="gender" value="Male"></label>
			<label for="Female">Female<input type="radio" name="gender" value="Female"></label>
			<label for="Unset">Unset<input type="radio" name="gender" value="Unset" checked></label>
		</label>
		<br><br>
		<label for="color">Favorite Color:<input type="text" name="color"></label>
		<br><br>
		<label for="food">Favorite Food:<input type="text" name="food"></label>
		<br><br>
		<input type="submit">
	</form>
	<#else>
	<p>You're already logged in! Click <a href="/logout" style="color: blue;">here</a> to <a href="/logout" style="color: blue;">log out</a>.</p>
	</#if>

</body>