<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <tbody>
    <tr>
        <th>Data</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${requestScope.listMeals}" var="meal">
        <tr>
            <td><c:out value="${meal.date}"></c:out></td>
            <td><c:out value="${meal.description}"></c:out></td>
            <td><c:out value="${meal.calories}"></c:out></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>