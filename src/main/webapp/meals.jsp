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
            <c:if test="${meal.excess}"><tr style="color: red"></c:if>
            <c:if test="${!meal.excess}"><tr style="color: green"></c:if>
            <td><c:out value="${dateTimeFormatter.format(meal.dateTime)}"></c:out></td>
            <td><c:out value="${meal.description}"></c:out></td>
            <td><c:out value="${meal.calories}"></c:out></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>