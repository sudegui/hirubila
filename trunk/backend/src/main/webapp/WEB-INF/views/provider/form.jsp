<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
		<h1><fmt:message key="provider.add.section.header"/></h1>
		
		
		 <form:form commandName="provider" method="post">
		 	<form:hidden path="id"/>
		 	<form:hidden path="mediationService"/>
          	<div class="form_settings">        
          		<h3><form:errors path="name" cssClass="ui-state-error"/></h3>
            	<p><span><fmt:message key="provider.fields.name"/></span><form:input path="name"/></p>
            	<h3><form:errors path="feed" cssClass="ui-state-error"/></h3>
            	<p><span><fmt:message key="provider.fields.feed"/></span><form:input path="feed"/></p>          	
            	<p style="padding-top: 15px"><span>&nbsp;</span>
            	<h3><form:errors path="regulated" cssClass="ui-state-error"/></h3>
            	<p><span><fmt:message key="provider.fields.regulated"/></span><form:checkbox path="regulated"/></p>
            	<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
            	</p>
          </div>
        </form:form>