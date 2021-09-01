<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="page-content-550">
	<#if userId lte 0 || registerOther>
	<p class="h1-right-floater"><span class="required">*</span> Required fields</p>
	<h1>Register New User:</h1>
	<br>
	<form method="post" action="/">
		<input type="hidden" name="action" value="register_member">
		<div class="form-grid-full-rows form-grid-full-rows-halfsies">
			<p class="label">Display Name (public)<span class="required">*</span>:</p>
			<input type="text" name="displayName" value="${displayName}" placeholder="Jane Doe">
			<p class="label">Login Name (private)<span class="required">*</span>:</p>
			<input type="text" name="loginName" value="${loginName}" placeholder="jdoe93">
			<p class="label">Password<span class="required">*</span>:</p>
			<input type="password" name="password1">
			<p class="label">Confirm Password<span class="required">*</span>:</p>
			<input type="password" name="password2">
			<p class="label" style="align-self: baseline;">Gender:</p>
			<div class="form-grid-gender-rows" style="justify-self: baseline;">
				<input type="radio" name="gender" id="male" value="m"${maleChecked}><label for="male">Male</label>
				<input type="radio" name="gender" id="female" value="f"${femaleChecked}><label for="female">Female</label>
				<input type="radio" name="gender" id="other" value=""${otherChecked}><label for="other">Other/Unspecified</label>
			</div>
		</div>
		<br>
		<p class="h1-right-floater"><span class="desired">*</span> Please provide at least one good method.</p>
		<h1>Contact<span class="desired">*</span>:</h1>
		<div class="form-grid-full-rows form-grid-full-rows-halfsies">
			<p class="label">Phone 1:</p>
			<input type="text" name="phone1" value="${phone1}" placeholder="425-555-5555">
			<p class="label">Phone 2:</p>
			<input type="text" name="phone2" value="${phone2}">
			<p class="label">Email:</p>
			<input type="email" name="email" value="${email}" placeholder="jdoe@gmail.com">
		</div>
		<br>
		<div class="form-flex-row">
			<#if registerOther><input type="submit" value="Create User"><#else><input type="submit" value="Register"></#if>
		</div>
	</form>
	<#else>
	<p>You're already logged in!</p>
	<p>Click <a href="/logout">here to log out</a>.</p>
	</#if>
</div>
</body>
</html>