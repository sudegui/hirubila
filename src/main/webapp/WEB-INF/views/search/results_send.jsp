<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><fmt:message key="search.results.send.title"/></title>
	<link href="<c:url value='/static/search/css/reset.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/static/search/css/estilo.css'/>" rel="stylesheet" type="text/css" />
	
	<link rel="stylesheet" href="<c:url value='/static/qaptcha/css/QapTcha.jquery.css'/>" type="text/css" />
	<script type="text/javascript" src="<c:url value='/static/qaptcha/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/qaptcha/js/jquery-ui.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/qaptcha/js/jquery.ui.touch.js'/>"></script> 
	<script type="text/javascript" src="<c:url value='/static/qaptcha/js/QapTcha.jquery.js'/>"></script>
	<script type="text/javascript">
    	$(document).ready(function(){
       		$('#QapTcha').QapTcha({disabledSubmit:true, txtLock:'<fmt:message key="qaptcha.message.txtLock"/>', txtUnlock:'<fmt:message key="qaptcha.message.txtUnlock"/>'});
    	});
	</script>
</head>

<body class="formu_envia_mail">

<form:form commandName="email" method="post" action="" cssClass="formu_shadow">
	<h1><fmt:message key="search.results.send.header"/></h1>
	
	<c:if test="${sent == false}">
		<form:errors path="fromName" cssClass="ui-state-error"/>
	    <label for="pormail_tunombre"><fmt:message key="search.results.send.yourName"/>:</label>
	    <form:input path="fromName" id="pormail_tunombre" />
	    
	    <form:errors path="from" cssClass="ui-state-error"/>
	    <label for="pormail_tumail"><fmt:message key="search.results.send.yourEmail"/>:</label>
	    <form:input path="from" id="pormail_tumail" />
	    
	    <form:errors path="toName" cssClass="ui-state-error"/>
	    <label for="pormail_sunombre"><fmt:message key="search.results.send.dstName"/>:</label>
	    <form:input path="toName" id="pormail_sunombre" />
	    
	    <form:errors path="to" cssClass="ui-state-error"/>
	    <label for="pormail_sumail"><fmt:message key="search.results.send.dstEmail"/>:</label>
	    <form:input path="to" id="pormail_sumail" />
	    
	    <form:errors path="resultLink" cssClass="ui-state-error"/>
	    <label for="pormail_enlace"><fmt:message key="search.results.send.link"/>:</label>
	    <form:input path="resultLink" id="pormail_enlace" readonly="true" />
	    
	    <form:errors path="content" cssClass="ui-state-error"/>
	    <label for="pormail_texto"><fmt:message key="search.results.send.text"/></label>
	    <form:textarea path="content" id="pormail_texto" cols="20" />
	   
	   <!-- Add this line in your form -->
       <div id="QapTcha"></div>
	   <input type="submit" value='<fmt:message key="search.results.send.action"/>' />
   	</c:if>
   	<c:if test="${sent == true}">
		<fmt:message key="search.results.send.success"/><br>
	</c:if>
</form:form>
</body>
</html>