<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="fixed-width-subpage">

	<#if userId lte 0>

	<form method="post" action="/">
		<input type="hidden" name="action" value="register_new_member">

		<label for="full_name">Display Name (how others see you): <span class="required">*</span></label>
		<input type="text" name="displayName" placeholder="Jane Doe"/> 
		<br><br>

		<label for="username">Login Name (this is private): <span class="required">*</span></label>
		<input type="text" name="loginName" placeholder="jdoe93">
		<br><br>

		<label for="password">Password: <span class="required">*</span></label>
		<input type="password" name="password1">
		<br><br>

		<label for="confirm_password">Confirm Password: <span class="required">*</span></label>
		<input type="password" name="password2">
		<br><br> 

		<label for="gender" class="gender">Gender:	</label>
		<input type="radio" name="gender" value="m"><label for="Male" class="radio">Male</label>
		<input type="radio" name="gender" value="f"><label for="Female" class="radio">Female</label>
		<input type="radio" name="gender" value="" checked><label for="Other" class="radio">Other/Unspecified</label>
		<br><br>

		<p><span class="required">*</span> Please provide at least one form of contact:</p>

		<label for="age">Phone 1: </label>
		<input type="text" name="phone1" placeholder="425-555-5555">
		<br><br>

		<label for="age">Phone 2: </label>
		<input type="text" name="phone2">
		<br><br>

		<label for="age">Email: </label>
		<input type="email" name="email" placeholder="jdoe@gmail.com">
		<br><br>

		<input type="submit" value="Register">
	</form>

	<#else>

	<p>You're already logged in! Click <a href="/logout" style="color: blue;">here</a> to <a href="/logout" style="color: blue;">log out</a>.</p>

	</#if>
</div>
</body>
</html>