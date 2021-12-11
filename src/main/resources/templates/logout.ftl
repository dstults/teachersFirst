<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="logout">
	<#if userId lte 0>
	<p>You're not logged in!</p>
	<#else>
	<p>Log Out from...</p>
	<div class="left-right-grid">
		<form method="post" action="/">
			<input type="hidden" name="action" value="log_out">
			<input type="submit" value="This Device">
		</form>
		<form method="post" action="/">
			<input type="hidden" name="action" value="log_out">
			<input type="hidden" name="allDevices" value="true">
			<input type="submit" value="All Devices">
		</form>
	</div>
	</#if>
</div>
</body>
</html>