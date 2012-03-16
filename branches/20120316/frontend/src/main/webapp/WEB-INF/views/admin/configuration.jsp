<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="configuration.index.section.header"/></h1>

<form:form commandName="configuration" method="post">
	<form:hidden path="id"/>
	<form:hidden path="languages"/>
	<form:hidden path="searchEngine"/>
	
	<div class="form_settings">
						
		<p><span><fmt:message key="configuration.fields.pageSize"/></span>	
			<form:input path="pageSize"/>
			<fmt:message key="configuration.fields.pageSize.range"/>
		</p>
			
		<p><span><fmt:message key="configuration.fields.searchUri"/></span>
			<form:input path="searchUri"/>
		</p>
		
		<p><span><fmt:message key="configuration.fields.searchBaseCollectionName"/></span>
			<form:input path="searchBaseCollectionName"/>
		</p>
		
		<p style="padding-top: 15px"><span>&nbsp;</span><input class="submit" type="submit" value="<fmt:message key='configuration.actions.save'/>" title="<fmt:message key='generic.submit.message'/>"/></p>
	</div>
	<a href='<c:url value="/${rc.locale.language}/dashboard/admin/configuration/reset"/>'><fmt:message key="configuration.actions.reset"/></a>
</form:form>
