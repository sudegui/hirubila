<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title><fmt:message key="event.website.title"/></title>
		<link href="<c:url value='/static/css/estilo_efqm.css'/>" rel="stylesheet" type="text/css" />
	
	</head>
	
	<body id="efqm">
	
	  <div id="global" class="fondo_practices">
		
		<%@ include file="/WEB-INF/views/common/header.jsp" %>
			
		<div id="efqm_content"> 
			<h3 class="upload_practices">
				<fmt:message key="event.website.target"/>
			</h3>	
			<div id="conten_central">
				<div class="columna_left">
					<div class="borde_right">
						
					</div>
					
					
					
				</div>
				<div class="conten_right">
				<p class="entra_naranja">
					<fmt:message key="message.thanks"/><br/><br/>
					${message}
				</p>
				
				</div>
			</div>
			</div>
			
		
	</div>
	</body>
	
</html>
