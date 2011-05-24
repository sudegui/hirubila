<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>
		
		<h1><fmt:message key="course.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
		
		<%@ include file="/WEB-INF/views/common/paginator-tables.jsp"%>	
			
		<table style="width:100%; border-spacing:0;">
			<thead>
				<tr>
					<th class="sortable" order="title"><fmt:message key="course.fields.title"/></th>
					<th><fmt:message key="course.fields.information"/></th>
					<th><fmt:message key="generic.options.message"/></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${paginator.collection}" var="course" varStatus="counter">
				<tr>
					<td><a href="<c:url value='/${rc.locale.language}/course/detail/${course.id}'/>" title="${course.title}">${course.title}</a></td>
					<td class="description-collapse">${course.information.value}</td>
					<td>
						<div id="menu-item-${counter.count}" class="options-menu">		
							<a class="expand-menu" href="#menu-item-${counter.count}"><fmt:message key="generic.options.message"/></a>
							<br/>
							<ul class="content-options-menu" style="display: none">
								<sec:authorize ifAllGranted="ROLE_AUTOMATIC_MEDIATOR">
									<li>
										<a class="edit-multilanguage-link" href="/course/edit/${course.id}/" title="${course.title}">
											<fmt:message key="course.action.edit"/>
										</a>	
									</li>
								</sec:authorize>
								<sec:authorize ifAllGranted="ROLE_AUTOMATIC_MEDIATOR">
									<li>
										<a href="<c:url value='/${rc.locale.language}/course/delete/${course.id}'/>" title="${course.title}">
											<fmt:message key="course.action.delete"/>
										</a>
									</li>
								</sec:authorize>
								<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_AUTOMATIC_MEDIATOR">
									<li>
										<a href="<c:url value='/${rc.locale.language}/i18n/translations/${course.id}'/>" title="${course.title}">
											<fmt:message key="message.traductions"/>
										</a>
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
				<sec:authorize ifAllGranted="ROLE_AUTOMATIC_MEDIATOR">
					<td>
						<a href="<c:url value='/${rc.locale.language}/course/'/>" title="<fmt:message key='course.action.add'/>">
							<fmt:message key="course.action.add" />
						</a>
					</td>
				</sec:authorize>
			</tr>
		</table>
		<%@ include file="/WEB-INF/views/common/dialog/multilanguage-dialog.jsp"%>
