<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="make-appointment">
			<p>Selected teacher: ${instructor}</p>
			<p>Available from: ${startTime}</p>
			<p>Available till: ${endTime}</p>
			<label for="start">Choose a start time:</label>
			<select name="start" id="start">
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
</body>