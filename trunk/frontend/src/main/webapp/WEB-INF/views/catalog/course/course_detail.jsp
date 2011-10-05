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
	<link href="<c:url value='/static/search/css/estilo.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/static/search/js/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/ga.js'/>"></script>
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
	<script type="text/javascript" src="http://apis.google.com/js/plusone.js"></script>
	
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

	function wordToUpper(strSentence) {
		//alert(strSentence);
	    return strSentence.toLowerCase().replace(/\b[a-z]/g, convertToUpper);
	    function convertToUpper() {
	        return arguments[0].toUpperCase();
	    }
	}

	function exists(element, index, array) {
		  return array.indexOf(element) == index;  
	}
	
	function cutLastChar(text) {
		var cutted = text.substring(0, text.length -1);
		var a = cutted.split(",");
		var filtered = a.filter(exists).join(",");
		return filtered;
	}

</script>

<body>
<div id="contenedor" class="red_mediadores detalle_busqueda">

    <h1>${course.title}</h1>
     <div class="content_red_texto">
    
        <div class="red_texto" style="width:100%">
    
            <table>
            	<tr>
            	<td width="80%">
            		<h2><fmt:message key="catalog.course.detail.message.provider"/>: ${course.providerName}</h2>
            	</td>
            	<td align="right">
            		<a href="<c:url value='/${rc.locale.language}/search/'/>">
            			<img border="0" src="<c:url value='/static/search/img/hirubila_${rc.locale.language}.jpg'/>" alt="Logo Hirubila" />
            		</a>
            	</td>
            	</tr>
            </table>
            
            <c:if test="${not empty course.information.value}">
            	<p><strong><fmt:message key="course.fields.information"/>:&nbsp;</strong>${course.information.value}</p>
            </c:if>
            
            <c:if test="${not empty course.tags}">
            	<p><strong><fmt:message key="course.fields.tags"/>:&nbsp;</strong><script type="text/javascript">document.write(cutLastChar('${course.tags}'));</script></p>
            </c:if>
            
            
            <c:set var="url" value="${course.url}" />
            <c:if test="${not empty url}">
            	
            	<c:if test="${!fn:startsWith(url, 'http://')}">
            		<c:set var="url" value="http://${url}" />
            	</c:if>
            <p><strong><fmt:message key="catalog.moreinfo.message" />:&nbsp;</strong><a href="${url}" target="_blank"><fmt:message key="catalog.originallink.message" /></a></p>
            </c:if>
            
            <c:if test="${rc.locale.language == 'es'}">
            	<c:if test="${not empty course.start}">
            		<p><strong><fmt:message key="course.fields.start" /></strong>: <fmt:formatDate value="${course.start}" type="date" pattern="dd-MM-yyyy" /></p>
            	</c:if>
            	<c:if test="${not empty course.end}">
					<p><strong><fmt:message key="course.fields.end" /></strong>: <fmt:formatDate value="${course.end}" type="date" pattern="dd-MM-yyyy" /></p>
				</c:if>
            </c:if>
            
            <c:if test="${rc.locale.language != 'es'}">
            	<c:if test="${not empty course.start}">
            		<p><strong><fmt:message key="course.fields.start" /></strong>: <fmt:formatDate value="${course.start}" type="date" pattern="yyyy-MM-dd" /></p>
            	</c:if>
            	<c:if test="${not empty course.end}">
					<p><strong><fmt:message key="course.fields.end" /></strong>: <fmt:formatDate value="${course.end}" type="date" pattern="yyyy-MM-dd" /></p>
				</c:if>
            </c:if>
           	           
            <%-- SHARE --%>
           
            	<table cellspacing="10" cellpadding="3">
            		<tr>
            	<td valign="top">
            		<%-- LINKEDIN--%>
					<script type="text/javascript" src="http://platform.linkedin.com/in.js"></script><script type="in/share" data-url="${result.link}" data-counter="right"></script>
            	</td>
				<td valign="top">
					<%-- FACEBOOK--%>
				<iframe src="http://www.facebook.com/plugins/like.php?href=${result.link}&amp;layout=button_count&amp;show_faces=true&amp;width=650&amp;action=recommend&amp;font=arial&amp;colorscheme=light&amp;height=21" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:90px; height:21px;margin-bottom:-7px;" allowTransparency="true"></iframe>
				</td>
				<td valign="top">
					<%-- TWITTER --%>
				<a href="http://twitter.com/share" class="twitter-share-button"  data-url="${result.link}" data-text="<fmt:message key="search.results.share.twitter.message1"/>: ${result.title} <fmt:message key="search.results.share.twitter.message2"/> #Hirubila" data-count="horizontal"><fmt:message key="search.results.share.twitter.shareMessage"/></a><script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>				
				</td>
				<td valign="top">
					<%-- GOOGLE+ --%>
					<g:plusone size="medium"></g:plusone>
				</td>
				</tr>
				</table>
			
            <%-- SCHOOL INFORMATION --%>
            
            <div style="width: 400px; float:right;">
            <div id="map_canvas" style="width: 400px; height: 300px;  margin-left:15px;"></div>
            	
            
            <div id="warning_info" style=" margin-top:10px;margin-left:15px;"><strong><fmt:message key="message.map.warning.noexactLocation"/></strong></div>
            </div>
            <h2><fmt:message key="catalog.course.detail.message.school"/>:&nbsp;${course.schoolName}</h2>
            	<c:if test="${not empty course.schoolInfo.city}">
            		<p><strong><fmt:message key="contactInfo.fields.city"/>:</strong>&nbsp <script>document.write(wordToUpper('${course.schoolInfo.city}'));</script></p>
            	</c:if>
            	<c:if test="${not empty course.schoolInfo.telephone}">
			  		<p> <strong><fmt:message key="contactInfo.fields.telephone"/>:</strong>&nbsp;${course.schoolInfo.telephone}</p>
			  	</c:if>
			  	<c:if test="${not empty course.schoolInfo.fax}">
			   		<p><strong><fmt:message key="contactInfo.fields.fax"/>:</strong>&nbsp;${course.schoolInfo.fax} </p>
			   	</c:if>
			   	<c:if test="${not empty course.schoolInfo.zipCode}">
			   		<p><strong><fmt:message key="contactInfo.fields.zipCode"/>:</strong>&nbsp;${course.schoolInfo.zipCode} </p>
			   	</c:if>
			   	<c:if test="${not empty course.schoolInfo.email}">
			   		<p><strong><fmt:message key="contactInfo.fields.email"/>:</strong>&nbsp;<a href="mailto:${course.schoolInfo.email}">${course.schoolInfo.email}</a></p>
			   	</c:if>
			   	<c:if test="${not empty course.schoolInfo.streetAddress}">
			   		<p><strong><fmt:message key="contactInfo.fields.streetAddress"/>:</strong>&nbsp;${course.schoolInfo.streetAddress} </p>
			   	</c:if>
			   	<c:if test="${not empty course.schoolInfo.webSite}">
			   		<p><strong><fmt:message key="contactInfo.fields.webSite"/>:</strong>&nbsp;<a href="${course.schoolInfo.webSite}">${course.schoolInfo.webSite}</a></p>
			   	</c:if>
			<br/>
            <br/>
			<p><a href="<c:url value='/${rc.locale.language}/search/'/>"><img border="0" src="<c:url value='/static/search/img/hirubila_${rc.locale.language}.jpg'/>" alt="Logo Hirubila" /></a></p>
        </div> 
     </div>
    
    	


</div>


</body>
</html>