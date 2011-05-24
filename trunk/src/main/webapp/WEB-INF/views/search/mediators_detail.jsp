<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>${mediatonService.name} | <fmt:message key="search.mediators.net.message.part1"/></title>
	<link href="<c:url value='/static/search/css/reset.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/static/search/css/estilo.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/static/search/js/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>


<script type="text/javascript">
	var mediations = [];
	$(document).ready(function() {	
		$(":input[name=mediadores]").change(function(){
			if($(this).val() != '') {
				window.location = "<c:url value='/${rc.locale.language}/search/mediators/'/>"+$(this).val();
			}
		});
		initialize();	
	});

	function initialize(){
		var mediationGeo = new google.maps.LatLng('${mediatonService.geoLocation.latitude}', '${mediatonService.geoLocation.longitude}');
		var myOptions = {
				zoom: 12,
				center: mediationGeo,
				disableDefaultUI: true,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			}
		var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

		var infowindow = new google.maps.InfoWindow({  
			content: '${mediatonService.name} | <fmt:message key="search.mediators.net.message.part1"/>', 
		});  

		var marker = new google.maps.Marker({  
			position: mediationGeo,  
			map: map,  
			title: '${mediatonService.name}',  
			content: '<div id="content"><strong>${mediatonService.name}</strong><br><br>${mediatonService.contactInfo.streetAddress}<br>${mediatonService.contactInfo.city}<br>' +
			'${mediatonService.contactInfo.telephone}<br>${school.contactInfo.email}</div>'    
				//'${mediatonService.name}',
		});  
		google.maps.event.addListener(marker, 'click', function(){
			infowindow.setContent(this.content);
			infowindow.open(map, this);  
		});
	}
</script>

<body>
<div id="contenedor" class="red_mediadores">
    <h1><fmt:message key="search.mediators.net.header"/></h1>
     <div class="content_red_texto">
        <div class="red_texto">
            <h2>${mediatonService.name}</h2>
            <p><strong>${mediatonService.entity}</strong><br>
			<%-- Maite Goikouria<br> --%>
			<strong><fmt:message key="contactInfo.fields.streetAddress"/>:</strong>&nbsp;${mediatonService.contactInfo.streetAddress}&nbsp;${mediatonService.contactInfo.city}<br>
			<strong><fmt:message key="contactInfo.fields.telephone"/>:</strong>&nbsp;${mediatonService.contactInfo.telephone}<br>
			<strong><fmt:message key="contactInfo.fields.email"/>:</strong>&nbsp;<a href="mailto:${mediatonService.contactInfo.email}">${mediatonService.contactInfo.email}</a></p>
        </div> 
     </div>
     	<div class="conten_red_principal">
            <div class="conten_mapa">
            	<div id="map_canvas" style="width: 600px; height: 415px"></div>
            	<%-- <iframe width="600" height="415" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="http://maps.google.es/maps?f=q&amp;source=s_q&amp;hl=es&amp;geocode=&amp;q=pais+vasco&amp;sll=42.989625,-2.618927&amp;sspn=1.631326,3.56781&amp;ie=UTF8&amp;hq=&amp;hnear=Pa%C3%ADs+Vasco&amp;ll=42.990586,-2.620239&amp;spn=0.833733,1.645203&amp;z=9&amp;iwloc=A&amp;output=embed"></iframe> --%>
            </div>
          <div class="caja_red_dcha">
           <h3><fmt:message key="search.mediators.net.list"/></h3>
           
            <form name="form" id="form">
			  <c:forEach items="${provinces}" var="province">
			  	<select name="mediadores" id="mediadores_araba">
			  		<option value="">${province.name}</option>
			  		<c:forEach items="${mediators}" var="mediatorList">
			  			<c:if test="${mediatorList.key == province.id}">
			  			  <c:forEach items="${mediatorList.value}" var="mediator">
			  	    <option value="${mediator.id}">${mediator.name}</option>		
			  			  </c:forEach>
			  		
			  			</c:if>
	              	</c:forEach>
	              </select>
              </c:forEach>
            </form>
            <!--  
            <p class="descarga_pdf"><a href="#"><fmt:message key="search.mediators.net.dowload.pdf"/></a></p>
            -->
			<div class="caja_hiru_dcha">
            	<h2><fmt:message key="search.mediators.net.question.three"/></h2>
                <p><strong><fmt:message key="search.mediators.net.message.part1"/></strong>&nbsp;<fmt:message key="search.mediators.net.message.part2"/>
            	<a href="<c:url value='/${rc.locale.language}/search/'/>"><img src="<c:url value='/static/search/img/hirubila_${rc.locale.language}.jpg'/>" alt="Logo Hirubila" /></a>
        	</div>
            </div>
       </div>
</div>
</body>
</html>