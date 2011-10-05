<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<%-- Miembros del servicio de mediación en curso --%>
<h3><fmt:message key="mediation.members.message"/>:</h3>
<ul>
	<c:forEach items="${mediation.members}" var="member">
		<li><a href="<c:url value="/${rc.locale.language}/user/detail/${member.id}/"/>" >${member.name} (${member.email})</a></li>
	</c:forEach>		
</ul>
			
<h1><fmt:message key="mediation.add.section.header"/></h1>

<form:form commandName="mediation" method="post">

		
<div class="form_settings">
	
	<form:hidden path="mediationService.id"/>
	<form:hidden path="provider.id"/>
	<form:hidden path="provider.name"/>
	
	<h3><fmt:message key="basicInfo.title"/></h3>
	<!-- Province field -->
	<p><span><fmt:message key="user.mediator.fields.province"/></span>
		<form:select path="mediationService.province">
			<form:options items="${provinces}" itemValue="id" itemLabel="name"/>
		</form:select>
	</p>
		<!-- Name field -->
	<form:errors path="name" cssClass="ui-state-error"/>
	<p><span><fmt:message key="user.mediator.fields.name"/></span><form:textarea path="mediationService.name"/></p>
	
	<!-- Entity field -->
	<form:errors path="entity" cssClass="ui-state-error"/>
	<p><span><fmt:message key="user.mediator.fields.entity"/></span><form:textarea path="mediationService.entity"/></p>		
	
	<!-- HasFeed checkbox -->
	<p><span><fmt:message key="user.fields.hasFeed"/></span><form:checkbox path="mediationService.hasFeed" id="has_feed"/></p>
	
	<div style="display:none" id="feed_info">

		<h3><fmt:message key="mediationService.fields.feedInformation"/></h3>
		<p></p>
		<form:errors path="feed" cssClass="ui-state-error"/>
		<p><span><fmt:message key="user.mediator.fields.feed"/></span><form:input path="provider.feed"/></p>
		<p>
			<span><fmt:message key="user.mediator.fields.updateMethod"/></span>
			<form:select path="mediationService.updateMethod">
	    	<form:options items="${MediationService.UPDATE_METHOD}" itemLabel="displayName"/>
	    </form:select>
		</p>
	</div>
	
	<h3><fmt:message key="contactInfo.title"/></h3>
	
	<p><span><fmt:message key="contactInfo.fields.telephone"/></span><form:input path="mediationService.contactInfo.telephone" /></p>

	<p><span><fmt:message key="contactInfo.fields.fax"/></span><form:input path="mediationService.contactInfo.fax" /></p>
				
	<p><span><fmt:message key="contactInfo.fields.country"/></span><form:input path="mediationService.contactInfo.country" /></p>
				
	<p><span><fmt:message key="contactInfo.fields.city"/></span><form:input path="mediationService.contactInfo.city" /></p>
				
	<p><span><fmt:message key="contactInfo.fields.zipCode"/></span><form:input path="mediationService.contactInfo.zipCode" /></p>
				
	<p><span><fmt:message key="contactInfo.fields.streetAddress"/></span><form:input path="mediationService.contactInfo.streetAddress" /></p>
				
	<p><span><fmt:message key="contactInfo.fields.email"/></span><form:input path="mediationService.contactInfo.email" /></p>
	
	<p><span><fmt:message key="contactInfo.fields.webSite"/></span><form:input path="mediationService.contactInfo.webSite" /></p>
	
	<h3><fmt:message key="geoLocation.title"/></h3>
	<p><span><fmt:message key="geolocation.fields.latitude"/></span><form:input path="latitude"/></p>
	<p><span><fmt:message key="geolocation.fields.longitude"/></span><form:input path="longitude"/></p>
	
	
	<p style="padding-top: 15px"><span>&nbsp;</span>
	<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
	</p>
				
</div>
</form:form>

<script>
	
$(document).ready(function(){
	$("#has_feed").click(function(){
   		if ($("#has_feed").attr("checked")){
   	         $("#feed_info").css("display", "block");
   	     }else{
   	         $("#feed_info").css("display", "none");
   	     }
     });
  	 //Init
   	 if ($("#has_feed").attr("checked")){
  	  	$("#feed_info").css("display", "block");
  	 }else{
  	    $("#feed_info").css("display", "none");
  	 }
}); 
</script>