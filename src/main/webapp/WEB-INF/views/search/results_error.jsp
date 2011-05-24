<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><fmt:message key="search.results.error.title"/></title>
	<link href="<c:url value='/static/search/css/reset.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/static/search/css/estilo.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/static/search/js/shadowbox.js'/>"></script>
	<script type="text/javascript">
	function close() {
		parent.Shadowbox.close();
	}
	</script>
</head>

<body class="formu_envia_mail avisa_error">
<form:form commandName="inbox" method="post" action="" class="formu_shadow">
    <h1><fmt:message key="search.results.error.header"/></h1>
    
    <c:if test="${sent == false}">
    <form:errors path="name" cssClass="ui-state-error"/>
	<label for="pormail_tunombre"><fmt:message key="search.results.error.name"/>:</label>  
    <form:input path="name"/>
    
    <form:errors path="from" cssClass="ui-state-error"/>
    <label for="pormail_tumail"><fmt:message key="search.results.error.email"/>:</label>
    <form:input path="from"/>
  
  	<form:errors path="content" cssClass="ui-state-error"/>
    <label for="pormail_texto"><fmt:message key="search.results.error.text"/>:</label>
    <form:textarea path="content" id="pormail_texto" cols="20"/>
   
   	<input type="submit" onclick="close();" value='<fmt:message key="search.results.error.action"/>'/>
   	</c:if>
   	
   	<c:if test="${sent == true}"><fmt:message key="search.results.error.success"/><br></c:if>
</form:form> 
</body>
</html>