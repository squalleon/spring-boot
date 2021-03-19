<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <meta charset="utf-8">
        <title>환영합니다</title>
    </head>
    <body>
        <c:url value="/showMessage.html" var="messageUrl" />
        <a href="${messageUrl}">메시지를 보여주세요</a>
    </body>
</html>
