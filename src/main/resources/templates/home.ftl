<!DOCTYPE html>
<html>
    <head>
        <title>Home page</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Skeleton Servlet</h1>

        <table border="1">
            <tr>
                <th>RecID</th><th>Name</th><th>Age</th>
            </tr>
            <#list skeletons as skeleton>
            <tr>
                <td>${skeleton.recID}</td>
                <td>${skeleton.name}</a></td>
                <td>${skeleton.age}</td>
            </tr>
            </#list>
        </table><br />
    </body>
</html>
