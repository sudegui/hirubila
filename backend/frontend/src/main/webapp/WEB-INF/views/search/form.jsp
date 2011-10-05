<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


			<script type="text/javascript">
				$(document).ready(function() {
					$("#query").autocomplete({
					        minChars:3,  
					        autoFill:true, 
					        mustMatch:true, 
					        cacheLength:100, 
					        max:20, 
						//define callback to format results
						source: function(req, add){
							//pass request to server
							var url = '/${rc.locale.language}/search/list/json?query='+req.term;
							$.getJSON(url,req, function(data) {
								//create array for response objects
								var suggestions = [];
								//process response
								 $.each(data, function(i, val){
									 suggestions.push(val.phrase);
								  });

								//pass array to callback
								 add(suggestions);
							 });
						},
						//define select handler
					 	select: function(e, ui) {
							//create formatted friend
					 		var friend = ui.item.value;
					 		
			            },
			          //define select handler
			            change: function() {
			                //prevent 'to' field being updated and correct position
			          		//alert("Cahnge!");
			            }
					});
					
					$("#results a").each(function(){
						var href = $(this).attr("href");
						var base = '/${rc.locale.language}/search/dispatch';
						var link = base + "?resultUrl=" + href;
						$(this).attr("href", link);
					});
				});
			</script>

		
			<h1><fmt:message key="search.section.header"/></h1>	
			
		<form:form commandName="search" method="POST">
			<table width="100%" border="0">
					
	    			<tr>
	    				
	    					<td width="100"><form:input id="query" path="query" /></td>
	    				
	    				<td><input type="submit" value="<fmt:message key='search.action.search'/>" title="<fmt:message key='search.action.search'/>"/></td>
	       			</tr>
			</table>  
			
		</form:form>
		
	<c:if test="${not empty paginator.collection}">
		<div id="results">
			<h5>Aproximadamente  ${paginator.collection.totalResults} resultados en ${paginator.collection.totalTime} segundos</h5> <%-- TODO use I18n --%>
				
			<c:if test="${not empty paginator.collection}">	
			 	<c:forEach items="${paginator.collection}" var="result">
					[${result.mime}] - [${result.lang}] <a href="${result.link}">${result.title}</a>
					<br>
					<p>${result.description}</p>
				</c:forEach>
				<br>
				
				<br>
    		</c:if>
    	</div>	
    	<%-- START PAGINATION --%>
				<table>
					<tr>
						<c:if test="${paginator.currentPage != paginator.pageStart}">
							<td><a href="<c:url value='/${rc.locale.language}/search?q=${search.query}&page=${paginator.currentPage-1}'/>">Anterior</a></td>
						</c:if>
						<c:forEach items='${paginator.pagesIterator}' var='page'>
	      						<c:if test="${page == paginator.currentPage}">
	      							<td>${page}</td>
	      						</c:if>
	      						<c:if test="${page != paginator.currentPage}">
	      							<td></td>
	      						</c:if>
	    				</c:forEach>
	    				<c:if test="${paginator.currentPage != paginator.pageEnd}">
							<td><a href="<c:url value='/${rc.locale.language}/search?q=${search.query}&page=${paginator.currentPage+1}'/>">Siguiente</a></td>
						</c:if>
	    			</tr>
	    		</table>
	</c:if>
	<c:if test="${empty paginator.collection}">
		<c:if test="${not empty search.query}"><fmt:message key="search.results.empty" /><h4>${search.query}</h4></c:if>
	</c:if>
