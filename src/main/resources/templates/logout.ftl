<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="logout">
	<#if userId lte 0>
	<p>You're not logged in!</p>
	<#else>
	<p>
		Click here to logout:
		<form method="post" action="/"><input type="hidden" name="action" value="log_out"><input type="submit" value="Logout"></form>
	</p>
	</#if>
</div>
</body>
</html>