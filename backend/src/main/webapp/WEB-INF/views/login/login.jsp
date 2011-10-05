<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	
	<h1><fmt:message key="login.form.title"/></h1>
		
		<c:choose>
			<c:when test="${not empty loggedUser}">
				<fmt:message key="login.logged" />
			</c:when>
			<c:otherwise>
				<c:if test='${not empty param.login_error}'>
					<div id='login_errors'>
						<fmt:message key='login.error.advice' />.
						<fmt:message key='login.error.reason' />: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" /> 
					</div>
				</c:if>
				
				<form name="login_form" action="<c:url value='j_spring_security_check'/>" method="POST">
					<div class="form_settings">
					<p>
						<span><fmt:message key="login.form.username"/></span>
						<input type='text' id='username' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/> </c:if>'/>
					</p>
					<p>
						<span><fmt:message key="login.form.password"/></span> 
						<input type='password' id='password' name='j_password'/>
					</p>
					<p>
						<a href="<c:url value='/${rc.locale.language}/login/recovery'/>"><fmt:message key="login.action.recovery" /></a>
					</p>
					<p style="padding-top: 15px"><span>&nbsp;</span>
						<input type='submit' class="submit" name='submit' value="<fmt:message key='login.action.login'/>"/> 
					</p>
					</div>
				</form>
			</c:otherwise>
		</c:choose>
