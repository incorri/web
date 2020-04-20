<html>
<head>
    <title>Title</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
</head>

<div class="form-style-2">
    <div>
        <form method="post" enctype="multipart/form-data">
            <input type="file" name="fileName">
            <button type="submit">Загрузить</button>
        </form>
    </div>
    <div>
        <#if newFilename??>
            ${newFilename}
        </#if>
    </div>
    <table>
        <tr>
            <th>Название</th>
            <th>Автор</th>
        </tr>
        <#list booksFromServer as book>
            <tr>
                <td>${book.titleName}</td>
                <td>${book.authorName}</td>
            </tr>
        </#list>
    </table>
</div>
</body>
</html>