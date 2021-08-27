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
		<#include "widgetClock.ftl">
		<nav>
			<ul class="top-nav-list">
				<li><a href="/services" class="nav-link">Services</a></li>
				<li><a href="/openings" class="nav-link">Openings</a></li>
				<#if userId gt 0>
				<li><a href="/appointments" class="nav-link">Appointments</a></li>
				<li><a href="/members" class="nav-link">Members</a></li>
				<li><a href="/profile" class="nav-link">Profile</a></li>
				</#if>
			</ul>
		</nav>
	</header>
	<#if userId gt 0>
	<#include "widgetConsole.ftl">
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