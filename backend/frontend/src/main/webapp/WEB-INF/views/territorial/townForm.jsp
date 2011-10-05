<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<script type="text/javascript">
	$(document).ready(function(){
		var regionId = '${town.region}';
		if(regionId != '') {
			getRegions($("#provincesSelect").val(), regionId);
		}
		
		$("#provincesSelect").change(function(){
			getRegions($(this).val());
		});

	});

	function getRegions(id, regionId) {
		$('#regionsSelect').empty();
		var provinceId = id;
		var url = '/${rc.locale.language}/territorial/town/regions/json?provinceId='+provinceId;
		$.getJSON(url, function(data){
			$.each(data, function(i, region){
				if(regionId != null && regionId == region.id) $("#regionsSelect").append('<option selected="true" value="' + region.id + '">' + region.name +'</option>');
				else $("#regionsSelect").append('<option value="' + region.id + '">' + region.name +'</option>');
			});
		});	
	}
</script>

<h1><fmt:message key="territorial.town.section.hedader"/></h1>

<form:form commandName="town" method="post">
	<div class="form_settings">
		<form:hidden path="id"/>
		
		<form:errors path="province" cssClass="error"/>
		<p><span><fmt:message key="territorial.town.fields.province"/></span>
			<form:select id="provincesSelect" path="province">
				<form:option value=""></form:option>
				<form:options items="${provinces}" itemValue="id" itemLabel="name" />
			</form:select>
		</p>
		
		<form:errors path="region" cssClass="error"/>
		<p><span><fmt:message key="territorial.town.fields.region"/></span>
			<form:select id="regionsSelect" path="region">
				<form:option value=""></form:option>
			</form:select>
		</p>	

		<form:errors path="name" cssClass="error"/>
		<p><span><fmt:message key="territorial.town.fields.name"/></span>
   			<form:input path="name"/>
   		</p>
      		
   		<p style="padding-top: 15px"><span>&nbsp;</span>
   			<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
   		</p>
	</div>
</form:form>
