<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
		
		<h1><fmt:message key="i18.list.section.header"/></h1>
		
			
	<div id="accordion">
		<c:forEach items="${translations}" var="translation">
			<h3><c:out value="${translation.key}"/></h3>
			<div>
				<table style="width:100%; border-spacing:0;">
					<thead>
						<tr>
							<th><fmt:message key="i18n.fields.name"/></th>
							<th><fmt:message key="i18n.fields.value"/></th>
						</tr>
					</thead>
   					<c:forEach items="${translation.value}" var="entry">
   						<tr>  							
   							<td>${entry.fieldKey}</td>
   							<td>${entry.fieldValue.value}</td>
   						</tr>
   					</c:forEach>
   				</table>
			</div>
		</c:forEach>
	</div>
	
	
<script>
	$(function() {
		$( "#accordion" ).accordion();
	});
</script>

