<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
		
		<h1><fmt:message key="user.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
		
	    <%@ include file="/WEB-INF/views/common/paginator-tables.jsp"%>	
		
		<table style="width:100%; border-spacing:0;">
			<thead>
				<tr>
					<th class="sortable" order="name"><fmt:message key="user.fields.name"/></th>
					<th class="sortable" order="email"><fmt:message key="user.fields.email"/></th>
					<th class="sortable" order="admin"><fmt:message key="user.fields.admin"/></th>
					<th><fmt:message key="user.actions"/></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${paginator.collection}" var="user" varStatus="counter">
				<tr>
					<td>
						<a href="<c:url value="/${rc.locale.language}/user/detail/${user.id}/"/>" >${user.name}</a>
					</td>
					<td>
						${user.email}
					</td>
					<td>${user.admin}</td>
					<td>
						<div id="menu-item-${counter.count}" class="options-menu">		
							<a class="expand-menu" href="#menu-item-${counter.count}"><fmt:message key="generic.options.message"/></a>
							<br>
							<ul class="content-options-menu" style="display: none">
								<sec:authorize ifAllGranted="ROLE_ADMIN">
								<li>
									<a href="<c:url value="/${rc.locale.language}/user/edit/${user.id}/"/>" ><fmt:message key ="user.action.edit" /></a>
								</li>
								
								<li>
									<a href="<c:url value="/${rc.locale.language}/user/delete/${user.id}/"/>" ><fmt:message key ="user.action.delete" /></a>
								</li>
								</sec:authorize>
						</ul>
						</div>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
		
		<table id="operations">
			<tr>
				<td>
					<a href='<c:url value='/${rc.locale.language}/user/'/>' title='<fmt:message key="user.action.add" />'>
						<fmt:message key="user.action.add"/>
					</a>
				</td>
			</tr>
		</table>