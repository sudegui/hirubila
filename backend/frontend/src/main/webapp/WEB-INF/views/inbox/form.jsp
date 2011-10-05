<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

		<h1><fmt:message key="suggestion.add.section.header"/></h1>
		
		<c:if test="${not empty message}">
			<fmt:message key="suggestion.message.add.succesful"/>
		</c:if>
		<c:if test="${empty message}">
			<form:form commandName="inbox" method="post">
				<div class="form_settings">
					<form:hidden path="from"/>
					
					<!-- Name field -->
				<form:errors path="type" cssClass="ui-state-error"/>
				<p>
					<span><fmt:message key="suggestion.fields.type"/></span>
					<form:select path="type">
	    				<form:options items="${Inbox.TYPE}"/>
	    			</form:select>
	    		</p>
				
				<form:errors path="content" cssClass="ui-state-error"/>
				<p><span><fmt:message key="suggestion.fields.content"/></span><form:textarea path="content" rows="15"/></p>
				
				<p style="padding-top: 15px"><span>&nbsp;</span>
            	<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
            	</p>
				
				</div>
			</form:form>
		</c:if>