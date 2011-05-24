<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div id="logo">
		<div id="topbar">
		<ul id="login">	
		<sec:authorize access="isAuthenticated()">
			<li>
				<fmt:message key="user.message.welcome" />: <sec:authentication property="principal.username" />
				<a href="<c:url value="/j_spring_security_logout"/>">
					<fmt:message key="action.user.logoff"/></a>
			</li>
		</sec:authorize>
		<sec:authorize access="!isAuthenticated()">
			<li>
				<a href="<c:url value='/${rc.locale.language}/login'/>"><fmt:message key="action.user.login"/></a>
			</li>
		</sec:authorize>
		
		</ul>
		<ul id="languages">
			<c:forEach items="${langs}" var="lang">
				<li><a href='<c:url value="/${lang.key}/"/>'><fmt:message key="${lang.key}"/>(${lang.key})</a></li>
			</c:forEach>
		</ul>
	</div>
        <div id="logo_text">
          <!-- class="logo_colour", allows you to change the colour of the text -->
          <h1><a href="<c:url value='/${rc.locale.language}/'/>">Hiru<span class="logo_colour">Bila</span></a></h1>
          <h2><fmt:message key="header.title.description"/>....</h2>
        </div>
</div>
