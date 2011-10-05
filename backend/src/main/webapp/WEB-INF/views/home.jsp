<!-- insert the page content here -->
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<h1><fmt:message key="welcome.message.1"/></h1>

<sec:authorize access="isAuthenticated()">
		<h3><fmt:message key="welcome.message.2"/> <sec:authentication property="principal.username" /></h3>
		<h4><fmt:message key="message.suggestion.info.2"/>.</h4>
</sec:authorize>

<sec:authorize access="!isAuthenticated()">
<p><fmt:message key="welcome.message.3"/></p>
	<a href="<c:url value='/${rc.locale.language}/login'/>">
		<img src="<c:url value='/static/img/login.jpg'/>" alt="login" align="right"/>
	</a>		
</sec:authorize>
	
