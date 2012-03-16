<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1><fmt:message key="suggestion.detail.section.header"/></h1>
		
<fmt:message key="suggestion.message.inresponse"/>:

<fmt:message key="suggestion.fields.from"/>  : ${inboxReq.from} <br/>
<fmt:message key="suggestion.fields.type"/>  : ${inboxReq.type} <br/>
<fmt:message key="suggestion.fields.content" /> : ${inboxReq.content} <br/>
<fmt:message key="suggestion.fields.userType" /> : ${inboxReq.user} <br/>

<fmt:message key="suggestion.message.response"/>:
<br/>
<form:form commandName="inbox" method="post">
	<div class="form_settings">
		<form:hidden path="from"/>

	<p><span><fmt:message key="suggestion.fields.content"/></span><form:textarea path="content" rows="15"/></p>
	
	<p style="padding-top: 15px"><span>&nbsp;</span>
  	<input class="submit" type="submit" value="<fmt:message key='suggestion.action.add'/>" title="<fmt:message key='suggestion.action.add'/>"/>
  	</p>
	</div>
</form:form>