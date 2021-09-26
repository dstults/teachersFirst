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
			<input id="input-student-id" type="hidden" name="studentId" value="${studentId}">
			<input id="input-instructor-id" type="hidden" name="instructorId" value="${instructorId}">
			<input id="input-date" type="hidden" name="date" value="${date}">
			<input id="input-start-time" type="hidden" name="openingStartTime" value="${openingStartTime}">
			<input id="input-end-time" type="hidden" name="openingEndTime" value="${openingEndTime}">

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
			<#assign j = 0>
			<#list 0..23 as i>
			<div class="hour">
				<div id="slot-${j}" class="quarter first">${i?string("00")}:00</div><#assign j++>
				<div id="slot-${j}" class="quarter">:15</div><#assign j++>
				<div id="slot-${j}" class="quarter">:30</div><#assign j++>
				<div id="slot-${j}" class="quarter last">:45</div><#assign j++>
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

const uid1 = document.getElementById('input-student-id').value;
const uid2 = document.getElementById('input-instructor-id').value;
const date = document.getElementById('input-date').value;
const startTime = document.getElementById('input-start-time').value;
const endTime = document.getElementById('input-end-time').value;

const slotToStartTime = slot => { 
	return {
		hours: Math.trunc(slot / 4),
		minutes: 15 * Math.trunc(slot % 4),
	};
};
const timeToSlot = time => {
	const [ hours, minutes ] = time.split(':');
	return Math.trunc(hours * 4 + minutes / 15);
};

const slotCount = 24 * 4;
const startSlot = timeToSlot(startTime);
const endSlot = timeToSlot(endTime);

const slots = [];

const populateOpenings = _ => {
	for (let i = 0; i < slotCount; i++) {
		slots.push(document.getElementById('slot-' + i));
		if (i < startSlot || i >= endSlot) slots[i].classList.add('closed');
		else slots[i].classList.add('open');
	}
};

populateOpenings();

let blockers = [];
const getBlockers = async _ => {
	try {
		const response = await fetch('/conflicts?uid1=' + uid1 + '&uid2=' + uid2 + '&date=' + date + '&startTime=' + startTime + '&endTime=' + endTime);
		const json = await response.json();
		blockers = json;
		populateBlocks();
	} catch(err) {
		console.error(err.name, err.message);
	} 
}
getBlockers();

const populateBlocks = _ => {
	for (const blocker of blockers) {
		const slot1 = timeToSlot(blocker.startTimeFormatted);
		const slot2 = timeToSlot(blocker.endTimeFormatted);
		markSlotsBusy(slot1, slot2);
	}
};

const markSlotsBusy = (slot1, slot2) => {
	let slotElement;
	while (true) {
		slotElement = document.getElementById('slot-' + slot1);
		slotElement.classList.remove('open');
		if (!slotElement.classList.contains('closed')) slotElement.classList.add('busy');
		slot1 = (slot1 + 1) % 96;
		if (slot1 === slot2) break;
	}
};

</script>
</#if>
</html>