<!DOCTYPE html>
<#include "head.ftl">
<body>
	<#include "header.ftl">
	<div class="page-content-750-1000">
	<#if userId lte 0>
	<#include "please_login.ftl">
	<#else>
		<div class="appointment-title-container">
			<div class="appointment-page-controls">
				<a href="javascript:prevPage();">Prev Page</a>
				<p id="current-page">1</p>
				<a href="javascript:nextPage();">Next Page</a>
			</div>
			<h2 class="appointments-subtitle">Upcoming appointments</h2>
			<div></div>
		</div>
		<table class="info-list">
			<tr>
				<th>#</th>
				<th>Name</th>
				<th>ID</th>
				<#if isAdmin || isInstructor><th>Credits</th></#if>
				<th>Gender</th>
				<th>Age</th>
				<th>Phone</th>
				<th>Email</th>
			</tr>
			<#list 0..11 as i>
			<tr id="member-row-${i?c}">
				<td id="member-row-${i?c}-arrayIndex"></td>
				<td id="member-row-${i?c}-displayName"></td>
				<td id="member-row-${i?c}-recId"></td>
				<#if isAdmin || isInstructor><td id="member-row-${i?c}-credits"></td></#if>
				<td id="member-row-${i?c}-gender"></td>
				<td id="member-row-${i?c}-age"></td>
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
	const isAdmin = <#if isAdmin>true<#else>false</#if>;
	const isInstructor = <#if isInstructor>true<#else>false</#if>;
	const isStudent = <#if isStudent>true<#else>false</#if>;

	let currentPage = 0;
	const currentPageElem = document.getElementById('current-page');

	let allMemberData = null;
	let filteredMemberData = null;
	const memberRows = 12;
	
	fetch('https://funteachers.org/members?json').then(response => response.json()).then(data => {
		allMemberData = data;
		filteredMemberData = allMemberData;
		refreshAll();
	}).catch(err => console.error(err.message));
	
	const renderRow = (member, row) => {
		const rowName = 'member-row-' + row;
		const tableRow = document.getElementById(rowName);
		const arrayIndex = document.getElementById(rowName + '-arrayIndex');
		const displayName = document.getElementById(rowName + '-displayName');
		while (displayName.firstChild) displayName.removeChild(displayName.firstChild); // safely remove child elements (anchor-link)
		const recId = document.getElementById(rowName + '-recId');
		while (recId.firstChild) recId.removeChild(recId.firstChild); // safely remove child elements (anchor-link)
		<#if isAdmin || isInstructor>const credits = document.getElementById(rowName + '-credits');</#if>
		const gender = document.getElementById(rowName + '-gender');
		const age = document.getElementById(rowName + '-age');
		const phones = document.getElementById(rowName + '-phones');
		const email = document.getElementById(rowName + '-email');
		if (!member) {
			tableRow.classList.remove('soft-highlight');
			arrayIndex.innerHTML = '';
			displayName.innerHTML = '';
			recId.innerHTML = '';
			<#if isAdmin || isInstructor>credits.innerHTML = '';</#if>
			gender.innerHTML = '';
			age.innerHTML = '';
			phones.innerHTML = '';
			email.innerHTML = '';
		} else {
			if (false) {
				tableRow.classList.add('soft-highlight');
			} else {
				tableRow.classList.remove('soft-highlight');
			}
			arrayIndex.innerHTML = 1 + row + currentPage * memberRows;
			displayName.innerHTML = '<a href="/profile?memberId=' + member.id + '">' + member.displayName + '</a>';
			recId.innerHTML = '<a href="/profile?memberId=' + member.id + '">' + member.id + '</a>';
			<#if isAdmin || isInstructor>credits.innerHTML = member.credits;</#if>
			gender.innerHTML = member.gender;
			phones.innerHTML = member.phone1 + '\n' + member.phone2;
			email.innerHTML = member.email;
		}
	};

	const getMemberFromFiltered = (row) => filteredMemberData[row + memberRows * currentPage];
	const refreshAll = _ => {
		currentPageElem.innerHTML = currentPage + 1;
		for (let i = 0; i < memberRows; i++) {
			const member = getMemberFromFiltered(i);
			renderRow(member, i);
		}
	};

	const prevPage = _ => {
		if (currentPage > 0) {
			currentPage--;
			refreshAll();
		} else {
			// This is annoying, it should just disable/reenable.
			//alert('Already at first page!');
		}
	};
	const nextPage = _ => {
		if (currentPage < Math.trunc((filteredMemberData.length - 1) / memberRows)) {
			currentPage++;
			refreshAll();
		} else {
			// This is annoying, it should just disable/reenable.
			//alert('Already at last page!');
		}
	};


</script>
</#if>
</html>
