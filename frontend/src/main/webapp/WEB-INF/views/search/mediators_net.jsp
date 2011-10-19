<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><fmt:message key="search.mediators.net.title"/></title>
	<link href="<c:url value='/static/search/css/estilo.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/static/search/js/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/ga.js'/>"></script>
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
		var euskadiGeo = new google.maps.LatLng(42.990586, -2.620239);
		var myOptions = {
				zoom: 8,
				center: euskadiGeo,
				disableDefaultUI: true,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			}
		var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

		var infowindow = new google.maps.InfoWindow({  
			content: ''   
		});  

		var markers = [];
		
		for(var i=0;i<mediations.length;i++) {
			var mediation = mediations[i];
			var marker = new google.maps.Marker({  
				position: new google.maps.LatLng(mediation.latitude, mediation.longitude),  
				map: map,  
				//title: mediation.name,  
				content: '<div id="content"><strong>'+mediation.name+'</strong><br><br>'+mediation.street+'<br>'+mediation.city+'<br>' +
				mediation.telephone+'<br>'+mediation.email+'</div>'
			});  
			markers.push(marker);
			
		}
		for(var i=0;i<markers.length;i++) {
			var mark = markers[i];
			google.maps.event.addListener(mark, 'click', function(){
				infowindow.setContent(this.content);
				infowindow.open(map, this);  
			});							
		}
	}

	function addMediation(longitude, latitude, name, street, city, telephone, email) {
		var m = new mediation(longitude, latitude, name, street, city, telephone, email);
		mediations.push(m);
	}

	function mediation(longitude, latitude, name, street, city, telephone, email) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = name;
		this.street = street;
		this.city = city;
		this.telephone = telephone;
		this.email = email;
	}
</script>

<body>
<div id="contenedor" class="red_mediadores">
    <h1><!--<img alt="Hirubila ikasgida" src="/static/search/img/logo_ikasgida_es.jpg">--><img src="<c:url value='/static/search/img/logo_ikasgida_${rc.locale.language}.jpg'/>" alt="<fmt:message key='search.site.title'/>"/>
    <!--<fmt:message key="search.mediators.net.header"/>-->
    </h1>
     <div class="content_red_texto">
        <div class="red_texto">
            <h2><fmt:message key="search.mediators.net.question.one"/></h2>
            <p><fmt:message key="search.mediators.net.answer.one"/></p>
        </div> 
        <div class="red_texto">
            <h2><fmt:message key="search.mediators.net.question.two"/></h2>
            <p><fmt:message key="search.mediators.net.answer.two"/></p>
        </div> 
     </div>
   	<div class="conten_red_principal">
          <div class="conten_mapa">
         	<div id="map_canvas" style="width: 600px; height: 415px"></div>
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
			  	   	<script type="text/javascript">addMediation('${mediator.geoLocation.longitude}','${mediator.geoLocation.latitude}','${mediator.name}','${mediator.contactInfo.streetAddress}','${mediator.contactInfo.city}', '${mediator.contactInfo.telephone}', '${mediator.contactInfo.email}');</script>
			  			  </c:forEach>
			  			</c:if>
	              	</c:forEach>
	              </select>
              </c:forEach>
            </form>
            <p class="descarga_pdf"><a href="http://clients.m4f.es/mediadores-${rc.locale.language}.pdf" target="_blank" type="application/pdf"><fmt:message key="search.mediators.net.dowload.pdf"/></a></p>
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