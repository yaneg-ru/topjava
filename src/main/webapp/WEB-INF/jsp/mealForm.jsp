<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <jsp:include page="fragments/headTag.jsp"/>
</head>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h2>
        <c:choose>
            <c:when test="${action = 'create'}">
                <s:message code="meal.create"/>
            </c:when>
            <c:otherwise>
                <s:message code="meal.edit"/>
            </c:otherwise>
        </c:choose>
    </h2>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><s:message code="meal.date_time"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><s:message code="meal.desc"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><s:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><s:message code="app.save"/></button>
        <button onclick="window.history.back()" type="button"><s:message code="app.cancel"/></button>
    </form>
</section>
</body>
</html>
