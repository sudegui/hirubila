<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
		
			<c:if test="${empty form.user.id}">
				<h1><fmt:message key="user.add.section.header"/></h1>	
			</c:if>
			<c:if test="${not empty form.user.id}">
				<h1><fmt:message key="user.edit.section.header"/></h1>
			</c:if>

			
		<form:form commandName="form" action="/${rc.locale.language}/user" method="POST">
			<div class="form_settings">
				<form:hidden path="user.id"/>
				<!-- IsAdmin checkbox -->
				<p><span><fmt:message key="user.fields.admin"/></span><form:checkbox path="user.admin" /></p>
				
				
				<form:errors path="mediationService" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.mediator.fields.mediationService"/></span>
					<form:select path="mediationService">
						<c:forEach items="${mediations}" var="mediation">
								<c:choose>
  									<c:when test="${fn:contains(mediation.members,form.user.id)}">
  										<form:option  value="${mediation.id}" label="${mediation.name}" selected="true"/>
  									</c:when>
  									<c:otherwise>
  										<form:option  value="${mediation.id}" label="${mediation.name}"/>
  									</c:otherwise>
  								</c:choose>
						</c:forEach>
    				</form:select>
    			</p>
				<!-- Name field -->
				<form:errors path="name" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.fields.name"/></span><form:input path="user.name" /></p>
				
				<!-- Email field -->
				<form:errors path="email" cssClass="ui-state-error"/>
				<p><span><fmt:message key="user.fields.email"/></span><form:input path="user.email" /></p>
				
				<!-- Password field -->
				<form:errors path="password" cssClass="ui-state-error"/>
				<p>
					<span><fmt:message key="user.fields.password"/></span>
						<c:if test="${empty form.user.password}">
	    					<form:password path="user.password" showPassword="true"/>
	    				</c:if>
	    				<c:if test="${not empty form.user.password}">
	    					<form:password path="user.password" showPassword="true" />
	    				</c:if>
				</p>

				<p style="padding-top: 15px"><span>&nbsp;</span>
				<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
				</p>
			</div>
		</form:form>
		