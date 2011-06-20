<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title><spring:escapeBody htmlEscape="true">${course.title} - (${course.schoolName})</spring:escapeBody></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<tiles:insertAttribute name="gsa_metadata"/>
	<link href="<c:url value='/static/search/css/reset.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/static/search/css/estilo.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/static/search/js/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/ga.js'/>"></script>
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
	
</head>


<script type="text/javascript">
	
	$(document).ready(function() {	
		initialize();	
	});

	function initialize() {
		var latitud = '${course.schoolGeoLocation.latitude}';
		var longitud = '${course.schoolGeoLocation.longitude}';
		var myLatlng = null;
		
		if(latitud != null && '' != latitud && longitud != null && '' != longitud) {
			myLatlng = new google.maps.LatLng(latitud, longitud);
			doMap(myLatlng);
		} else {
			var address = '${course.schoolInfo.streetAddress}';
		    var city = '${course.schoolInfo.city}';
		    var town = '${course.schoolInfo.city}';
		    var country = '${course.schoolInfo.country}';
		    var addresParam = address + ", " + '${course.schoolInfo.zipCode}' + ", " + city;
		    var geocoder = new google.maps.Geocoder();
		    if(town != '' && address != '') {
		    	geocoder.geocode( { 'address': addresParam, 'region' : 'es'}, function(results, status) {
			      if(status == google.maps.GeocoderStatus.OK) {
			    	  doMap(new google.maps.LatLng(results[0].geometry.location.lat(), results[0].geometry.location.lng()));
			      }
			      
				});
		    }
		}

	}
	
	function doMap(myLatlng) {
		if(myLatlng != null) {
			var myOptions = {
				zoom: 16,
				center: myLatlng,
				disableDefaultUI: true,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			}
	   		var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	
			var marker = new google.maps.Marker({  
				position: myLatlng,  
				map: map,  
				title: '${course.schoolName}'  
			});  
	
			var infowindow = new google.maps.InfoWindow({  
				content: '<strong>${course.schoolName}</strong><br><br>${course.schoolInfo.streetAddress}<br>${course.schoolInfo.city}<br>' +
					'${course.schoolInfo.telephone}<br>${course.schoolInfo.email}<br>${course.schoolInfo.webSite}'   
			});  
	
			google.maps.event.addListener(marker, 'click', function() {  
				infowindow.open(map, marker);  
			}); 
		}
	}
</script>

<body>
<div id="contenedor" class="red_mediadores detalle_busqueda">
<!--  
     <div class="barra_enlaces enlaces_arriba"> 
                		<p>                               
		                   <a target="_blank" class="envia_mail" rel="shadowbox;height=403;width=450" href="/es/search/results/send?link=http://goo.gl/hgTDk">Enviar por e-mail</a> - 
		                   <a onclick="muestra_oculta('1')" class="comparte_link_top" id="link_compartir_1">Compartir</a> - 
		                   <a rel="shadowbox;height=302;width=450" href="/es/search/results/error">Avisar de error</a>
              			</p>
              			<div style="display: block;" id="caja_compartir_1" class="caja_compartir">
              				<p>Compartir en: 
                    		
                    		<script src="http://platform.linkedin.com/in.js" type="text/javascript"></script><span style="line-height: 1; vertical-align: middle; display: inline-block; text-align: center;" class="IN-widget"><span style="padding: 0pt ! important; margin: 0pt ! important; text-indent: 0pt ! important; display: inline-block ! important; vertical-align: middle ! important; font-size: 1px ! important;"><span id="li_ui_li_gen_1305213297091_0"><a href="javascript:void(0);" id="li_ui_li_gen_1305213297091_0-link"><span id="li_ui_li_gen_1305213297091_0-logo">in</span><span id="li_ui_li_gen_1305213297091_0-title">Share</span></a></span></span><span style="padding: 0pt ! important; margin: 0pt ! important; text-indent: 0pt ! important; display: inline-block ! important; vertical-align: middle ! important; font-size: 1px ! important;"><span class="right hidden" id="li_ui_li_gen_1305213297096_1-container"><span class="right" id="li_ui_li_gen_1305213297096_1"><span class="right" id="li_ui_li_gen_1305213297096_1-inner"><span class="right" id="li_ui_li_gen_1305213297096_1-content">0</span></span></span></span></span></span><script data-counter="right" data-url="http://goo.gl/hgTDk" type="in/share+init"></script>
                    		
                    		
                    		<iframe frameborder="0" scrolling="no" allowtransparency="true" style="border:none; overflow:hidden; width:90px; height:21px;margin-bottom:-7px;" src="http://www.facebook.com/plugins/like.php?href=http://goo.gl/hgTDk&amp;layout=button_count&amp;show_faces=true&amp;width=650&amp;action=recommend&amp;font=arial&amp;colorscheme=light&amp;height=21"></iframe>
                    		
							
                    		<iframe frameborder="0" scrolling="no" class="twitter-share-button twitter-count-horizontal" tabindex="0" allowtransparency="true" src="http://platform0.twitter.com/widgets/tweet_button.html?_=1305213296584&amp;count=horizontal&amp;lang=en&amp;text=Curso%3A%20CONDUCCI%C3%93N%20ECON%C3%93MICA%20-%20(EVE%20Ente%20Vasco%20de%20la%20%3Cb%3E...%3C%2Fb%3E%20encontrado%20a%20trav%C3%A9s%20de%20%23Hirubila&amp;url=http%3A%2F%2Fgoo.gl%2FhgTDk" style="width: 110px; height: 20px;" title="Twitter For Websites: Tweet Button"></iframe><script src="http://platform.twitter.com/widgets.js" type="text/javascript"></script>
                    		</p>
                    	</div>
              		</div>
-->
    <h1>${course.title}</h1>
     <div class="content_red_texto">
        <div class="red_texto" style="width:100%">
            
            <h2><fmt:message key="catalog.course.detail.message.provider"/>: ${course.providerName}</h2>
            
            <p><strong><fmt:message key="course.fields.information"/>:&nbsp;</strong>${course.information.value}</p>
            
            <p><strong><fmt:message key="course.fields.tags"/>:&nbsp;</strong>${course.tags}</p>
            <c:set var="url" value="${course.url}" />
            <c:if test="${not empty url}">
            <br/>
            	
            	<c:if test="${!fn:startsWith(url, 'http://')}">
            		<c:set var="url" value="http://${url}" />
            	</c:if>
            <p><strong><fmt:message key="catalog.moreinfo.message" />:&nbsp;</strong><a href="${url}" target="_blank"><fmt:message key="catalog.originallink.message" /></a></p>
            </c:if>
            
            <c:if test="${rc.locale.language == 'es'}">
            <p><strong><fmt:message key="course.fields.start" /></strong>: <fmt:formatDate value="${course.start}" type="date" pattern="dd-MM-yyyy" /></p>
			<p><strong><fmt:message key="course.fields.end" /></strong>: <fmt:formatDate value="${course.end}" type="date" pattern="dd-MM-yyyy" /></p>
            </c:if>
            <c:if test="${rc.locale.language != 'es'}">
            <p><strong><fmt:message key="course.fields.start" /></strong>: <fmt:formatDate value="${course.start}" type="date" pattern="yyyy-MM-dd" /></p>
			<p><strong><fmt:message key="course.fields.end" /></strong>: <fmt:formatDate value="${course.end}" type="date" pattern="yyyy-MM-dd" /></p>
            </c:if>
           	
            <br/>
            
            <%-- SHARE --%>
            <p>
				<%-- LINKEDIN--%>
				<script type="text/javascript" src="http://platform.linkedin.com/in.js"></script><script type="in/share" data-url="${result.link}" data-counter="right"></script>
				
				<%-- FACEBOOK--%>
				<iframe src="http://www.facebook.com/plugins/like.php?href=${result.link}&amp;layout=button_count&amp;show_faces=true&amp;width=650&amp;action=recommend&amp;font=arial&amp;colorscheme=light&amp;height=21" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:90px; height:21px;margin-bottom:-7px;" allowTransparency="true"></iframe>
				
				<%-- TWITTER --%>
				<a href="http://twitter.com/share" class="twitter-share-button"  data-url="${result.link}" data-text="<fmt:message key="search.results.share.twitter.message1"/>: ${result.title} <fmt:message key="search.results.share.twitter.message2"/> #Hirubila" data-count="horizontal"><fmt:message key="search.results.share.twitter.shareMessage"/></a><script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>
			</p>
			
			<br/>
            <%-- SCHOOL INFORMATION --%>
            
            <div style="width: 400px; float:right;">
            <div id="map_canvas" style="width: 400px; height: 300px;  margin-left:15px;"></div>
            	
            
            <div id="warning_info" style=" margin-top:10px;margin-left:15px;"><strong><fmt:message key="message.map.warning.noexactLocation"/></strong></div>
            </div>
            <h2><fmt:message key="catalog.course.detail.message.school"/>:&nbsp;${course.schoolName}</h2>
            <p><strong><fmt:message key="contactInfo.fields.city"/>:</strong>&nbsp;${course.schoolInfo.city}</p>
			 <%--<strong><fmt:message key="contactInfo.fields.country"/>:</strong>${course.schoolInfo.country} <br/>--%>
			  <p> <strong><fmt:message key="contactInfo.fields.telephone"/>:</strong>&nbsp;${course.schoolInfo.telephone}</p>
			   <p><strong><fmt:message key="contactInfo.fields.fax"/>:</strong>&nbsp;${course.schoolInfo.fax} </p>
			   <p><strong><fmt:message key="contactInfo.fields.zipCode"/>:</strong>&nbsp;${course.schoolInfo.zipCode} </p>
			   <p><strong><fmt:message key="contactInfo.fields.email"/>:</strong>&nbsp;<a href="mailto:${course.schoolInfo.email}">${course.schoolInfo.email}</a></p>
			   <p><strong><fmt:message key="contactInfo.fields.streetAddress"/>:</strong>&nbsp;${course.schoolInfo.streetAddress} </p>
			   <p><strong><fmt:message key="contactInfo.fields.webSite"/>:</strong>&nbsp;<a href="${course.schoolInfo.webSite}">${course.schoolInfo.webSite}</a></p>

			<br/>
            <br/>
			
        </div> 
     </div>
    
    	


</div>
</body>
</html>