<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>


<h1><fmt:message key="process.index.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
	
	
	<%@ include file="/WEB-INF/views/common/paginator-tables.jsp"%>

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
			
			<th><fmt:message key="process.fields.type"/></th>
			<th><fmt:message key="process.fields.description"/></th>
			<th><fmt:message key="process.fields.date"/></th>
			<th><fmt:message key="process.fields.result"/></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${paginator.collection}" var="cronTask" varStatus="counter">
		<tr>	
			
			<td>${cronTask.type.displayName}</td>
			<c:choose>
				<c:when test="${cronTask.type eq 'PROVIDER_FEED'}">
					<td>
					<a href="<c:url value='/${rc.locale.language}/provider/${cronTask.object_id}/summary/'/>" title="Resumen de todos los procesos">
								${cronTask.description}
					</a>
					</td>
					
				</c:when>
				<c:otherwise>
					<td>${cronTask.description}</td>	
				</c:otherwise>
			</c:choose>
			
			<td><fmt:formatDate value="${cronTask.date}" type="date" pattern="dd-MM-yyyy hh:mm:ss" /></td>
			<td>${cronTask.result}</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>
		
