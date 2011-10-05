<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
		
			
			
		<h1><fmt:message key="user.mediator.add.section.header"/></h1>
			

			
		<form:form action='/${rc.locale.language}/user/mediator/save' commandName="mediator" method="POST">
			<div class="form_settings">
				<form:hidden path="id"/>
				<form:hidden path="user.id"/>
				<form:hidden path="user.email"/>
				<form:hidden path="user.password"/>
				
				
				<form:errors path="name" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.name"/></span><form:input path="name" /></p>
				
				<form:errors path="provider.feed" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.feed"/></span><form:input path="provider.feed" /></p>
				
				
				<h3><fmt:message key="user.mediator.fields.contact"/></h3>
				
				
				<form:errors path="provider.contactInfo.telephone" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.contact.phone"/></span><form:input path="provider.contactInfo.telephone" /></p>
				
				
				<form:errors path="provider.contactInfo.fax" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.contact.fax"/></span><form:input path="provider.contactInfo.fax" /></p>
				
				
				<form:errors path="provider.contactInfo.country" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.contact.country"/></span><form:input path="provider.contactInfo.country" /></p>
				
				
				<form:errors path="provider.contactInfo.city" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.contact.city"/></span><form:input path="provider.contactInfo.city" /></p>
				
				
				<form:errors path="provider.contactInfo.zipCode" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.contact.zipCode"/></span><form:input path="provider.contactInfo.zipCode" /></p>
				
				
				<form:errors path="provider.contactInfo.streetAddress" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.contact.streetAddress"/></span><form:input path="provider.contactInfo.streetAddress" /></p>
				
				<form:errors path="provider.contactInfo.email" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.contact.webSite"/></span><form:input path="provider.contactInfo.email" /></p>
				
				<form:errors path="name" cssClass="ui-state-error"/>
				<p><span></span></p>
				
				<form:errors path="name" cssClass="ui-state-error"/>
				<p><span></span></p>
				
				<p style="padding-top: 15px"><span>&nbsp;</span>
					<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
				</p>
					
			</div>
			  
       			
			
</form:form>