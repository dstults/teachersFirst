<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

	<div class="openings-buttons">
		<ul class="fake-buttons">
			<li>
				<a href="javascript:openFilter();" class="fake-button">Search openings</a>
			</li>
			<#if isAdmin || isInstructor>
			<li>
				<a href="javascript:makeOpenings();" class="fake-button">Make New Openings</a>
			</li>
			</#if>
		</ul>
	</div>

	<div class="openings-text">
		<h1>Teacher openings for ${startDate} to ${endDate}:</h1>
	</div>

	<table class="openings-calendar">
		<tr>
			<th>SUN</th><th>MON</th><th>TUE</th><th>WED</th><th>THU</th><th>FRI</th><th>SAT</th>
		</tr>
		<#list weeks as days>
		<tr>
			<#list days as day>
			<td class="${day.color}">
				<p class="calendar-date">${day.name}</p>
				<#if day.openings?has_content && day.openings?size != 0>
				<#list day.openings as opening>
				<#if isAdmin || (isInstructor && opening.instructorId == userId)>
				<div class="${opening.highlight}">
					<p class="aleft"><a href="javascript:confirmDeleteOpening(${opening.id?c});" class="red bold">X </a><a href="/make_appointment?openingId=${opening.id}&instructorId=${opening.instructorId}&date=${opening.date}&openingStartTime=${opening.startTime}&openingEndTime=${opening.endTime}">${opening.instructorName}:</p>
					<p class="aright">${opening.startTime} - ${opening.endTime}</a></p>
				</div>
				<#else>
				<a href="/make_appointment?openingId=${opening.id}&instructorId=${opening.instructorId}&date=${opening.date}&openingStartTime=${opening.startTime}&openingEndTime=${opening.endTime}"><div class="${opening.highlight}">
					<p class="aleft">${opening.instructorName}:</p>
					<p class="aright">${opening.startTime} - ${opening.endTime}</p>
				</div></a>
				</#if>
				</#list>
				<#else>
				
				</#if>
			</td>
			</#list>
		</tr>
		</#list>
	</table>
	
</body>
<script>
const openFilter = () => {
	let instructor = prompt("Please enter a name to filter by.", "");
	if (instructor != null) {
		window.location.href = "/openings?instructorName=" + instructor;
	}
}
<#if isAdmin || isInstructor>
const makeOpenings = () => window.location.href = "/new_openings";
const confirmDeleteOpening = (openingId) => {
	if (confirm('Are you sure you want to delete opening ID #' + openingId + ' ?')) {
		const xhr = new XMLHttpRequest();
		xhr.open('POST', '/');
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onload = function() {
			if (xhr.status === 200) {
				window.location.href = xhr.responseURL;
			}
		}
		xhr.send('action=delete_opening&openingId=' + openingId);
	}
}
</#if>
</script>
</html>