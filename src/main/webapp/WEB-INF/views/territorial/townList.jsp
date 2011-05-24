<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>	
			
		<h1><fmt:message key="territorial.town.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
		
		<%@ include file="/WEB-INF/views/common/paginator-tables.jsp"%>	
				
		<table>
			
			<tr>
				<th><fmt:message key="territorial.town.fields.name"/></th>
				<th><fmt:message key="territorial.town.fields.region"/></th>
				<th><fmt:message key="territorial.town.fields.province"/></th>
				<th><fmt:message key="generic.options.message"/></th>
			</tr>
			
			<c:forEach items="${paginator.collection}" var="town" varStatus="counter">
				<tr>
					<td>${town.name} - (${town.upperName})</td>
					<td>${regionsMap[rc.locale.language][town.region].name}</td>
					<td>${provincesMap[rc.locale.language][town.province].name}</td>
					<td>
						<div id="menu-item-${counter.count}" class="options-menu">		
							<a class="expand-menu" href="#menu-item-${counter.count}"><fmt:message key="generic.options.message"/></a>
							<br>
							<ul class="content-options-menu" style="display: none">
								<li><a class="edit-multilanguage-link" href="/territorial/town/edit/${town.id}" title="${town.name}">
							<fmt:message key="course.action.edit"/>
						</a></li>
								<li><a href="<c:url value='/${rc.locale.language}/territorial/town/delete/${town.id}'/>" title="${town.name}">
							<fmt:message key="course.action.delete"/>
						</a></li>
							</ul>
						</div>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<table id="operations">
			<tr>
				<td>
					<a href="<c:url value='/${rc.locale.language}/territorial/town/'/>" title='<fmt:message key="territorial.town.actions.add" />'>
						<fmt:message key="territorial.town.actions.add" />
					</a>
				</td>
			</tr>
		</table>
		<%@ include file="/WEB-INF/views/common/dialog/multilanguage-dialog.jsp"%>