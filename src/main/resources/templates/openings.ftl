<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

	<div class="openings-buttons">
		<ul class="fake-buttons">
			<li>
				<a href="#favorites" class="fake-button">My favorites</a>
			</li>
			<li>
				<a href="#search" class="fake-button">Search openings</a>
			</li>
		</ul>
	</div>

	<div class="openings-text">
		<h1>Teacher openings for ${startDate} to ${endDate}:</h1>
	</div>

	<table>
		<tr>
			<th>Sunday</th><th>Monday</th><th>Tuesday</th><th>Wednesday</th><th>Thursday</th><th>Friday</th><th>Saturday</th>
		</tr>
		<tr>
			<#list days as day>
				<td>
					<#list day as openingData>
						<#if openingData?has_content>
							<br><a style="color: blue;" href="/make_appointment?instructor=${openingData.instructor}&date=${openingData.date}&startTime=${openingData.startTime}&endTime=${openingData.endTime}">${openingData.instructor}:<br>${openingData.startTime} - ${openingData.endTime}<br></a>
						<#else>
							No openings!
						</#if>
					</#list>
				</td>
			</#list>
		</tr>
	</table>
	
</body>
</html>