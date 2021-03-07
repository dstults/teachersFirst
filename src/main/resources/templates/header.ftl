	<header>
		<div class="landing-text">
			<h1>${websiteTitle}</h1>
			<h6>${websiteSubtitle}</h6>
			<div class="login-logout">
				<#if showWelcome && userId gt 0>
					<p>Welcome, ${userName}<br><a href="/logout" style="color: blue;">Log Out</a></p>
				<#elseif showWelcome>
					<a href="/login" style="color: blue;">Log In</a>
				</#if>
			</div>
		</div>
		<nav class="top-nav">
			<ul class="nav-list">
				<li>
					<a href="/services" class="nav-link">Services</a>
				</li>
				<li>
					<a href="/openings" class="nav-link">Openings</a>
				</li>
				<li>
					<a href="/appointments" class="nav-link">Appointments</a>
				</li>
				<li>
					<a href="/calendar" class="nav-link">Calendar</a>
				</li>
				<li>
					<a href="/profile" class="nav-link">Profile</a>
				</li>
				<li>
					<a href="/members" class="nav-link">Members</a>
				</li>
			</ul>
		</nav>
	</header>

	<#if message != "">
	<p class="banner">${message}</p>
	</#if>
