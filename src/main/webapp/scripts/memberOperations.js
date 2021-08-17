
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

const boyImage = '<img src="/images/boy.svg" style="background-color: hsl(220, 100%, 85%);height: 38px;padding: 2px;vertical-align: middle;border-radius: 10px;width: 32px;">';
const girlImage = '<img src="/images/girl.svg" style="background-color: hsl(0, 80%, 90%); height: 38px;padding: 2px;vertical-align: middle;border-radius: 10px;width: 32px;">';
const phoneImage = '<img src="/images/phone.svg" style="vertical-align: middle; background-color: hsl(0, 0%, 100%); height: 24px; width: 24px; padding: 2px; border-radius: 6px;">';
const emailImage = '<img src="/images/email.svg" style="vertical-align: middle; background-color: hsl(0, 0%, 100%); height: 24px; width: 24px; padding: 2px; border-radius: 6px;">';
const renderRow = (member, row) => {
	const rowName = 'member-row-' + row;
	const tableRow = document.getElementById(rowName);
	const arrayIndex = document.getElementById(rowName + '-arrayIndex');
	const displayName = document.getElementById(rowName + '-displayName');
	while (displayName.firstChild) displayName.removeChild(displayName.firstChild); // safely remove child elements (anchor-link)
	const recId = document.getElementById(rowName + '-recId');
	while (recId.firstChild) recId.removeChild(recId.firstChild); // safely remove child elements (anchor-link)
	const credits = document.getElementById(rowName + '-credits');
	const category = document.getElementById(rowName + '-category');
	const phones = document.getElementById(rowName + '-phones');
	const email = document.getElementById(rowName + '-email');
	if (!member) {
		tableRow.classList.remove('soft-highlight');
		arrayIndex.innerHTML = '';
		displayName.innerHTML = '';
		recId.innerHTML = '';
		if (credits) credits.innerHTML = '';
		category.innerHTML = '';
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
		if (credits) credits.innerHTML = member.credits;
		switch (member.gender) {
			case 'm':
				category.innerHTML = boyImage;
				break;
			case 'f':
				category.innerHTML = girlImage;
				break;
			default:
				category.innerHTML = '-';
				break;
		}
		category.innerHTML += '<span style="vertical-align: middle;"> / ' + (member.age && member.age < 18 ? member.age : member.ageClass) + '</span>';
		phones.innerHTML = '';
		if (member.phone1) phones.innerHTML += phoneImage + '<span style="vertical-align: middle;"> ' + member.phone1 + '</span>';
		if (member.phone1 && member.phone2) phones.innerHTML += '<br>';
		if (member.phone2) phones.innerHTML += phoneImage + '<span style="vertical-align: middle;"> ' + member.phone2 + '</span>';
		email.innerHTML = '';
		if (member.email) email.innerHTML += emailImage + '<span style="vertical-align: middle;"> ' + member.email + '</span>';
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
