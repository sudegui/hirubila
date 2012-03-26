<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><fmt:message key="search.site.title"/></title>
	<link href="<c:url value='/static/search/css/reset.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/static/search/css/estilo.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/static/search/css/shadowbox.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/static/style/smoothness/jquery-ui-1.8.10.custom.css'/>" rel="stylesheet" type="text/css" />
	<link rel="image_src" href="<c:url value='/static/search/img/logo_hirubila_${rc.locale.language}.jpg'/>" />
	<script type="text/javascript" src="<c:url value='/static/search/js/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/javascript.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/ga.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/static/search/js/shadowbox.js'/>"></script>
	<script type="text/javascript">Shadowbox.init();</script>
	<script type="text/javascript" src="http://apis.google.com/js/plusone.js"></script>
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
	
$.fx.speeds._default = 400;
$(function() {
	$( "#opener" ).click(function() {
		$( "#dialog" ).dialog( "open" );
		return false;
	});
});

	$(document).ready(function() {
		var type = '${search.collection}';
		if(type == 'hirubila-reglated-') {
			$('#oficial').css('background-color','#BBDCFF');
		} else if (type=='hirubila-nonreglated-') {
			$('#continua').css('background-color','#BBDCFF');
		} else {
			$('#todo').css('background-color','#BBDCFF');
		}
		$("a[name=result-link]").each(function(){
			$(this).attr("target", "_blank");
		});

		$('#todo').click(function() {
			$('#collection').val('hirubila-');
			$('#todo').css('background-color','#BBDCFF');			
			$('#continua').css('background-color','');
			$('#oficial').css('background-color','');
			$('#target').submit();
		});

		$('#continua').click(function() {
			$('#collection').val('hirubila-nonreglated-');
			$('#continua').css('background-color','#BBDCFF');			
			$('#todo').css('background-color','');
			$('#oficial').css('background-color','');
			$('#target').submit();
		});

		$('#oficial').click(function() {
			$('#collection').val('hirubila-reglated-');
			$('#oficial').css('background-color','#BBDCFF');			
			$('#todo').css('background-color','');
			$('#continua').css('background-color','');
			$('#target').submit();
		});
		
	});

	
</script>

<body>
<div id="contenedor">
	<!--Caja buscador arriba-->
	<div id="buscador_top">
	    <h1>
	     <a href="<c:url value='/${rc.locale.language}/search/'/>"><img src="<c:url value='/static/search/img/logo_hirubila_dos_${rc.locale.language}.jpg'/>" alt="Logo Hirubila" title="<fmt:message key='search.logo.title'/>" /></a> 
	     </h1>
     	<form:form commandName="search" action="/${rc.locale.language}/search" method="post" cssClass="buscador_general" id="target">
			<label for="caja_buscador"><fmt:message key="search.searchBox.label"/></label>
			<form:hidden path="collection"/>
            <input type="text" id="caja_buscador" name="query" placeholder="<fmt:message key='search.field.search.message'/>" value="${search.query}"/>
            <input type="submit" value='<fmt:message key="search.action.search"/>' title='<fmt:message key="search.action.search"/>'/>
      	</form:form>      	
		<span class="fin_caja">&nbsp;</span>
		
		<p class="resulta_mini">${paginator.collection.totalResults} <fmt:message key="search.results.total"/></p>
  	</div>
  	
<div id="conten_izq">
<!--Menú izquierda-->
<div id="nav" >
    	<ul>
    		<li class="todo" id="todo">
           				<fmt:message key='all.learning.type'/>
            		</li>
            		<li class="continua" id="continua">
           				<fmt:message key='continua.learning.type'/>
            		</li>
            		<li class="oficial" id="oficial">
           				<fmt:message key='oficial.learning.type'/>
            </li>
            <br/>
    		<hr/>
    		<br/>		
        	<li class="red">
           	<a href="<c:url value='/${rc.locale.language}/search/mediators/'/>"><fmt:message key="search.mediators.net"/></a>
           	<p style="font-size: 0.9em;">Consulta al mediador de tu zona</p>
            </li>
            <!--  
            <li class="guiada">
           	<a href="<c:url value='/${rc.locale.language}/search/advanced'/>"><fmt:message key="search.advanced.search"/></a>
            </li>
            -->
        </ul>
    </div>
    <!--Caja twitter con los términos de búsqueda "Formación País Vasco"-->
    <div class="conten_twitter">
    	<blockquote><span>  <fmt:message key="search.home.twitter.hashtags.title"/> <strong><fmt:message key="search.home.twitter.hashtags.formatted"/></strong> </span></blockquote>
    	<script src="http://widgets.twimg.com/j/2/widget.js"></script>
<script>
new TWTR.Widget({
  version: 2,
  type: 'search',
  search: '<fmt:message key="search.results.twitter.hashtags"/>',
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
</div>
<!--Contenido central - Listado de resultados-->
    <div id="conten_central">
    	<ul>
    		<c:forEach items="${paginator.collection}" var="result" varStatus="status">
    			<li>
    				<h2><a name="result-link" href="${result.link}">${result.title}</a></h2>
    				<p>${result.description}</p>
                	<div class="barra_enlaces"> 
                		<p>                               
		                   <a href="<c:url value='/${rc.locale.language}/search/results/send?link=${result.link}'/>" rel="shadowbox;height=468;width=400" class="envia_mail" target="_blank"><fmt:message key="search.result.sendByEmail"/></a> - 
		                   <a id="link_compartir_${status.count}" class="comparte_link" onClick="muestra_oculta('${status.count}')" ><fmt:message key="search.result.share"/></a> - 
		                   <a href="<c:url value='/${rc.locale.language}/search/results/error'/>" rel="shadowbox;height=300;width=400" ><fmt:message key="search.result.send.error"/></a>
              			</p>
              			<div class="caja_compartir" id="caja_compartir_${status.count}" style="display:none">
              				
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
              				
              				
                    	</div>
              		</div>
            	</li>
			</c:forEach>
		</ul>            

        <!--Paginado-->
        <div class="hiru_paginado">
	        <ul>
				<c:if test="${paginator.currentPage > paginator.pageStart}">
		       	  	<li><!--Si el anterior o posterior no tiene enlace al li se le aplica la clase no_ante_poste-->
		          		<a href="<c:url value='/${rc.locale.language}/search?q=${search.query}&page=${paginator.currentPage-1}&partialfields=${search.inMeta}'/>"><fmt:message key="pagination.before"/></a>
		          	</li>
	          	</c:if>
	          	<c:if test="${paginator.currentPage == paginator.pageStart}">
		       	  	<li class="no_ante_poste"><!--Si el anterior o posterior no tiene enlace al li se le aplica la clase no_ante_poste-->
		          		<fmt:message key="pagination.before"/>
		          	</li>
	          	</c:if>
	           
				<c:forEach items='${paginator.pagesIterator}' var='page'>
					<c:if test="${page == paginator.currentPage}">
						<li class="page_present"><!--page_present, clase para la página en la que estás-->
	         	 			${page}
	          			</li>
					</c:if>
					<c:if test="${page != paginator.currentPage}">
						<li>
							<a href="<c:url value='/${rc.locale.language}/search?q=${search.query}&page=${page}&partialfields=${search.inMeta}&collection=${search.collection}'/>" title="${page}">${page}</a>
						</li>
					</c:if>
				</c:forEach>
				
		        <c:if test="${paginator.currentPage < paginator.pageEnd}">
			    	<li>
			        	<a href="<c:url value='/${rc.locale.language}/search?q=${search.query}&page=${paginator.currentPage+1}'/>&partialfields=${search.inMeta}&collection=${search.collection}"><fmt:message key="pagination.next"/></a>
			        </li>
			    </c:if>
			    <c:if test="${paginator.currentPage == paginator.pageEnd}">
			    	<li>
			        	<fmt:message key="pagination.next"/>
			        </li>
			    </c:if>
	    	</ul>
        </div>
        
      <p class="txt_mediador"><fmt:message key="search.footer.message.part1"/> <a href="<c:url value='/${rc.locale.language}/search/mediators'/>"><fmt:message key="search.footer.message.part2"/></a>.</p>
    </div>
</div>
</body>
</html>