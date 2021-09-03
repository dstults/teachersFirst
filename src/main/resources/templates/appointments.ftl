<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
	<div class="page-content-750-1000">
<#if userId lte 0>
	<#include "please_login.ftl">
<#else>
		<div class="info-list-title-container">
			<div class="info-list-page-controls">
				<a href="javascript:futurePagePrev();">Prev Page</a>
				<p id="current-future-page">1</p>
				<a href="javascript:futurePageNext();">Next Page</a>
			</div>
			<h2 class="info-list-subtitle">Upcoming appointments</h2>
			<div class="data-chart-top-right-buttons">
				<a href="javascript:updateFilter();" class="data-chart-button"><img src="/images/search.svg" height="22" style="vertical-align: middle;"> Filter</a>
			</div>
		</div>
		<table class="info-list">
			<tr>
				<th style="width: 50px;">#</th>
				<th style="width: 135px;">Controls</th>
				<#if isAdmin || isInstructor><th style="width: 50px;">No.</th></#if>
				<th style="width: 108px;">Date</th>
				<th style="width: 66px;">Start</th>
				<th style="width: 66px;">End</th>
				<th style="width: 135px;">Attendee</th>
				<th style="width: 135px;">Instructor</th>
				<th style="width: 165px;">Status</th>
			</tr>
			<#list 0..9 as i>
			<tr id="future-row-${i?c}">
				<td id="future-row-${i?c}-arrayindex"></td>
				<td id="future-row-${i?c}-controls"></td>
				<#if isAdmin || isInstructor><td id="future-row-${i?c}-recID"></td></#if>
				<td id="future-row-${i?c}-date"></td>
				<td id="future-row-${i?c}-startTime"></td>
				<td id="future-row-${i?c}-endTime"></td>
				<td><a id="future-row-${i?c}-student"></a></td>
				<td><a id="future-row-${i?c}-instructor"></a></td>
				<td id="future-row-${i?c}-status"></td>
			</tr>
			</#list>
		</table>
		<br>
		<div class="info-list-title-container">
			<div class="info-list-page-controls">
				<a href="javascript:pastPagePrev();">Prev Page</a>
				<p id="current-past-page">1</p>
				<a href="javascript:pastPageNext();">Next Page</a>
			</div>
			<h2 class="info-list-subtitle">Previous appointments</h2>
			<div class="data-chart-top-right-buttons">
				<a href="javascript:updateFilter();" class="data-chart-button"><img src="/images/search.svg" height="22" style="vertical-align: middle;"> Filter</a>
			</div>
		</div>
		<table class="info-list">
			<tr>
				<th style="width: 50px;">#</th>
				<th style="width: 135px;">Controls</th>
				<#if isAdmin || isInstructor><th style="width: 50px;">No.</th></#if>
				<th style="width: 108px;">Date</th>
				<th style="width: 66px;">Start</th>
				<th style="width: 66px;">End</th>
				<th style="width: 135px;">Attendee</th>
				<th style="width: 135px;">Instructor</th>
				<th style="width: 165px;">Status</th>
			</tr>
			<#list 0..14 as i>
			<tr id="past-row-${i?c}">
				<td id="past-row-${i?c}-arrayindex"></td>
				<td id="past-row-${i?c}-controls"></td>
				<#if isAdmin || isInstructor><td id="past-row-${i?c}-recID"></td></#if>
				<td id="past-row-${i?c}-date"></td>
				<td id="past-row-${i?c}-startTime"></td>
				<td id="past-row-${i?c}-endTime"></td>
				<td><a id="past-row-${i?c}-student"></a></td>
				<td><a id="past-row-${i?c}-instructor"></a></td>
				<td id="past-row-${i?c}-status"></td>
			</tr>
			</#list>
		</table>
</#if>
	</div>
</body>
<#if userId gt 0>
<!-- <script src="/dynamic.js"></script> -->
<script src="/dynamic.js"></script>
<script src="/scripts/appointmentOperations.js"></script>
</#if>
</html>
