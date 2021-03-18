<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="fixed-width-subpage">
	<div class="new-opening">
		<#if userId lte 0>
			<#include "please_login.ftl">
		<#elseif isAdmin || isInstructor>

		<h1>Please make a new opening:</h1>

		<form method="post" action="/" onsubmit = "return false;">
			<label for="instructorId" style="margin-top: 1.3rem;">Instructor ID:</label>
			<input type="text" id="instructorId" value="${instructorId}" style="width: 100px; margin-left: 1.2rem; text-align: center;" placeholder="##">
			<ul class="opening-days">
				<li>
					<input type="checkbox" id="sunday" value="Su"${sundayChecked}>
					<label for="sunday">Sun</label>
				</li>
				<li>
					<input type="checkbox" id="monday" value="Mo"${mondayChecked}>
					<label for="monday">Mon</label>
				</li>
				<li>
					<input type="checkbox" id="tuesday" value="Tu"${tuesdayChecked}>
					<label for="tuesday">Tue</label>
				</li>
				<li>
					<input type="checkbox" id="wednesday" value="We"${wednesdayChecked}>
					<label for="wednesday">Wed</label>
				</li>
				<li>
					<input type="checkbox" id="thursday" value="Th"${thursdayChecked}>
					<label for="thursday">Thu</label>
				</li>
				<li>
					<input type="checkbox" id="friday" value="Fr"${fridayChecked}>
					<label for="friday">Fri</label>
				</li>
				<li>
					<input type="checkbox" id="saturday" value="Sa"${saturdayChecked}>
					<label for="saturday">Sat</label>
				</li>
			</ul>

			<ul class="opening-times">
				<li>
					<label for="startDate">Start Date: </label>
					<input type="date" id="startDate" value="${startDate}">
				</li>
				<li class="second-value">
					<label for="startTime">Start Time: </label>
					<input type="time" id="startTime" value="${startTime}">
				</li>
			</ul>
			<ul class="opening-times">
				<li>
					<label for="endDate">End Date: </label>
					<input type="date" id="endDate" value="${endDate}">
				</li>
				<li class="second-value" style="margin-left:1.7rem;">
					<label for="endTime">End Time: </label>
					<input type="time" id="endTime" value="${endTime}">
				</li>
			</ul>
			
			<input type="submit" id="submitOpening" value="Create Openings" onclick="handlePost();">
		</form>

		<#else>
			<p>Sorry, but you must be a teacher to use this feature.</p>
		</#if>
	</div>
</div>
</body>

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
		xhr.send('action=new_openings&instructorId=' + instructorId + '&daysOfWeek=' + daysOfWeek + '&startDate=' + startDate + '&startTime=' + startTime + '&endDate=' + endDate + '&endTime=' + endTime);
	}
}
</script>

</html>