<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

	<div class="openings-buttons">
		<ul class="fake-buttons">
			<li>
				<a href="javascript:openFilter();" class="fake-button">Search openings</a>
			</li>
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
				<a href="/make_appointment?instructorId=${opening.instructorId}&date=${opening.date}&openingStartTime=${opening.startTime}&openingEndTime=${opening.endTime}"><div class="${opening.highlight}">
					<p class="aleft">${opening.instructorName}:</p>
					<p class="aright">${opening.startTime} - ${opening.endTime}</p>
				</div></a>
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
</script>
</html>