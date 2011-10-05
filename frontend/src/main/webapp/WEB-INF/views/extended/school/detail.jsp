<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
<script type="text/javascript">

	$(document).ready(function() {
		initialize();
	}); 
	
	function initialize() {
		var latitud = '${school.geoLocation.latitude}';
		var longitud = '${school.geoLocation.longitude}';
		if(latitud != null && '' != latitud && longitud != null && '' != longitud) {
	   		var myLatlng = new google.maps.LatLng(latitud, longitud);
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
				title: '${school.name}'  
			});  
	
			var infowindow = new google.maps.InfoWindow({  
				content: '<strong>${school.name}</strong><br><br>${school.contactInfo.streetAddress}<br>${school.contactInfo.city}<br>' +
					'${school.contactInfo.telephone}<br>${school.contactInfo.email}<br>${school.contactInfo.webSite}'   
			});  
	
			google.maps.event.addListener(marker, 'click', function() {  
				infowindow.open(map, marker);  
			}); 
		}
 }
   
			
</script>

		<h3><fmt:message key="school.detail.section.header"/></h3>
		
		<fmt:message key="school.fields.name"/>  : ${school.name} <br/>
		<fmt:message key="school.fields.created"/>  : <fmt:formatDate value="${school.created}" type="date" pattern="dd-MM-yyyy" /><br/>
		<fmt:message key="school.fields.updated"/>  : <fmt:formatDate value="${school.updated}" type="date" pattern="dd-MM-yyyy" /> <br/>
		
		<h3><fmt:message key="contactInfo.title"/>:</h3>
		<fmt:message key="contactInfo.fields.city"/>  : ${school.contactInfo.city} <br/>
		<fmt:message key="contactInfo.fields.country"/>  : ${school.contactInfo.country} <br/>
		<fmt:message key="contactInfo.fields.telephone"/> : ${school.contactInfo.telephone} <br/>
		<fmt:message key="contactInfo.fields.fax"/>  : ${school.contactInfo.fax} <br/>
		<fmt:message key="contactInfo.fields.zipCode"/>  : ${school.contactInfo.zipCode} <br/>
		<fmt:message key="contactInfo.fields.email"/>  : ${school.contactInfo.email} <br/>
		<fmt:message key="contactInfo.fields.streetAddress"/> : ${school.contactInfo.streetAddress} <br/>
		<fmt:message key="contactInfo.fields.webSite"/> :<a href="${school.contactInfo.webSite}" target="_blank"> ${school.contactInfo.webSite} </a><br/>
		<br/>
		<fmt:message key="school.fields.geoLocation"/> : ${school.geoLocation} <br/>
		<br/>

		<div id="map_canvas" style="width: 500px; height: 300px"></div> 