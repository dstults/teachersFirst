<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="page-content-550">
	<#if userId lte 0>
	<#include "please_login.ftl">
	<#elseif isAdmin || isInstructor>
	<h1>Batch Appointments:</h1>
	<br>
	<form method="post" action="/" onsubmit = "return false;">
		<input id="actionType" type="hidden" value="make_appointment_batch">
		<div class="form-grid-full-rows">
			<p class="label">Instructor ID:</p>
			<select id="instructorId" value="null">
				<option value="null">-</option>
			</select>
			<p class="label">Student ID:</p>
			<select id="studentId" value="null">
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
			<input type="date" id="startDate" value="${startDate}">
			<p class="label">Start Time: </p>
			<input type="time" id="startTime" value="${startTime}">
			<p class="label">End Date: </p>
			<input type="date" id="endDate" value="${endDate}">
			<p class="label">End Time: </p>
			<input type="time" id="endTime" value="${endTime}">
		</div>
		<br>
		<div class="form-flex-row">
			<input type="submit" id="submitOpening" value="Create Appointments" onclick="handlePost();">
		</div>
	</form>
	<#else>
	<p>Sorry, but you must be a teacher to use this feature.</p>
	</#if>
</div>
</body>
<script src="/dynamic.js"></script>
<#if userId gt 0><script src="/scripts/batchAppointmentOperations.js"></script></#if>
</html>
