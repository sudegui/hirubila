<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<tiles:insertAttribute name="extraHead" />
	<c:set var="title"><tiles:insertAttribute name="title" ignore="true" /></c:set>
	<c:set var="menuId" scope="request"><tiles:insertAttribute name="menuId" ignore="true" /></c:set>
	<title><fmt:message key="${title}"/></title>
	<%@ include file="/WEB-INF/views/common/includes_css.jsp"%>
	<%@ include file="/WEB-INF/views/common/includes_js.jsp"%>	
</head>

<body>

<div id="main">

    <div id="catalog_header"/>
      
    </div>
    
    <div id="content_header"></div>
    
    <div id="site_content">
      
      
      <div id="content_1col">
        <tiles:insertAttribute name="body" />
      </div>
      
    </div>
    
    <div id="content_footer"></div>
    
    <div id="footer">
      <tiles:insertAttribute name="footer" />
    </div>
  </div>
</body>
</html>
