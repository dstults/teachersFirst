<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="register">
	<#if userId lte 0>
		<form method="post" action="/">
			<input type="hidden" name="action" value="register_new_member">

			<label for="full_name">Full Name: <span class="required">*</span></label>
			<input type="text" name="firstName" class="field-divided" placeholder="First name"/> 
			<input type="text" name="lastName" class="field-divided" placeholder="Last name"/>
			<br><br>

			<label for="username">Username: <span class="required">*</span></label>
			<input type="text" name="loginName">
			<br><br>

			<label for="password">Password: <span class="required">*</span></label>
			<input type="password" name="password">
			<br><br>

			<label for="confirm_password">Confirm Password: <span class="required">*</span></label>
			<input type="password" name="confirm_password">
			<br><br> 

			<label for="age">Age: </label>
			<input type="text" name="age">
			<br><br>

			<label for="gender" class="gender">Gender:	</label>
			<input type="radio" name="gender" value="Male"><label for="Male" class="radio">Male</label>
			<input type="radio" name="gender" value="Female"><label for="Female" class="radio">Female</label>
			<input type="radio" name="gender" value="Other" checked><label for="Other" class="radio">Other</label>
			<br><br>

			<input type="submit" value="Register">
		</form>
	<#else>
		<p>You're already logged in! Click <a href="/logout" style="color: blue;">here</a> to <a href="/logout" style="color: blue;">log out</a>.</p>
	</#if>
</div>
</body>