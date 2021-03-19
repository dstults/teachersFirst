
<!doctype html>
<html lang="en-US">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Darren's Page o' Diagnostics</title>
	<style>
		html {
			background-color: azure;
			word-break: break-all;
		}
		p { margin: 0; }
		p.extraTests { color: blue; }
		h3.extraTests { color: navy; }
		p:not(.extraTests) { text-shadow: 0 0 4px yellow; }
		div.container {
			margin: 5vw auto;
			max-width: 900px;
			border: 1px solid black;
			background-color: white;
			padding: 1vw;
			font-size: 1.5rem;
		}
		table {
			border-collapse: collapse;
			margin: 1em auto;
			color: blue;
			padding: 1vw;
			width: 90%;
			max-width: 880px;
		}
		td, th {
			border: 1px solid navy;
			text-align: center;
			padding: 3px;
			font-size: 0.75em;
		}
		tr:nth-child(even) { background-color: rgb(32, 32, 32, 0.2); }
	</style>
</head>
<body>
	<div class="container">
		<p>Client IP: ${clientIp}</p>
		<p class="extraTests">Client Host: ${clientHost}</p>
		<p class="extraTests">HTTP/S?: ${httpType}</p>
		<p>URI: ${uriPath}<p>
		<p class="extraTests">Servlet Path Info: ${pathInfo}<p>
		<p>Query: ${fullQuery}</p>
		<p class="extraTests">Memory DAO Check 1: ${memberDaoCheck1}</p>
		<p>Memory DAO Check 2: ${memberDaoCheck2}</p>
		<p class="extraTests">Opening DAO Check 1: ${openingDaoCheck1}</p>
		<p>Opening DAO Check 2: ${openingDaoCheck2}</p>
		<p class="extraTests">Appointment DAO Check 1: ${appointmentDaoCheck1}</p>
		<p>Appointment DAO Check 2: ${appointmentDaoCheck2}</p>
		<h3 class="extraTests">Full parameter dump:</h3>
		<#if paramMap?has_content>
		<table class="extraTests">
			<tr><th style="width: 20%;">Key</th><th>Value</th></tr>
			<#list paramMap as key, values>
			<#list values as value>
			<tr><td>${key}</td><td>${value}</td></tr>
			</#list>
			</#list>
		</table>
		<#else>
		<p class="extraTests">(No parameters provided.)</p>
		</#if>
		<h3 class="extraTests">Full header dump:</h3>
		<table class="extraTests">
			<tr><th style="width: 20%;">Key</th><th>Value</th></tr>
			<#list headerItems as key, values>
			<#list values as value>
			<tr><td>${key}</td><td>${value}</td></tr>
			</#list>
			</#list>
		</table>
	</div>
</body>
</html>