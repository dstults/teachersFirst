<#include "head.ftl">
<body>
<#include "header.ftl">
<div class="page-content-550">
	<#if userId lte 0>
	<#include "please_login.ftl">
	<#else>
	<h1>Confirm Appointment:</h1>
	<br>
	<div class="form-grid-full-rows">
		<p class="label">Student:</p><p>${studentName}</p>
		<p class="label">Instructor:</p><p>${instructorName}</p>
		<p class="label">Date:</p><p>${date}</p>
		<p class="label">Starting:</p><p>${appointmentStartTime}</p>
		<p class="label">Ending:</p><p>${appointmentEndTime}</p>
	</div>
	<br>
	<p class="centered">Does everything look correct?</p>
	<form method="post" action="/">
		<input type="hidden" name="action" value="make_appointment">
		<input type="hidden" name="openingId" value="${openingId}">
		<input type="hidden" name="studentId" value="${studentId}">
		<input type="hidden" name="instructorId" value="${instructorId}">
		<input type="hidden" name="date" value="${date}">
		<input type="hidden" name="appointmentStartTime" value="${appointmentStartTime}">
		<input type="hidden" name="appointmentEndTime" value="${appointmentEndTime}">
		<br>
		<div class="form-flex-row">
			<a class="buttonize-link back-button" href="/make_appointment?openingId=${openingId}&studentId=${studentId}&instructorId=${instructorId}&date=${date}&openingStartTime=${openingStartTime}&openingEndTime=${openingEndTime}&appointmentStartTime=${appointmentStartTime}&appointmentEndTime=${appointmentEndTime}">&larr; BACK</a>
			<input type="submit" value="Confirm Appointment">
		</div>
		
	</form>
	</#if>
</div>
</body>
</html>