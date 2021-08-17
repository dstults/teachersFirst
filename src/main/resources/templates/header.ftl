	<header>
		<div class="side-circle c1"></div>
		<div class="side-circle c2"></div>
		<div class="side-circle c3"></div>
		<div class="side-circle c4"></div>
		<div class="main-title-text">
			<h1>${websiteTitle}</h1>
			<h3>${websiteSubtitle}</h3>
		</div>
		<#if userId gt 0>
		<div class="top-right-button welcome-text gray">Welcome, <span class="black">${userName}</span></div>
		<a href="/logout"><div class="top-right-button slightly-lower mouseover faded">Log Out</div></a>
		<#else>
		<a href="/login"><div class="top-right-button navy mouseover">Log In</div></a>
		<#if canRegister><a href="/register"><div class="top-right-button slightly-lower navy mouseover">Register</div></a></#if>
		</#if>
		<#if time1Name?has_content && time1Time?has_content && time2Name?has_content && time2Time?has_content><div class="bottom-right timestamp timestamp-grid">
			<p class="label col1to3">Last Update:</p>
			<p class="label">${time1Name}</p><p>${time1Time}</p>
			<p class="label">${time2Name}</p><p>${time2Time}</p>
		</div></#if>
		<nav>
			<ul class="top-nav-list">
				<li><a href="/services" class="nav-link">Services</a></li>
				<li><a href="/openings" class="nav-link">Openings</a></li>
				<#if userId gt 0>
				<li><a href="/appointments" class="nav-link">Appointments</a></li>
				<li><a href="/profile" class="nav-link">Profile</a></li>
				<#if isAdmin || isInstructor>
				<li><a href="/members" class="nav-link">Members</a></li>
				</#if>
				</#if>
			</ul>
		</nav>
	</header>
	<#if userId gt 0>
	<div id="console-frame" class="console-frame">
		<div class="console-topbar">
			<p id="console-drag-bar" class="console-drag-bar">Messages</p>
			<p id="console-closer" class="console-closer">-</p>
		</div>
		<ul id="console-message-list" class="console-message-list" style="display: none;">
		</ul>
	</div>
	<script src="/scripts/dragElement.js"></script>
	<script src="/scripts/messageConsole.js"></script>
	<#if message != "">
	<script>
		let bannerMessage = `${message}`;
		bannerMessage = bannerMessage.replaceAll('\n', ' // ');
		bannerMessage = bannerMessage.replaceAll('<br>', ' // ');
		const messages = bannerMessage.split(' // ');
		for (const msg of messages)
			addMessage(msg);
	</script>
	</#if>
	<#elseif message != "">
	<div id="message-banner" class="banner">
		<p>${message}</p>
	</div>
	</#if>