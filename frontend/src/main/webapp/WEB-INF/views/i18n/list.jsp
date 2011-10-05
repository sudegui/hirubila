<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
		
		<h1><fmt:message key="i18.list.section.header"/> </h1>
		
		<hr>
		
			Total: ${total}
		
			<c:forEach items="${translations}" var="translation">
				<h2>Language: <c:out value="${translation.key}"/></h2>
				<table border="1">
					<thead>
						<tr>
							<th>Content id</th>
							<th>Object class</th>
							<th>Field name</th>
							<th>Field value</th>
							<th>Lang</th>
							<th>Operations</th>
						</tr>
					</thead>
   					<c:forEach items="${translation.value}" var="entry">
   						<tr>
   							<td>${entry.contentId}</td>
   							<td>${entry.objectClass}</td>
   							<td>${entry.fieldKey}</td>
   							<td>${entry.fieldValue.value}</td>
   							<td>${entry.lang}</td>
   							<td><a href="<c:url value='/${rc.locale.language}/i18n/delete/${entry.id}'/>" title="${entry.id}">
									<fmt:message key="school.action.delete"/>
								</a>
							</td>
   						</tr>
   					</c:forEach>
   				</table>
			</c:forEach>
