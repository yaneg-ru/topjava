<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h1><a href="index.html">Home</a></h1>
<hr>
<h2>List of meals (base implementation)</h2>

<table style="border: 1px solid black">
    <tbody >
    <tr>
        <th >Data</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${requestScope.listMealsBase}" var="meal">
        <tr style="color: ${meal.excess? 'red': 'green'}">
            <td style="border: 1px solid black"><c:out value="${requestScope.dateTimeFormatter.format(meal.dateTime)}"></c:out></td>
            <td style="border: 1px solid black"><c:out value="${meal.description}"></c:out></td>
            <td style="border: 1px solid black"><c:out value="${meal.calories}"></c:out></td>
        </tr>
    </c:forEach>
    </tbody>
</table>


<h2>List of dynamic meals (optional implementation)</h2>

<table style="border: 1px solid black">
    <tbody>
    <tr>
        <th>Data</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    <c:forEach items="${requestScope.listMealsDynamic}" var="meal">
        <tr style="color: ${meal.excess? 'red': 'green'}">
            <td style="border: 1px solid black"><c:out value="${requestScope.dateTimeFormatter.format(meal.dateTime)}"></c:out></td>
            <td style="border: 1px solid black"><c:out value="${meal.description}"></c:out></td>
            <td style="border: 1px solid black"><c:out value="${meal.calories}"></c:out></td>
            <td style="border: 1px solid black"><a href="meals?action=edit&mealId=${meal.id}">Edit</a></td>
            <td style="border: 1px solid black"><a href="meals?action=delete&mealId=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
    <th colspan="5"><a href="meals?action=add">Add meal</a></th>
    </tbody>
</table>

</body>
</html>