<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="tutorial.section.header"/></h1>
       
<sec:authorize ifAllGranted="ROLE_AUTOMATIC_MEDIATOR">
	<ul>
		<!--  
		<li><a href="#" title="<fmt:message key='tutorial.add.school'/>"><fmt:message key="tutorial.add.school"/></a></li>
		<li><a href="#" title="<fmt:message key='tutorial.add.course'/>"><fmt:message key="tutorial.add.course"/></a></li>
		-->
	</ul>
</sec:authorize>
<sec:authorize ifAllGranted="ROLE_MANUAL_MEDIATOR">
	<ul>
		<li><a href="http://www.youtube.com/watch?v=pY_XoN-wmnE" title="<fmt:message key='tutorial.add.school'/>" target="_blank"><fmt:message key="tutorial.add.school"/></a></li>
		<li><a href="http://www.youtube.com/watch?v=65eRIEuZNvc" title="<fmt:message key='tutorial.add.course'/>" target="_blank"><fmt:message key="tutorial.add.course"/></a></li>
	</ul>
</sec:authorize>
			
        
        
			
        