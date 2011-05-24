<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>


<h1><fmt:message key="process.index.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
	
	
	<c:if test="${paginator.size > 0 && (paginator.pageStart < paginator.pageEnd)}">
	<table>
		<tr>
		<c:if test="${paginator.currentPage != paginator.pageStart}">
			<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage-1}&order=${order}&typeFilter=${filter.type}'/>">Anterior</a></td>
		</c:if>
		<c:forEach items='${paginator.pagesIterator}' var='page'>
	  						<c:if test="${page == paginator.currentPage}">
	  							<td>${page}</td>
	  						</c:if>
	  						<c:if test="${page != paginator.currentPage}">
	  							<td><a href="<c:url value='${paginator.urlBase}?page=${page}&order=${order}&typeFilter=${filter.type}'/>" title="Schools">${page}</a></td>
	  						</c:if>
					</c:forEach>
					<c:if test="${paginator.currentPage != paginator.pageEnd}">
			<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage+1}&order=${order}&typeFilter=${filter.type}'/>">Siguiente</a></td>
		</c:if>
		</tr>
	</table>	
	</c:if>

	<form:form commandName="filter" action="/${rc.locale.language}/summary/crontaskreport">
		<form:errors path="type" cssClass="ui-state-error"/>
		<p>
			<span><fmt:message key="user.mediator.fields.updateMethod"/></span>
			<form:select path="type">
	    	<form:options items="${CronTaskReportFilterForm.TYPE}" itemLabel="displayName"/>
	    </form:select>
	    
	    <input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
		</p>
	</form:form>
	
	<table style="width:100%; border-spacing:0;">
		<thead>
		<tr>
			<th><fmt:message key="process.fields.id"/></th>
			<th><fmt:message key="process.fields.type"/></th>
			<th><fmt:message key="process.fields.description"/></th>
			<th><fmt:message key="process.fields.date"/></th>
			<th><fmt:message key="process.fields.result"/></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${paginator.collection}" var="cronTask" varStatus="counter">
		<tr>	
			<td>${cronTask.id}</td>
			<td>${cronTask.type.displayName}</td>
			<td>${cronTask.description}</td>
			<td><fmt:formatDate value="${cronTask.date}" type="date" pattern="dd-MM-yyyy hh:mm:ss" /></td>
			<td>${cronTask.result}</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>
		
