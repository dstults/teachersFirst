<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="page-content-550">
	<div class="new-opening">
		<#if userId lte 0>
			<#include "please_login.ftl">
		<#elseif isAdmin || isInstructor>

		<h1>Please make new appointments:</h1>

		<form method="post" action="/" onsubmit = "return false;">
			<ul>
				<li>
					<label for="instructorId" style="margin-top: 1.3rem;">Instructor ID:</label>
					<input type="text" id="instructorId" value="${instructorId}" style="width: 32rem;" placeholder="##">
				</li>
				<li class="second-value">
					<label for="studentId" style="margin-top: 1.3rem;">Student ID:</label>
					<select type="text" id="studentId" value="null" style="width: 32rem;">
						<option value="null">-</option>
					</select>
				</li>
			</ul>

			<ul class="day-list">
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

			<ul>
				<li>
					<label for="startDate">Start Date: </label>
					<input type="date" id="startDate" value="${startDate}">
				</li>
				<li class="second-value">
					<label for="startTime">Start Time: </label>
					<input type="time" id="startTime" value="${startTime}">
				</li>
			</ul>
			<ul>
				<li>
					<label for="endDate">End Date: </label>
					<input type="date" id="endDate" value="${endDate}">
				</li>
				<li class="second-value">
					<label for="endTime">End Time: </label>
					<input type="time" id="endTime" value="${endTime}">
				</li>
			</ul>
			
			<input type="submit" id="submitOpening" value="Create Appointments" onclick="handlePost();">
		</form>

		<#else>
			<p>Sorry, but you must be a teacher to use this feature.</p>
		</#if>
	</div>
</div>
</body>
<!--<script src="/dynamic.js"></script>-->
<script src="/scripts/batchAppointmentOperations.js"></script>
</html>
