<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><fmt:message key="search.site.title"/></title>
	<link href="<c:url value='/static/search/css/reset.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/static/search/css/estilo.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/static/search/js/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/ui/jquery.ui.core.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/ui/jquery.ui.widget.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/ui/jquery.ui.accordion.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/ui/jquery.ui.mouse.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/ui/jquery.ui.draggable.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/ui/jquery.ui.droppable.js'/>"></script>
<script>
	$(function() {
		$( "#accordion" ).accordion({
			collapsible: true,
			autoHeight: false
		});
	});
	
	</script>
   	<script>
	$(function() {
		$( ".draggable" ).draggable({ snap: ".ui-widget-header" });
	});
	</script>
</head>


<script type="text/javascript">
	$(document).ready(function() {	
		var tags = [];
		$("#snaptarget").droppable({
        	drop: function(event, ui) {
	                //$(this).append($(ui.draggable).clone());
	               // alert($(ui.draggable).attr('id'));
	               // tags.append($(ui.draggable).attr('id'));
	               var name = $(ui.draggable).attr('id');
	               tags.push(name); 
				},
            out: function(event, ui) {
					var position = tags.indexOf($(ui.draggable).attr('id'));
					tags[position] = null; 
		         }
        
		});
		
		
		$("#results a").each(function(){
			var href = $(this).attr("href");
			var base = '/${rc.locale.language}/search/dispatch';
			var link = base + "?resultUrl=" + href;
			$(this).attr("href", link);
		});
		
		$('#territorialButton').click(function(){
			<%-- PROVINCES --%>
			var iProvinces = $('input[name=province]:checkbox:checked');
			var provinces = ''; 
			$.each(iProvinces, function(i, province){
				if(provinces != '') {
					provinces += '|' + 'cProvince:' + $(province).val();
				}  else {
					provinces += 'cProvince:' + $(province).val();
				}
			});
			//alert(provinces);
			<%-- REGIONS --%>
			var iRegions = $('input[name=region]:checkbox:checked');
			var regions = ''; 
			$.each(iRegions, function(i, region){
				if(regions != '') {
					regions += '|' + 'cRegion:' + $(region).val();
				} else {
					regions += 'cRegion:' + $(region).val();
				}
			});
			
			
			<%-- PHRASE TO SEND --%>
			var partial = provinces;
			//alert(partial);
			if(partial == '') {
				partial = regions;
			} else {
				if(regions != "") {
					partial += '|' + regions;
				}
			}
			//alert(partial);
			
			
			$("#required").val(partial);
			
		});

		$("#tagsButton").click(function(){
			<%-- TAGS --%>
			//var iRegions = $('input[name=region]:checkbox:checked');
			var categories = '';
			$.each(tags, function(i, tag){
				if(tag != null && tag != '') {
					if(categories != '') {
						categories += '|';
					}
					categories += 'cTopics:' + tag;
				}
			});
			
			//alert(categories);
			$("#required").val(categories);
			$('#searchForm').submit();
		});
	});
</script>

<body>
<div id="contenedor" class="busqueda_guiada">
		<!--Cabecera-->
   <div id="cabecera">
    <h1>
     <a href="<c:url value='/${rc.locale.language}/search'/>"><img src="<c:url value='/static/search/img/hirubila_${rc.locale.language}.jpg'/>" alt="Logo Hirubila" title="<fmt:message key='search.logo.title'/>" /></a>
     </h1>
     <h2><fmt:message key='search.advanced.search'/>
     </h2>
    </div>
    <div id="accordion">
        <h3><a href="#"><fmt:message key='advancedSearch.geography.zone'/></a></h3> 
        <div>
           <form:form id="searchForm" action="/${rc.locale.language}/search/advanced" commandName="search" method="post" cssClass="formu_comarcas">
           		<form:hidden id="required" path="inMeta" />
           		<c:forEach items="${provinces}" var="province">
           			<ul>
           				<li><input name="province" value="${province.name}" type="checkbox" id="${province.name}"> <label for="${province.name}">${province.name}</label></li>
           				
           				<c:forEach items="${regions}" var="region">
           					<c:if test="${region.key eq province.id}">
           						<c:forEach items="${region.value}" var="region">
           							<li><input name="region" value="${region.name}" type="checkbox" id="${region.name}"><label for="${region.name}">${region.name}</label></li>
           						</c:forEach>
           						
           					</c:if>
           				</c:forEach>
                    </ul>
           		</c:forEach>
                <input id="territorialButton" type="submit" value="<fmt:message key='advancedSearch.topic.action'/>" />
           </form:form>
        </div>
        <h3><a href="#"><fmt:message key='advancedSearch.topic'/></a></h3>
        <div>
          <div id="snaptarget" class="ui-widget-header">
            <p><fmt:message key='advancedSearch.topic.advice'/></p>
          </div>
            	<c:forEach items="${tags}" var="tag">
            		 <div id="${tag.category}" class="draggable tema1">
                		<p>${tag.category}</p>
               		</div>
            	</c:forEach>
           
               	<p><input id="tagsButton" type="submit" value="<fmt:message key='advancedSearch.topic.action'/>" /></p>
         </div>
    </div>
</div>
</body>
</html>
