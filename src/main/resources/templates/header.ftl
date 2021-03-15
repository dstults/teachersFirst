	<header>
		<div class="landing-text">
			<h1>${websiteTitle}</h1>
			<h6>${websiteSubtitle}</h6>
			
			<#if userId gt 0>
				<div class="top-right-button welcome-text gray">Welcome, <span class="black">${userName}</span></div>
				<a href="/logout"><div class="top-right-button slightly-lower mouseover faded">Log Out</div></a>
			<#else>
				<a href="/login"><div class="top-right-button navy mouseover">Log In</div></a>
				<a href="/register"><div class="top-right-button slightly-lower navy mouseover">Register</div></a>
			</#if>
			</div>
		</div>
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

<#if message != "">
	<div class="banner">
		<p>${message}</p>
	</div>
</#if>