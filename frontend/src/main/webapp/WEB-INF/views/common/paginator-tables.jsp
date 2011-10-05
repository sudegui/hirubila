<%@ include file="/WEB-INF/views/common/includes.jsp" %>

<c:if test="${paginator.size > 0 && (paginator.pageStart < paginator.pageEnd)}">
<table>
	<tr>
	<c:if test="${paginator.currentPage != paginator.pageStart}">
		<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage-1}&order=${order}'/>">Anterior</a></td>
	</c:if>
	<c:forEach items='${paginator.pagesIterator}' var='page'>
  						<c:if test="${page == paginator.currentPage}">
  							<td>${page}</td>
  						</c:if>
  						<c:if test="${page != paginator.currentPage}">
  							<td><a href="<c:url value='${paginator.urlBase}?page=${page}&order=${order}'/>" title="Schools">${page}</a></td>
  						</c:if>
				</c:forEach>
				<c:if test="${paginator.currentPage != paginator.pageEnd}">
		<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage+1}&order=${order}'/>">Siguiente</a></td>
	</c:if>
	</tr>
</table>	
</c:if>