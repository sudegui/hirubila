<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<%-- TESTING TERRITORIAL FILTER --%>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#provinces").change(function(){
					$("#regions").empty();
					$("#towns").empty();
					var provinceId = $("#provinces").val();
					var url = '/${rc.locale.language}/territorial/region/ajax/regionsByProvince?provinceId='+provinceId;
					$.getJSON(url, function(data){	
						$.each(data, function(i, val){
							if(i == 0) {
								$("#regions").append('<option value="">---</option>');
							}
							if($("#region").val() == val.id) {
								$("#regions").append('<option selected="true" value="' + val.id + '">' + val.name +'</option>');
							} else {
								$("#regions").append('<option value="' + val.id + '">' + val.name +'</option>');
							}

							if(i == data.length-1) $("#regions").change();
						});	
						
					 });
				});

				$("#regions").change(function(){
					$("#towns").empty();
					var regionId = $("#regions").val();
					if(regionId != null && regionId != '') {
						var url = '/${rc.locale.language}/territorial/town/ajax/townsByRegion?regionId='+regionId;
						$.getJSON(url, function(data){	
							$.each(data, function(i, val){
								if(i == 0) {
									$("#towns").append('<option value="">---</option>');
								}
								if($("#town").val() == val.id) {
									$("#towns").append('<option selected="true" value="' + val.id + '">' + val.name +'</option>');
								} else {
									$("#towns").append('<option value="' + val.id + '">' + val.name +'</option>');
								}
							});	
							
						 });
					}
				});

				if($("#province").val() != '') {
					$("#provinces").change();
				}
			});
			
		</script>
		<div id="filter">
		   <form:form action="${paginator.urlBase}" commandName="filterForm" method="post">
		   <input type="hidden" id="province" value="${filterForm.provinceId}"/>
		   <input type="hidden" id="region" value="${filterForm.regionId}"/>
		   <input type="hidden" id="town" value="${filterForm.townId}"/>
		   
		   	<form:errors path="provinceId" cssClass="ui-state-error"/>
			<span><fmt:message key="territorial.fields.province"/></span>:
			 <select id="provinces" name="provinceId" value="${filterForm.provinceId}">
				<option value="">---</option>
				<c:forEach items="${provinces}" var="province">
				<c:if test="${filterForm.provinceId == province.id}">
				<option selected="true" value="${province.id}">${province.name}</option>
				</c:if>
				<c:if test="${filterForm.provinceId != province.id}">
				<option value="${province.id}">${province.name}</option>
				</c:if>
				</c:forEach>
			</select>
			<form:errors path="regionId" cssClass="ui-state-error"/>
			<span><fmt:message key="territorial.fields.region"/></span>:
			<select id="regions" name="regionId">
				<option value="">---</option>
			</select>
			<form:errors path="townId" cssClass="ui-state-error"/>
			<span><fmt:message key="course.fields.town"/></span>:
			<select id="towns" name="townId">
				<option value="">---</option>
			</select>
			<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
		   </form:form>
		</div>
	<%-- END TESTING --%>