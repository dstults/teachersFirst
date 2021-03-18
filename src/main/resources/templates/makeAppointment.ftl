<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>

<div class="make-appointment">
	<form method="get" action="/confirm_make_appointment">
		<input type="hidden" name="openingId" value="${openingId}">
		<input type="hidden" name="studentId" value="${studentId}">
		<input type="hidden" name="instructorId" value="${instructorId}">
		<input type="hidden" name="date" value="${date}">
		<input type="hidden" name="openingStartTime" value="${openingStartTime}">
		<input type="hidden" name="openingEndTime" value="${openingEndTime}">

		<p>Student: <a href="/profile?memberId=${studentId}">${studentName}</a></p>
		<p>Instructor: <a href="/profile?memberId=${instructorId}">${instructorName}</a></p>
		<p>Date: ${date}</p>
		<p>Available from: ${openingStartTime}</p>
		<p>Available till: ${openingEndTime}</p>

		<label for="appointmentStartTime">Choose a start time: </label>
		<select name="appointmentStartTime" id="appointmentStartTime" onchange="adjustDurations(this);">
			<#list possibleStartTimes as possibleStartTime>
				<#if possibleStartTime = defaultStartTime>
				<option value="${possibleStartTime}" selected="selected">${possibleStartTime}</option>
				<#else>
				<option value="${possibleStartTime}">${possibleStartTime}</option>
				</#if>
			</#list>
		</select>
		<br>

		<label for="appointmentDuration">Choose the class duration: </label>
		<select name="appointmentDuration" id="appointmentDuration">
			<#list possibleDurations as possibleDuration>
				<#if possibleDuration = defaultDuration>
				<option value="${possibleDuration}" selected="selected">${possibleDuration}</option>
				<#else>
				<option value="${possibleDuration}">${possibleDuration}</option>
				</#if>
			</#list>
		</select>

		<input type="submit" value="Make Appointment">
	</form>
</div>

</#if>

</body>
<#if userId gt 0>
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