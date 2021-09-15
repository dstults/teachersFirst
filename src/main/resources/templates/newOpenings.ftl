<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="page-content-550">
	<#if userId lte 0>
	<#include "please_login.ftl">
	<#elseif isAdmin || isInstructor>
	<h1>New Opening:</h1>
	<br>
	<form method="post" action="/" onsubmit="return false;">
		<div class="form-grid-full-rows">
			<p class="label">Instructor ID:</p>
			<select id="instructor-id" value="null">
				<option value="null">-</option>
			</select>
		</div>
		<br>
		<div class="form-grid-days-of-week">
			<label for="sunday">Sun</label>
			<input type="checkbox" id="sunday" value="Su">
			<label for="monday">Mon</label>
			<input type="checkbox" id="monday" value="Mo">
			<label for="tuesday">Tue</label>
			<input type="checkbox" id="tuesday" value="Tu">
			<label for="wednesday">Wed</label>
			<input type="checkbox" id="wednesday" value="We">
			<label for="thursday">Thu</label>
			<input type="checkbox" id="thursday" value="Th">
			<label for="friday">Fri</label>
			<input type="checkbox" id="friday" value="Fr">
			<label for="saturday">Sat</label>
			<input type="checkbox" id="saturday" value="Sa">
		</div>
		<br>
		<div class="form-grid-datetime-rows">
			<p class="label">Start Date: </p>
			<input type="date" id="start-date">
			<p class="label">Start Time: </p>
			<input type="time" id="start-time" value="12:00">
			<p class="label">End Date: </p>
			<input type="date" id="end-date">
			<p class="label">End Time: </p>
			<input type="time" id="end-time" value="21:00">
		</div>
		<br>
		<div class="form-flex-row">
			<input type="submit" id="submitOpening" value="Create Openings" onclick="handlePost();">
		</div>
	</form>
	<#else>
	<p>Sorry, but you must be a teacher to use this feature.</p>
	</#if>
</div>
</body>
<#if userId gt 0>
<script src="/dynamic.js"></script>
<script src="/scripts/utils.js"></script>
<script src="/scripts/newOpeningOperations.js"></script>
</#if>
</html>