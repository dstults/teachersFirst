<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="fixed-width-subpage">

	<#if userId lte 0>

	<form method="post" action="/">
		<input type="hidden" name="action" value="register_new_member">

		<label for="full_name">Display Name (how others see you): <span class="required">*</span></label>
		<input type="text" name="displayName" value="${displayName}" placeholder="Jane Doe"/> 
		<br><br>

		<label for="username">Login Name (this is private): <span class="required">*</span></label>
		<input type="text" name="loginName" value="${loginName}" placeholder="jdoe93">
		<br><br>

		<label for="password">Password: <span class="required">*</span></label>
		<input type="password" name="password1">
		<br><br>

		<label for="confirm_password">Confirm Password: <span class="required">*</span></label>
		<input type="password" name="password2">
		<br><br> 

		<label for="gender" class="gender">Gender:	</label>
		<#if gender == "m">
			<input type="radio" name="gender" value="m" checked><label for="Male" class="radio">Male</label>
		<#else>
			<input type="radio" name="gender" value="m"><label for="Male" class="radio">Male</label>
		</#if>
		<#if gender == "f">
			<input type="radio" name="gender" value="f" checked><label for="Female" class="radio">Female</label>
		<#else>
			<input type="radio" name="gender" value="f"><label for="Female" class="radio">Female</label>
		</#if>
		<#if gender == "other">
			<input type="radio" name="gender" value="other" checked><label for="Other" class="radio">Other/Unspecified</label>
		<#else>
			<input type="radio" name="gender" value="other"><label for="Other" class="radio">Other/Unspecified</label>
		</#if>
		<br><br>

		<p><span class="required">*</span> Please provide at least one form of contact:</p>

		<label for="age">Phone 1: </label>
		<input type="text" name="phone1" value="${phone1}" placeholder="425-555-5555">
		<br><br>

		<label for="age">Phone 2: </label>
		<input type="text" name="phone2" value="${phone2}">
		<br><br>

		<label for="age">Email: </label>
		<input type="email" name="email" value="${email}" placeholder="jdoe@gmail.com">
		<br><br>

		<input type="submit" value="Register">
	</form>

	<#else>

	<p>You're already logged in! Click <a href="/logout" style="color: blue;">here</a> to <a href="/logout" style="color: blue;">log out</a>.</p>

	</#if>
</div>
</body>
</html>