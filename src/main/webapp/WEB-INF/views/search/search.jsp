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
	<script type="text/javascript" src="<c:url value='/static/search/js/javascript.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/ga.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/jquery-1.5.1.js'/>"></script>
</head>


<script type="text/javascript">
	$(document).ready(function() {	
		$("#results a").each(function(){
			var href = $(this).attr("href");
			var base = '/${rc.locale.language}/search/dispatch';
			var link = base + "?resultUrl=" + href;
			$(this).attr("href", link);
			$(this).attr("target", "_blank");
		});
	});
</script>

<body>
	<div id="contenedor">
		<div id="conten_izq" class="margin_home">
		<!--Menú izquierda-->
			<div id="nav" >
		    	<ul>
		    		
		        	<li class="red">
		           	<a href="<c:url value='/${rc.locale.language}/search/mediators'/>"><fmt:message key="search.mediators.net"/></a>
		            </li>
		            <!--  
		            <li class="guiada">
		           	<a href="<c:url value='/${rc.locale.language}/search/advanced'/>"><fmt:message key="search.advanced.search"/></a>
		            </li>
		            -->
		        </ul>
		        <%-- Actualmente: ${total} cursos indexados --%>
    		</div>
    	<!--Caja twitter con los términos de búsqueda "Formación País Vasco"-->
    		<div class="conten_twitter">
    			<script src="http://widgets.twimg.com/j/2/widget.js"></script>
				<script>
				new TWTR.Widget({
				  version: 2,
				  type: 'search',
				  search: '<fmt:message key="search.home.twitter.hashtags"/>',
				  interval: 6000,
				  title: '',
				  subject: 'Hiru',
				  width: 200,
				  height: 300,
				   theme: {
				    shell: {
				      background: '#daeb7c',
				      color: '#2c5902'
				    },
				    tweets: {
				      background: '#ffffff',
				      color: '#757475',
				      links: '#2c5902'
				    }
				  },
				  features: {
				    scrollbar: false,
				    loop: true,
				    live: true,
				    hashtags: true,
				    timestamp: true,
				    avatars: true,
				    toptweets: true,
				    behavior: 'default'
				  }
				}).render().start();
				</script>
    		</div>
		</div>
		
	<!--Contenido central-->
	    <div id="conten_central" class="conten_margin_top">
	   		 <h1><img src="<c:url value='/static/search/img/logo_hirubila_${rc.locale.language}.jpg'/>" alt="<fmt:message key='search.site.title'/>"/></h1>
	   		 <form:form id="searchForm" commandName="search" method="post" cssClass="buscador_general"> 
	         	<label for="caja_buscador"><fmt:message key="search.searchBox.label"/></label>
	            <input type="text" id="caja_buscador" name="query" placeholder="<fmt:message key='search.field.search.message'/>"/>
	            <form:hidden path="collection"/>
	            <p class="txt_ejemplo"><fmt:message key="search.field.search.example"/></p>
	            <p class="barra_submit">
            <input type="submit" value="<fmt:message key='continua.learning.type'/>" title="<fmt:message key='continua.learning.type'/>" onClick="$('#collection').val('hirubila-nonreglated-');"/>
            <input type="submit" value="<fmt:message key='oficial.learning.type'/>" title="<fmt:message key='oficial.learning.type'/>" onClick="$('#collection').val('hirubila-reglated-');return true;"/>
            </p>
	        </form:form>
	        <p class="txt_mediador"><fmt:message key="search.footer.message.part1"/> <a href="<c:url value='/${rc.locale.language}/search/mediators'/>"><fmt:message key="search.footer.message.part2"/></a>.</p>
	    </div>
	</div>
</body>
</html>