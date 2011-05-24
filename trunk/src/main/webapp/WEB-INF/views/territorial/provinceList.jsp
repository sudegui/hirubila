<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>
		<h1><fmt:message key="territorial.province.list.section.header"/> (<fmt:message key="message.total.upper"/> ${fn:length(provinces)})</h1>
		
		<table>
			<tr>
				<th><fmt:message key="province.fields.name"/></th>
				<th><fmt:message key="generic.options.message"/></th>
			</tr>
			<c:forEach items="${provinces}" var="province" varStatus="counter">
				<tr>
					<td>${province.name}</td>
					<td>
						<div id="menu-item-${counter.count}" class="options-menu">		
							<a class="expand-menu" href="#menu-item-${counter.count}"><fmt:message key="generic.options.message"/></a>
							<br>
							<ul class="content-options-menu" style="display: none">
								<li>
									<a href="<c:url value='/${rc.locale.language}/territorial/province/${province.id}/regions'/>" title="${province.name}">
									<fmt:message key="province.action.viewRegions"/>
									</a>
								</li>
								<li>
									<a href="<c:url value='/${rc.locale.language}/territorial/region/${province.id}/towns/'/>" title="${province.name}">
										<fmt:message key="territorial.region.actions.viewTowns"/>
									</a>
								</li>
								<li>
									<a class="edit-multilanguage-link" href="/territorial/province/edit/${province.id}" title="${province.name}">
										<fmt:message key="course.action.edit"/>
									</a>
								</li>
								<li>
									<a href="<c:url value='/${rc.locale.language}/territorial/province/delete/${province.id}'/>" title="${province.name}">
									<fmt:message key="course.action.delete"/>
									</a>
								</li>
							</ul>
						</div>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<table id="operations">
			<tr>
				<td>
					<a href="<c:url value='/${rc.locale.language}/territorial/province'/>" title='<fmt:message key="territorial.province.actions.add" />'>
						<fmt:message key="territorial.province.actions.add" />
					</a>
				</td>
			</tr>
		</table>
		<%@ include file="/WEB-INF/views/common/dialog/multilanguage-dialog.jsp"%>
		