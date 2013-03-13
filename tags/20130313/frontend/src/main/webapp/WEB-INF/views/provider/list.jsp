<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>

<script type="text/javascript">


	/*$(document).ready(function(){
		$('#createCatalog').click(function(){
				alert('Creating catalog ' + $(this).attr("providerId"));
		});
	});*/

	function createCatalog(providerId) {
		var url = "<c:url value='/${rc.locale.language}/provider/" + providerId+ "/catalog/create'/>";
		alert(url);
		if(confirm("Esta seguro que...")) {
			$.get(url, function(data){
				alert("Data Loaded: " + data);
			});
		}
	}
	
	function deleteProvider(providerId) {
		if(confirm('<fmt:message key="provider.actions.delete.warning"/>')) {
			var url = '<c:url value='/${rc.locale.language}/dashboard/admin/management/delete/provider/'/>' + providerId;
			window.location = url;
			return true;
		} else {
			return false;
		}
	}
</script>
<h1><fmt:message key="provider.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
	
	
	<%@ include file="/WEB-INF/views/common/paginator-tables.jsp"%>	
	
	<table style="width:100%; border-spacing:0;">
		<thead>
		<tr>	
			<th class="sortable" order="name"><fmt:message key="provider.fields.name"/></th>
			<th><fmt:message key="provider.fields.feed"/></th>
			<th><fmt:message key="provider.actions"/></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${paginator.collection}" var="provider" varStatus="counter">
		<tr>	
			<td><a href="<c:url value='/${rc.locale.language}/provider/detail/${provider.id}'/>" title="${provider.name}">${provider.name}</a></td>
			<td>${provider.feed}</td>
			<td>
				<div id="menu-item-${counter.count}" class="options-menu">		
					<a class="expand-menu" href="#menu-item-${counter.count}"><fmt:message key="generic.options.message"/></a>
					<br>
					<ul class="content-options-menu" style="display: none">
	        			<li>
							<a href="<c:url value='/${rc.locale.language}/provider/${provider.id}/schools/'/>" title="${provider.name}">
								<fmt:message key="provider.action.schoolsManaged"/>
							</a>
						</li>
						<li>
							<a href="<c:url value='/${rc.locale.language}/provider/${provider.id}/courses/'/>" title="${provider.name}">
								<fmt:message key="provider.action.coursesManaged"/>
							</a>
						</li>
	        			<li>
	        				<a href="<c:url value='/${rc.locale.language}/provider/edit/${provider.id}/'/>" title="${provider.name}">
								<fmt:message key="school.action.edit"/>
							</a>
						</li>
						<sec:authorize ifAllGranted="ROLE_ADMIN">
		        			<li>
		        				<a href="#" title="${provider.name}" onclick="deleteProvider(${provider.id});">
									<fmt:message key="school.action.delete"/>
								</a>
							</li>
						</sec:authorize>
	        			<li>
	        				<a href="<c:url value='/loader/update/provider?providerId=${provider.id}/'/>" title="${provider.name} dump feed">
								<fmt:message key="action.dumpFeed"/>
							</a>
						</li>
						<%-- 
						<li>
	        				<a href="#" title="${provider.name} create catalog" onclick="javascript:createCatalog('${provider.id}');">
								Creacion catalogo SEO
							</a>
						</li>
						--%>
						<li>
							<a href="<c:url value='/${rc.locale.language}/provider/${provider.id}/summary/'/>" title="${provider.name}">
								<fmt:message key="provider.action.summary"/>
							</a>
						</li>
	   				</ul>	
	   			</div>
			</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>
		
		
						
		<table id="operations">
			<tr>
				<td>
					<a href="<c:url value='/${rc.locale.language}/provider/'/>" title="<fmt:message key='provider.list.addprovider'/>">
						<fmt:message key="provider.list.addprovider"/>
					</a>
				</td>
			</tr>
		</table>
