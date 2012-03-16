<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
		
	<h1><fmt:message key="login.recovery.section.header"/></h1>
	
	<form:form commandName="recovery" method="POST">
		<fmt:message key="login.recovery.message.email" />
		<div class="form_settings">
			<form:errors path="email" cssClass="ui-state-error"/>
			<p><span><fmt:message key="user.fields.email"/></span><form:input path="email"/></p>
			<p style="padding-top: 15px"><span>&nbsp;</span>
	          	<input class="submit" type="submit" value="<fmt:message key='user.action.recovery'/>" title="<fmt:message key='user.action.recovery'/>"/>
	          	</p>
		</div> 
	</form:form>
