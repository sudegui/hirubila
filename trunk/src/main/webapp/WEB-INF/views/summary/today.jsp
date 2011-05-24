<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<!--Load the AJAX API-->
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
google.load('visualization', '1', {'packages':['corechart']});
google.setOnLoadCallback(init);
var chart;

function init() {
	chart = new google.visualization.PieChart(document.getElementById('summaryChart'));
	if(${dumpId} != '') {
		updateChart(${dumpId});
		loadTableData();
	}
}

function updateChart(id) {
	var url = '/${rc.locale.language}/summary/today/ajax/dump/'+id;
	$.getJSON(url, function(dump){

		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Type');
		data.addColumn('number', 'Results');
		
		data.addRow(['Parse Errors', dump[0]]);
		data.addRow(['Validation Errors', dump[1]]);
		data.addRow(['Successful', dump[2]]);

		
		chart.draw(data, {width: 400, height: 240, is3D: true, title: '<fmt:message key="summary.chart.title"/>: '+id, enableEvents:true});
		// Assign  event  handler
        google.visualization.events.addListener(chart, 'select', function mouseEventHandler()  {
        	var selection = chart.getSelection();
        	var typeEvent = null;
        	var url = '/${rc.locale.language}/dump/' + id + '/events/';
        	for (var i = 0; i < selection.length; i++) {
        	    var item = selection[i];
        	    if (item.row != null) {
        	      typeEvent = item.row;
        	    }
        	  }
        	  if (typeEvent == 0) {
        	    window.location = url + 'parser-error/';
        	  } else if(typeEvent == 1) {
        		  window.location = url + 'store-error/';
        	  } else if(typeEvent == 2) {
        		  window.location = url + 'store-success/';
        	  }
        });
		
	});	
}



function addRow(row) {
	var tableBody = $("#historical tbody");
	var link = '<a href="#" onclick="updateChart('+ row.id +');">' + row.id + '</a>'	
	var date = row.date.getDate() + "/" + (row.date.getMonth()+1) + "/" + row.date.getFullYear();
	tableBody.append("<tr><td>" + link + "</td><td>" + date + "</td><td>" + row.description + "</td></tr>");
}

function loadTableData() {
	var url = '/${rc.locale.language}/summary/today/ajax/table/${providerId}';
	$.getJSON(url, function(data){
		$.each(data.collection, function(i, val){
			addRow(new rowData(val.id, val.launched, val.description));
		});	
		// Make pagination
		//var pages = 
	});
}

function rowData(id, date, description) {
	this.id = id;
	this.date = new Date(date);
	this.description = description;
}
</script>
    
<h1><fmt:message key="summary.section.header"/></h1>


 <!--Div that will hold the pie chart-->
    <div id="summaryChart"></div>
    <div id="debugger"></div>
    
    <c:if test="${dumpId != null}">
	    <div id="historicalTable">
	    	<table id="historical">
	    		<thead>
		    		<tr>
		    			<th><fmt:message key="summary.table.fields.id"/></th>
		    			<th><fmt:message key="summary.table.fields.launchedDate"/></th>
		    			<th><fmt:message key="summary.table.fields.description"/></th>
		    		</tr>
	    		</thead>
	    		<tbody>
	    		</tbody>
	    	</table>
	    	<div id="pagination">
	    	</div>
	    </div>
	</c:if>
	<c:if test="${dumpId == null}">
		<p><fmt:message key="summary.message.noDumps"/></p>
	</c:if>