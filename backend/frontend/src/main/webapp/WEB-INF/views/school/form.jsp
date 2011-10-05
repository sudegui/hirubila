<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="school.add.section.header"/></h1>
		<form:form commandName="school" method="post">
			<div class="form_settings">
				<form:hidden path="id"/>
				<form:hidden path="externalId"/>
				<form:hidden path="provider"/>
				
				<%-- Name field --%>
				<form:errors path="name" cssClass="ui-state-error"/>
				<p><span><fmt:message key="school.fields.name"/></span><form:textarea path="name"/></p>
				
				<%-- Feed field --%>
				<form:errors path="feed" cssClass="ui-state-error"/>
				<p><span><fmt:message key="school.fields.feed"/></span><form:textarea path="feed"/></p>
    			
    			
    			<form:hidden path="created" />
       			<form:hidden path="updated" />
       			<p style="padding-top: 15px"><span>&nbsp;</span>
            	<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
            	</p>	
			</div>	
		</form:form>