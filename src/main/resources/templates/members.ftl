<!DOCTYPE html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Services</title>
	<link rel="stylesheet" href="/css/main.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>

<body>
<header>
	<div class="landing-text">
		<h1>Name of the website</h1>
		<h6>Very short one sentence description</h6>
	</div>
	<nav class="top-nav">
		<ul class="nav-list">
			<li>
				<a href="/services" class="nav-link">Services</a>
			</li>
			<li>
				<a href="/messages" class="nav-link">Messages</a>
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
		</ul>
	</nav>
</header>

<div class="services-buttons">
	<ul class="fake-buttons">
		<li>
			<a href="#favorites" class="fake-button">My favorites</a>
		</li>
		<li>
			<a href="#search" class="fake-button">Search services</a>
		</li>
	</ul>
</div>

<table>
	<tr>
		<th>Service Name</th><th>Teacher</th><th>Time</th><th>Price</th><th>Reviews</th><th>Favorite</th>
	</tr>
	<#list members as member>
		<tr>
			<td>${member.recID}</td>
			<td>${member.name}</td>
			<td>${member.age}</td>
			<td>${member.gender}</td>
			<td>${member.favoriteColor}</td>
			<td>${member.favoriteFood}</td>
		</tr>
	</#list>
</table>
</body>