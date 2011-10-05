<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="territorial.region.add.section.header"/></h1>

<form:form commandName="region" method="post">
	<div class="form_settings">
		<form:hidden path="id"/>
		
		<form:errors path="province" cssClass="error"/>
		<p><span><fmt:message key="territorial.region.fields.province"/></span>
			<form:select path="province">
				<form:options items="${provinces}" itemValue="id" itemLabel="name" />
			</form:select>
		</p>
    					
		<form:errors path="name" cssClass="error"/>
		<p><span><fmt:message key="territorial.region.fields.name"/></span>
			<form:input path="name"/>
		</p>

		<p style="padding-top: 15px"><span>&nbsp;</span>
			<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
		</p>
	</div>
</form:form>
