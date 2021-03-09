<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">

<#if userId lte 0>
	<#include "please_login.ftl">
<#else>

<div class="make-appointment">
	<input type="hidden" name="studentID" value="${userId}">
	<p>Selected teacher: ${instructor}</p>
	<input type="hidden" name="instructorID" value="${instructorId}">
	<p>Date: ${date}</p>
	<input type="hidden" name="date" value="${date}">
	<p>Available from: ${earliestStartTime}</p>
	<p>Available till: ${latestEndTime}</p>
	<label for="startTime">Choose a start time:</label>
	<select name="startTime" id="startTime">
		<#list startTimes as startTime>
			<option value="startTime">${startTime}</option>
		</#list>
	</select>
	<br>
	<label for="duration">Choose the class duration: </label>
	<select name="duration" id="duration">
		<#list durations as duration>
			<option value="duration">${duration}</option>
		</#list>
	</select>
	<input type="submit" value="Make appointment">
</div>

</#if>

</body>
</html>