<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Meal</title>
</head>
<body>
<h1><a href="index.html">Home</a></h1>
<hr>
<h2>Meal</h2>

<form method="POST" action='meals?action=edit' name="frmAddMeal">
    <label>
        Data and time :
        <input type="datetime-local" name="dateTime"
               value="${requestScope.meal!=null?requestScope.meal.dateTime:''}"/>
    </label>
    <br>
    <label>
        Description of meal :
        <input type="text" name="description"
               value="${requestScope.meal!=null?requestScope.meal.description:''}"/>
    </label>
    <br />
    <label>
        Calories :
        <input type="text" name="calories"
               value="${requestScope.meal!=null?requestScope.meal.calories:0}"/>
    </label>
    <br />
    <input type="hidden" name="idFromAddEditForm" value="${requestScope.meal!=null?requestScope.meal.id:''}" />
    <input type="submit" value="Submit" />
</form>

</body>
</html>