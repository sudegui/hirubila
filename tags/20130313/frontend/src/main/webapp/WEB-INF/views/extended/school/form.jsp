<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 

<script type="text/javascript">
	var geocoder;
	var map;
	
	$(document).ready(function() {
		$('#address').change(function(){
			codeAddress();
		});

		$('#town').change(function(){
			codeAddress();
		});
		
		
		initialize();

		var select = false;
		$("#contactInfo_city").click(function(){
			select = false;
		});
		
		$("#contactInfo_city").change(function(){
			if(!select) $("#contactInfo_city").val("");
		});

		
		$("#contactInfo_city").autocomplete({
			 matchContains:false, 
		        minChars:2,  
		        autoFill:true, 
		        mustMatch:true, 
		        cacheLength:20, 
		        max:20, 
			//define callback to format results
			source: function(req, add){
				//pass request to server
				var url = '/${rc.locale.language}/extended/school/towns/json?query='+req.term,req;
				$.getJSON(url ,req, function(data) {
					//create array for response objects
					var suggestions = [];
					//process response
					 $.each(data, function(i, val){
						 suggestions.push({'id': val.id,'name': val.name});
					  });

					//pass array to callback
					 add(suggestions);
				 });
			},
			focus: function( event, ui ) {
				return false;
			},
			//define select handler
		 	select: function(e, ui) {
				$("#contactInfo_city").val(ui.item.name);
				$("#town").val(ui.item.id);	 
				$("#town").change();
				select = true;
				return false;	
           },
           close: function(event, ui) {
               if($("#town").val() == null || $("#town").val() == '') $("#contactInfo_city").val('');
               return false;
           },    
         //define select handler
           change: function(event, ui) {	
               if(!select) {
            	   $("#contactInfo_city").val('');
                   $("#town").val('');
               }	 	
               return false;		
           }
		}).data( "autocomplete" )._renderItem = function( ul, item ) {
			return $( "<li></li>" )
			.data( "item.autocomplete", item )
			.append( "<a>" + item.name + "</a>" )
			.appendTo( ul );
		};
	}); 
	
	function initialize() {
		geocoder = new google.maps.Geocoder();
	}		

	function codeAddress() {
		var address = document.getElementById("address").value;
	    var city = document.getElementById("contactInfo_city").value;
	    var town = document.getElementById("town").value;
	    var country = document.getElementById("contactInfo_country").value;
	    var addresParam = address + ", " + city;
	    var name = document.getElementById("name").value;
	    if(town != '' && address != '') {
		    geocoder.geocode( { 'address': addresParam, 'region' : 'es'}, function(results, status) {
		      if(status == google.maps.GeocoderStatus.OK) {
			    var geoLocation = results[0].geometry.location.lat() + "," + results[0].geometry.location.lng();	
			   	//var latlng = new google.maps.LatLng(-34.397, 150.644);
		  	    var myOptions = {
		  	      zoom: 16,
		  	      center: results[0].geometry.location,
		  	      mapTypeId: google.maps.MapTypeId.ROADMAP
		  	    }
		    	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
		       // map.setCenter(results[0].geometry.location);
		        var marker = new google.maps.Marker({
		            map: map, 
		            position: results[0].geometry.location,
		            title: name
		        });
		        var infowindow = new google.maps.InfoWindow({  
					content: '<strong>' + name + '</strong><br>'
				});  
		        infowindow.open(map, marker);  
		      } else {
		    	  map = null;
		      }
		      $("#geoLocation").val(geoLocation);
		    });
	    }
	  }
</script>

<h1><fmt:message key="school.add.section.header"/></h1>
		<form:form commandName="school" method="post">
			<div class="form_settings">
				<form:hidden path="id"/>
				
				<form:errors path="name" cssClass="ui-state-error"/>
				<p><span><fmt:message key="school.fields.name"/></span><form:input id="name" path="name"/></p>
				
				<form:errors path="contactInfo.telephone" cssClass="ui-state-error"/>				
				<p><span><fmt:message key="contactInfo.fields.telephone"/></span><form:input id="contactInfo.telephone" path="contactInfo.telephone"/></p>
				
				<form:errors path="contactInfo.fax" cssClass="ui-state-error"/>
				<p><span><fmt:message key="contactInfo.fields.fax"/></span><form:input id="contactInfo.fax" path="contactInfo.fax"/></p>
				
				<form:errors path="contactInfo.streetAddress" cssClass="error"/>
				<p><span><fmt:message key="contactInfo.fields.streetAddress"/></span><form:input cssClass="address" id="address" path="contactInfo.streetAddress"/></p>
				
				<form:errors path="contactInfo.city" cssClass="ui-state-error"/>				
				<p>
					<span><fmt:message key="contactInfo.fields.city"/></span>
					<form:input cssClass="address" id="contactInfo_city" path="contactInfo.city"/>
					<form:hidden id="town" path="town"/>
					<form:hidden id="geoLocation" path="geoLocation" />
				</p>
				
				<form:errors path="contactInfo.zipCode" cssClass="ui-state-error"/>				
				<p><span><fmt:message key="contactInfo.fields.zipCode"/></span><form:input id="contactInfo.zipCode" path="contactInfo.zipCode"/></p>
				
				<form:errors path="contactInfo.country" cssClass="ui-state-error"/>
				<p><span><fmt:message key="contactInfo.fields.country"/></span><form:input id="contactInfo_country" path="contactInfo.country"/></p>
				
    			<form:errors path="contactInfo.webSite" cssClass="ui-state-error"/>				
				<p><span><fmt:message key="contactInfo.fields.webSite"/></span><form:input id="contactInfo.webSite" path="contactInfo.webSite"/></p>
    			
    			<form:errors path="contactInfo.email" cssClass="ui-state-error"/>				
				<p><span><fmt:message key="contactInfo.fields.email"/></span><form:input id="contactInfo.email" path="contactInfo.email"/></p>
				
    			<form:hidden path="created" />
       			<form:hidden path="updated" />
       			<p style="padding-top: 15px"><span>&nbsp;</span>
            	<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
            	</p>	
			</div>	
		</form:form>
	
		<div id="map_canvas" style="width: 500px; height: 300px"></div> 