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
	<link href="<c:url value='/static/style/smoothness/jquery-ui-1.8.10.custom.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/static/search/js/javascript.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/ga.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/search.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/js/jquery-ui-1.8.10.custom.min.js'/>"></script>
	<style type="text/css">
		
		ui-dialog-titlebar {
			background-color:blue;
		}
		
		#dialog ul{
			list-style-type: square;
			font-family:"Arial", Helvetica, sans-serif;
			font-size:11px;
			padding-left:2%;
		}
		
		#dialog li {
			margin-bottom:5px;
		}
	
		
		.button, .button:visited { /* botones genéricos */
  			/*background: #222 url(<c:url value='/static/search/img/overlay.png'/>) repeat-x;*/
  			display: inline-block;
  			padding: 5px 10px 6px;
  			color: #FFF;
  			text-decoration: none;
  			-moz-border-radius: 6px;
  			-webkit-border-radius: 6px;
  			/*-moz-box-shadow: 0 1px 3px rgba(0,0,0,0.6);
  			-webkit-box-shadow: 0 1px 3px rgba(0,0,0,0.6);*/
  			text-shadow: 0 -1px 1px rgba(0,0,0,0.25);
  			border-bottom: 1px solid rgba(0,0,0,0.25);
  			position: relative;
  			cursor:pointer
		}

		.button:hover { /* el efecto hover */
  			background-color: #111
  			color: #FFF;
		}
		
		
		.button:active{  /* el efecto click */
  			top: 1px;
		}

 		/* botones pequeños */
		.small.button, .small.button:visited {
  			font-size: 11px ;
		}

 		/* botones medianos */
		.button, .button:visited,.medium.button, .medium.button:visited {
  			font-size: 13px;
  			font-weight: bold;
  			line-height: 1;
  			text-shadow: 0 -1px 1px rgba(0,0,0,0.25);
		}

 		/* botones grandes */
		.large.button, .large.button:visited {
  			font-size:14px;
  			padding: 8px 14px 9px;
		}

 		/* botones extra grandes */
		.super.button, .super.button:visited {
  			font-size: 34px;
  			padding: 8px 14px 9px;
		}
		
		.yellow.button, .yellow.button:visited { background-color: #518DCA; }
		.yellow.button:hover{ background-color: #9FAF55; }
		
		blockquote {
  			padding: 8px;
  			background-color: #518DCA;
 			border: 1px solid #518DCA;
  			margin-bottom: 5px;
  			border-radius:5px;
  			}
  		
  		blockquote span {
     		display: block;
     		background-repeat: no-repeat;
     		background-position: bottom right;
     		font-family: arial, helvetica, sans-serif;
     		font-size:11px;
     		line-height:12px;
     		color:#FFF;
     			}
   
	</style>
</head>


<script type="text/javascript">

	$.fx.speeds._default = 1000;
	$(function() {
		$( "#dialog" ).dialog({
			autoOpen: false,
			height: 250,
			width: 400,
			show: "blind",
			hide: "explode",
			position: ['right','center'],
			resizable: false,
			closeOnEscape: true,
			buttons: {
				"<fmt:message key='search.field.examples.close'/>": function() {
					$( this ).dialog( "close" );
				}
			},
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
			modal: false
		});

		$( "#opener" ).click(function() {
			$( "#dialog" ).dialog( "open" );
			return false;
		});
	});

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
		        	
		           	<a href="<c:url value='/${rc.locale.language}/search/mediators/'/>"><fmt:message key="search.mediators.net"/></a>
		           	
		            </li>
		            
		        </ul>
		        <%-- Actualmente: ${total} cursos indexados --%>
    		</div>
    		
    		
    	<!--Caja twitter con los términos de búsqueda "Formación País Vasco"-->
    		
    		<div class="conten_twitter">
    			<blockquote><span>  <fmt:message key="search.home.twitter.hashtags.title"/> <strong><fmt:message key="search.home.twitter.hashtags.formatted"/> </strong> </span></blockquote>
    			
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
				      background: '#518DCA',
				      color: '#FFF'
				    },
				    tweets: {
				      background: '#ffffff',
				      color: '#757475',
				      links: '#518DCA'
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
    		<script type="text/javascript" src="/_ah/channel/jsapi"></script>
    		<div id="channel_api_params" style="display:none;" chat_token="${token}"></div>
    		<div id="total_courses">
    			
    		</div>
		</div>
		
	<!--Contenido central-->
	    <div id="conten_central" class="conten_margin_top">
	   		 <h1><img src="<c:url value='/static/search/img/logo_hirubila_${rc.locale.language}.jpg'/>" alt="<fmt:message key='search.site.title'/>"/></h1>
	   		 <form:form id="searchForm" commandName="search" method="post" cssClass="buscador_general"> 
	         	<label for="caja_buscador"><fmt:message key="search.searchBox.label"/></label>
	            <input type="text" id="caja_buscador" name="query" placeholder="<fmt:message key='search.field.search.message'/>"/>
	            <form:hidden path="collection"/>
	            <p class="txt_ejemplo">
	            	<a href="#" class="medium button yellow" id="opener"><fmt:message key="search.field.search.example"/></a>
	            </p>
	            <p class="barra_submit">
            <input type="submit" value="<fmt:message key='continua.learning.type'/>" title="<fmt:message key='continua.learning.type'/>" onClick="$('#collection').val('hirubila-nonreglated-');"/>
            <input type="submit" value="<fmt:message key='oficial.learning.type'/>" title="<fmt:message key='oficial.learning.type'/>" onClick="$('#collection').val('hirubila-reglated-');return true;"/>
            </p>
	        </form:form>
	        <p class="txt_mediador"><fmt:message key="search.footer.message.part1"/> 
	        	<a href="<c:url value='/${rc.locale.language}/search/mediators/'/>"><fmt:message key="search.footer.message.part2"/></a>.
	        </p>
	    </div>
	</div>
	
	
	<div id="dialog" title='<fmt:message key="search.field.search.example"/>' >
		<ul>
			<li><fmt:message key="search.field.example.1"/></li>
			<li><fmt:message key="search.field.example.2"/></li>
			<li><fmt:message key="search.field.example.3"/></li>
			<li><fmt:message key="search.field.example.4"/></li>
			<li><span style="font-size:11px;"><fmt:message key="search.field.example.5"/></span></li>
		</ul>

	</div>
</body>
</html>