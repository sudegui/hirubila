<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>

<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
		
		<h1><fmt:message key="suggestion.inbox.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
		
		<%-- TABLE PAGINATION --%>
		<c:if test="${paginator.size > 0 && (paginator.pageStart < paginator.pageEnd)}">
		<table>
			<tr>
			<c:if test="${paginator.currentPage != paginator.pageStart}">
				<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage-1}&order=${order}&readedFilter=${filter.readed}&userFilter=${filter.user}'/>">Anterior</a></td>
			</c:if>
			<c:forEach items='${paginator.pagesIterator}' var='page'>
			<c:if test="${page == paginator.currentPage}">
				<td>${page}</td>
			</c:if>
			<c:if test="${page != paginator.currentPage}">
				<td><a href="<c:url value='${paginator.urlBase}?page=${page}&order=${order}&readedFilter=${filter.readed}&userFilter=${filter.user}'/>" title="Schools">${page}</a></td>
			</c:if>
			</c:forEach>
			<c:if test="${paginator.currentPage != paginator.pageEnd}">
				<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage+1}&order=${order}&readedFilter=${filter.readed}&userFilter=${filter.user}'/>">Siguiente</a></td>
			</c:if>
			</tr>
		</table>	
		</c:if>
	
		<%-- TABLE FILTER --%>
		<form:form commandName="filter" action="${paginator.urlBase}">
			<form:errors path="type" cssClass="ui-state-error"/>
			<p>
				<span><fmt:message key="suggestion.fields.filter.user"/></span>
				<form:select path="user">
				<form:option value=""/>
		    	<form:options items="${InboxFilterForm.USER}" itemLabel="displayName"/>
		    	</form:select>
		    	<span><fmt:message key="suggestion.fields.filter.noreaded"/></span>
		    	<form:checkbox path="readed"/>
		    <input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
			</p>
		</form:form>
		
		<table style="width:100%; border-spacing:0;">
			<tr>
				<th><fmt:message key="suggestion.fields.type"/></th>		
				<th><fmt:message key="suggestion.fields.from"/></th>
				<th><fmt:message key="suggestion.fields.received"/></th>
				<th><fmt:message key="generic.options.operations"/></th>
			</tr>
			<c:forEach items="${paginator.collection}" var="message" varStatus="counter">
				<tr>		
					<td>${message.type}</td>	
					<td>${message.from}</td>
					<td><fmt:formatDate value="${message.created}" type="date" pattern="dd-MM-yyyy" /></td>
					<td>
					<div id="menu-item-${counter.count}" class="options-menu">		
					<a class="expand-menu" href="#menu-item-${counter.count}"><fmt:message key="generic.options.message"/></a>
					<br>
					<ul class="content-options-menu" style="display: none">
		          		<li>
		          			<a href="<c:url value='/${rc.locale.language}/suggestion/detail/${message.id}'/>" title="${message.from}">
							<fmt:message key="suggestion.action.detail"/>
						</a>
						</li>
	        			<li>
	        				<a href="<c:url value='/${rc.locale.language}/suggestion/delete/${message.id}'/>" title="${message.from}">
							<fmt:message key="suggestion.action.delete"/>
						</a>
						</li>
	   				</ul>	
	   				</div>
					</td>
				</tr>
			</c:forEach>
		</table>
	
		