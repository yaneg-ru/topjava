<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Meals</title>
</head>

<style>
    tr, th, td {
        border: 1px solid black;
    }
</style>

<body>

<h1><a href="index.html">Home</a></h1>
<hr>

<h2>List of dynamic meals (optional implementation)</h2>

<table>
    <tbody>
    <tr>
        <th>Data</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    <c:forEach items="${requestScope.listMealsDynamic}" var="meal">
        <tr style="color: ${meal.excess? 'red': 'green'}">
            <td><c:out value="${requestScope.dateTimeFormatter.format(meal.dateTime)}"></c:out></td>
            <td><c:out value="${meal.description}"></c:out></td>
            <td><c:out value="${meal.calories}"></c:out></td>
            <td><a href="meals?action=edit&Id=${meal.id}">Edit</a></td>
            <td><a href="meals?action=delete&Id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
    <th colspan="5"><a href="meals?action=add">Add meal</a></th>
    </tbody>
</table>

</body>
</html>