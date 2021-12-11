<!doctype html>
<html lang="en-US">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Diagnostics</title>
	<link rel="stylesheet" href="/css/diagnostics.css">
</head>
<body>
	<div class="container">
		<p>Client IP: ${clientIp}</p>
		<p class="gray">Client Host: ${clientHost}</p>
		<p class="gray">HTTP/S?: ${httpType}</p>
		<p>URI: ${uriPath}<p>
		<p class="gray">Servlet Path Info: ${pathInfo}<p>
		<p class="gray">Query: ${fullQuery}</p>
		<br>
		<p class="green">Memory DAO NRE test: ${memberDaoCheckNull}</p>
		<p class="green">Member GET test: ${memberDaoCheckGet}</p>
		<p class="green">Opening DAO NRE test: ${openingDaoCheckNull}</p>
		<p class="green">Opening GET test: ${openingDaoCheckGet}</p>
		<p class="green">Appointment DAO NRE test: ${appointmentDaoCheckNull}</p>
		<p class="green">Appointment GET test: ${appointmentDaoCheckGet}</p>
		<br>
		<p>Attempt Manual SQL Connection Reset: <button onclick="resetSqlConnections()">Reset</button> <code id="sqlResetResultPre"> No reset underway </code></p>
		<#if paramMap?has_content>
		<h3>Full parameter dump:</h3>
		<table class="blue">
			<tr><th style="width: 20%;">Key</th><th>Value</th></tr>
			<#list paramMap as key, values>
			<#list values as value>
			<tr><td>${key}</td><td>${value}</td></tr>
			</#list>
			</#list>
		</table>
		<#else>
		<p class="blue">(No parameters provided.)</p>
		</#if>
		<h3>Full header dump:</h3>
		<table class="blue">
			<tr><th style="width: 20%;">Key</th><th>Value</th></tr>
			<#list headerItems as key, values>
			<#list values as value>
			<tr><td>${key}</td><td>${value}</td></tr>
			</#list>
			</#list>
		</table>
		<#list cookieItems as cookie>
		<h3>Cookie:</h3>
		<table class="blue">
			<tr><th style="width: 20%;">Key</th><th>Value</th></tr>
			<#list cookie as key, value>
			<tr><td>${key}</td><td>${value}</td></tr>
			</#list>
		</table>
		</#list>
	</div>
</body>
<script>
	const resetSqlConnections = () => {
		const resultPre = document.getElementById('sqlResetResultPre');
		
		if (confirm('Are you sure you want to try reset the DAOs and their connections?')) {
			resultPre.innerHTML = 'Resetting...';
			const xhr = new XMLHttpRequest();
			xhr.open('POST', '/');
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			xhr.onload = () => {
				if (xhr.status === 200) {
					resultPre.innerHTML = 'Reset successful.';
				} else {
					resultPre.innerHTML = 'Reset failed.';
				}
			};
			xhr.send('action=reset_daos&secret=makeLoveNotWar');
		}
	};
</script>
</html>