<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>		
	<%-- <link href="<c:url value='/static/style/tinymce_content.css'/>" rel="stylesheet" type="text/css" /> --%>
	<script type="text/javascript" src="<c:url value='/static/js/tiny_mce/tiny_mce.js'/>"></script>
	<script type="text/javascript">
		tinyMCE.init({
	        // General options
	        mode : "textareas",
	        theme : "advanced",
	        plugins : "pagebreak,style,layer,table,save,advhr,advimage,advlink,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,contextmenu,paste,directionality,noneditable,visualchars,nonbreaking,template",
	
	        theme_advanced_buttons1 : "mylistbox,mysplitbutton,bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,undo,redo,link,unlink",
	        theme_advanced_buttons2 : "",
	        theme_advanced_buttons3 : "",
	        theme_advanced_toolbar_location : "top",
	        theme_advanced_toolbar_align : "left",
	        theme_advanced_statusbar_location : "bottom",
	        force_p_newlines : true,
	        editor_selector : "mceAdvanced",
	        theme_advanced_resizing : true,
		});
		
		$(function(){
			// Datepicker
			$('#datepicker_start').datepicker({
				inline: true,
				dateFormat: "dd/mm/yy"
			});
			$('#datepicker_end').datepicker({
				inline: true,
				dateFormat: "dd/mm/yy"
			});
		});

		$(document).ready(function(){
			var ajax = true;
			$('#tags-dialog').dialog({
				title: 'Tags options',
				height: 600,
				width: 300,
				autoOpen: false, 
				buttons: {
					'OK' : 
						function() {
							var tagsText = '';
							$('#tags').val('');
							$.each($('#tags-dialog :checkbox:checked'), function(i, check){
								var tag = $.trim($(check).val()).toLowerCase();;
								tagsText = tagsText.concat(tag).concat(',');
							});
							$('#tags').val(tagsText);
							$('#tags-dialog').dialog('close');
						},
					'Cancel' : 
						function() {
							$('#tags-dialog').dialog('close');
						}
					},
			});
			
			$('#tagsOpenner').click(function(){
				var url = $('#tagsOpenner').attr('href');
				if(ajax) {
					getTags(url);
					ajax = false;
				} else {
					markTags();
				}
				$('#tags-dialog').dialog('open');

				return false;
			});
			
		$('#schools-dialog').dialog({
			title: '<fmt:message key="generic.action.school.selection.dialog"/>',
			autoOpen: false, 
			height: 300,
			width: 300,
			buttons: {
				'OK' : 
					function() {
						$('#school_desc').val('');
						$('#school_id').val('');
						var schoolName = $("#schools option:selected").text();
						var schoolId = $("#schools option:selected").val();
						if(schoolName != '' && schoolId != '') {
							$('#school_desc').val(schoolName);
							$('#school_id').val(schoolId);
						}
						$('#schools-dialog').dialog('close');
					},
				'Cancel' : 
					function() {
						$('#schools-dialog').dialog('close');
					}
				},
		});
		
		$('#schoolsOpenner').click(function(){
			var url = '/${rc.locale.language}/extended/course/ajax/provinces';
			$.getJSON(url, function(data){
				$("#provinces").empty();
				$("#provinces").append('<option selected="true" value=""/>');
				$('#towns').empty();
				$('#schools').empty();
				$.each(data, function(i, val){
					$("#provinces").append('<option value="' + val.id + '">' + val.name +'</option>');
				});	
				
			 });
			
			$('#schools-dialog').dialog('open');

			$("#provinces").change(function(){
				$('#towns').empty();
				$("#towns").append('<option selected="true" value=""/>');
				$('#schools').empty();
				$("#schools").append('<option selected="true" value=""/>');
				//$('#towns').change();
				var provinceId = $("#provinces").val();
				var url = '/${rc.locale.language}/extended/course/ajax/towns?provinceId='+provinceId;
				$.getJSON(url, function(data){	
					$.each(data, function(i, val){
						$("#towns").append('<option value="' + val.id + '">' + val.name +'</option>');
					});	
					
				 });
			});

			$("#towns").change(function(){
				$('#schools').empty();
				$("#schools").append('<option selected="true" value=""/>');
				//$('#schools').change();
				var townId = $("#towns").val();
				var url = '/${rc.locale.language}/extended/course/ajax/schools?townId='+townId;
				$.getJSON(url, function(data){	
					$.each(data, function(i, val){
						$("#schools").append('<option value="' + val.id + '">' + val.name +'</option>');
					});	
					
				 });
			});
			return false;
		});
	});
	</script>

		<h1><fmt:message key="course.add.section.header"/></h1>
		<form:form commandName="course" method="post">
			<form:hidden path="id"/>
			<form:hidden path="mediationService"/>
			<div class="form_settings">
				<form:errors path="school" cssClass="ui-state-error"/>
				<p><span><fmt:message key="course.fields.school"/></span>
					<c:if test="${not empty school}"><input id="school_desc" disabled="true" type="text" value="${school.name}" /></c:if>
					<c:if test="${empty school}"><input id="school_desc" disabled="true" type="text" /></c:if>
					<form:hidden id="school_id" path="school" />
					<a id="schoolsOpenner" href="#"><fmt:message key="course.fields.schools"/></a>
    			</p>
    			<form:errors path="title" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.title"/></span><form:input path="title"/></p>
           		<form:errors path="start" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.start"/></span><form:input path="start" id="datepicker_start"/></p>
            	<form:errors path="end" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.end"/></span><form:input path="end" id="datepicker_end"/></p>
            	
            	<form:errors path="information" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.information"/></span><form:textarea cssClass="mceAdvanced" path="information" rows="8" cols="70"/>
        		</p>
            	<form:errors path="timeTable" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.timetable"/></span><form:input path="timeTable"/></p>
            	<form:errors path="free" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.free"/></span><form:checkbox path="free"/></p>
            	<form:errors path="billMode" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.billMode"/></span><form:input path="billMode"/></p>
            	<form:errors path="tags" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.tags"/></span><form:input id="tags" path="tags"/><a id="tagsOpenner" href="/extended/course/ajax/tags"><fmt:message key='course.fields.tags'/></a></p>
            	
            	<p style="padding-top: 15px"><span>&nbsp;</span>
            	<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
            	</p>
            	
			</div>
			<%@ include file="/WEB-INF/views/common/dialog/tags-dialog.jsp"%>
			
			<div id="schools-dialog">
				<p><span><fmt:message key="territorial.fields.province"/></span>
					<select id="provinces">
					</select>
				</p>
				<p><span><fmt:message key="territorial.fields.town"/></span>
					<select id="towns">
					</select>
				</p>
				<p><span><fmt:message key="course.fields.school"/></span>
					<select id="schools">
					</select>
				</p>
			</div>
			
		</form:form>
