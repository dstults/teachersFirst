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
            <#list teachersFirsts as teachersFirst>
            <tr>
                <td>${teachersFirst.recID}</td>
                <td>${teachersFirst.name}</a></td>
                <td>${teachersFirst.age}</td>
            </tr>
            </#list>
        </table><br />
    </body>
</html>
