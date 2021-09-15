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
	<form method="post" action="/" onsubmit = "return false;">
		<input id="actionType" type="hidden" value="make_openings">
		<div class="form-grid-full-rows">
			<p class="label">Instructor ID:</p>
			<input type="text" id="instructorId" value="${instructorId}" placeholder="##">
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
			<input type="submit" id="submitOpening" value="Create Openings" onclick="handlePost();">
		</div>
	</form>
	<#else>
	<p>Sorry, but you must be a teacher to use this feature.</p>
	</#if>
</div>
</body>
<script src="/dynamic.js"></script>
<script>
const handlePost = () => {
	const sunday = document.getElementById('sunday').checked ? document.getElementById('sunday').value : '';
	const monday = document.getElementById('monday').checked ? document.getElementById('monday').value : '';
	const tuesday = document.getElementById('tuesday').checked ? document.getElementById('tuesday').value : '';
	const wednesday = document.getElementById('wednesday').checked ? document.getElementById('wednesday').value : '';
	const thursday = document.getElementById('thursday').checked ? document.getElementById('thursday').value : '';
	const friday = document.getElementById('friday').checked ? document.getElementById('friday').value : '';
	const saturday = document.getElementById('saturday').checked ? document.getElementById('saturday').value : '';
	const daysOfWeek = sunday + monday + tuesday + wednesday + thursday + friday + saturday; 

	if (daysOfWeek == '') {
		alert('Please select which days of the week will be enabled.');
		return;
	}

	const instructorId = document.getElementById('instructorId').value;
	const startDate = document.getElementById('startDate').value;
	const startTime = document.getElementById('startTime').value;
	const endDate = document.getElementById('endDate').value;
	const endTime = document.getElementById('endTime').value;

	if (confirm('Please confirm:' + 
			'\nInstructor ID: ' + instructorId + '   Days of the Week: ' + daysOfWeek +
			'\nDates: ' + startDate + ' - ' + endDate +
			'\nTimes: ' + startTime + ' - ' + endTime)) {

		const xhr = new XMLHttpRequest();
		xhr.open('POST', '/');
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onload = () => { if (xhr.status === 200) window.location.href = xhr.responseURL; };
		xhr.send('action=make_openings&instructorId=' + instructorId + '&daysOfWeek=' + daysOfWeek + '&startDate=' + startDate + '&startTime=' + startTime + '&endDate=' + endDate + '&endTime=' + endTime);
	}
}
</script>

</html>