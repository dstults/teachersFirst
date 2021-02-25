<!DOCTYPE html>
<html>
<head>
	<title>Home page</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<h1>TeachersFirst Servlet</h1>

	<table border="1">
		<tr>
			<th>RecID</th><th>Name</th><th>Age</th>
		</tr>
		<#list members as member>
		<tr>
			<td>${member.recID}</td>
			<td>${member.name}</a></td>
			<td>${member.age}</td>
		</tr>
		</#list>
	</table><br />
</body>
</html>
