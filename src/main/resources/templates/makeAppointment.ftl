<#include "head.ftl">
<body>
<#include "header.ftl">
<#if userId lte 0>
<div class="page-content-550">
	<#include "please_login.ftl">
</div>
<#else>
<h1 class="page-title">Make Appointment:</h1>
<br>
<div class="multiform-container">
	<div class="page-content-550 floating-box">
		<form method="get" action="/confirm_make_appointment">
			<input type="hidden" name="openingId" value="${openingId}">
			<input type="hidden" name="studentId" value="${studentId}">
			<input type="hidden" name="instructorId" value="${instructorId}">
			<input type="hidden" name="date" value="${date}">
			<input type="hidden" name="openingStartTime" value="${openingStartTime}">
			<input type="hidden" name="openingEndTime" value="${openingEndTime}">

			<div class="form-grid-full-rows">
				<p class="label">Student:</p><p><a href="/profile?memberId=${studentId}">${studentName}</a></p>
				<p class="label">Instructor:</p><p><a href="/profile?memberId=${instructorId}">${instructorName}</a></p>
				<p class="label">Date:</p><p>${date}</p>
				<p class="label">Available from:</p><p>${openingStartTime}</p>
				<p class="label">Available till:</p><p>${openingEndTime}</p>
			</div>
			<br>
			<div class="form-grid-full-rows">
				<label for="appointmentStartTime">Start Time:</label>
				<select name="appointmentStartTime" id="appointmentStartTime" onchange="adjustDurations(this);">
					<#list possibleStartTimes as possibleStartTime>
					<#if possibleStartTime = defaultStartTime>
					<option value="${possibleStartTime}" selected="selected">${possibleStartTime}</option>
					<#else>
					<option value="${possibleStartTime}">${possibleStartTime}</option>
					</#if>
					</#list>
				</select>
				<label for="appointmentDuration">Duration:</label>
				<select name="appointmentDuration" id="appointmentDuration">
					<#list possibleDurations as possibleDuration>
					<#if possibleDuration = defaultDuration>
					<option value="${possibleDuration}" selected="selected">${possibleDuration}</option>
					<#else>
					<option value="${possibleDuration}">${possibleDuration}</option>
					</#if>
					</#list>
				</select>
			</div>
			<br>
			<div class="form-flex-row">
				<input type="submit" value="Make Appointment">
			</div>
		</form>
	</div>
	<div class="page-content-550">
		<div class="availability-chart">
			<#list 0..23 as i>
			<div class="hour">
				<div class="quarter first open">${i?string("00")}:00</div>
				<div class="quarter busy">:15</div>
				<div class="quarter busy">:30</div>
				<div class="quarter last closed">:45</div>
			</div>
			</#list>
		</div>
	</div>
</div>
</#if>

</body>
<#if userId gt 0>
<script src="/dynamic.js"></script>
<script>
//const startTimes = [<#list possibleStartTimes as possibleStartTime>'${possibleStartTime}',</#list>];
const durations = [<#list possibleDurations as possibleDuration>'${possibleDuration}',</#list>];

const adjustDurations = (timeSelector) => {
	const timeIndex = timeSelector.selectedIndex;
	const durationMaxIndex = durations.length - timeIndex;
	const durationSelector = document.getElementById('appointmentDuration');
	const durationIndex = durationSelector.selectedIndex;
	
	durationSelector.options.length = 0;
	let newOption;
	for (let i = 0; i < durationMaxIndex; i++) {
		newOption = document.createElement('Option');
		newOption.text = durations[i];
		durationSelector.options.add(newOption);
	}
	durationSelector.options[0].selected = false;
	if (durationIndex >= durationMaxIndex) {
		durationSelector.options[durationMaxIndex - 1].selected = true;
	} else {
		durationSelector.options[durationIndex].selected = true;
	}
}
</script>
</#if>
</html>