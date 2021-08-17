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
				<a href="javascript:prevPage();">Prev Page</a>
				<p id="current-page">1</p>
				<a href="javascript:nextPage();">Next Page</a>
			</div>
			<h2 class="info-list-subtitle">Members</h2>
			<div></div>
		</div>
		<table class="info-list">
			<tr>
				<th style="width: 50px;">#</th>
				<th style="width: 165px;">Name</th>
				<th style="width: 50px;">ID</th>
				<#if isAdmin || isInstructor><th style="width: 50px;" class="credits"><img src="/images/credit-coin.svg" height="22" style="vertical-align: bottom;"></th></#if>
				<th style="width: 165px;">Category</th>
				<th style="width: 135px;">Phone</th>
				<th style="width: 135px;">Email</th>
			</tr>
			<#list 0..11 as i>
			<tr id="member-row-${i?c}">
				<td id="member-row-${i?c}-arrayIndex"></td>
				<td id="member-row-${i?c}-displayName"></td>
				<td id="member-row-${i?c}-recId"></td>
				<#if isAdmin || isInstructor><td id="member-row-${i?c}-credits" class="credits"></td></#if>
				<td id="member-row-${i?c}-category"></td>
				<td id="member-row-${i?c}-phones"></td>
				<td id="member-row-${i?c}-email"></td>
			</tr>
			</#list>
		</table>
	</div>
	</#if>
</body>
<#if userId gt 0>
<script>
	const isAdmin = ${isAdmin?c};
	const isInstructor = ${isInstructor?c};
	const isStudent = <#if isStudent>true<#else>false</#if>;
</script>
<script src="/scripts/memberOperations.js"></script>
</#if>
</html>
