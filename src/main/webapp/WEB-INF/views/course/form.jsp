<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>		
		
		<script type="text/javascript">
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
						autoOpen: false, 
						height: 600,
						width: 300,
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
						title: 'Schools selection',
						autoOpen: false, 
						height: 600,
						width: 300,
						buttons: {
							'OK' : 
								function() {
									$('#school_desc').val('');
									$('#school_id').val('');
									var schoolId = $("input[name='schoolRadio']:checked").val();
									var schoolName = $("#" + schoolId).text();
									if(schoolName != '' && schoolId != '') {
										$('#school_name').val(schoolName);
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
					
					$("#schoolsOpenner").click(function(){
						$('#schools-dialog').dialog('open');
					});

					$("#startLetter").change(function(){
						$("#schoolsTable > tbody").empty();
						var letter = $("#startLetter").val();
						if(letter != '') {
							var url = '/${rc.locale.language}/course/ajax/schools?letter='+letter;
							$.getJSON(url, function(data){							
								$.each(data, function(i, val){
									var city = val.contactInfo['city'] != null && val.contactInfo['city'] != 'null' ? val.contactInfo['city'] : "";
									var country = val.contactInfo['country'] != null && val.contactInfo['country'] != 'null' ? val.contactInfo['country'] : "";
									var row = '<tr><td><input type="radio" name="schoolRadio" class="schoolRadio" value="' + val.id + '"/></td><td id="' + 
									val.id + '">' + val.name + '</td><td>' + city + '</td><td>' + country + '</td></tr>';
									$("#schoolsTable > tbody").append(row);
								});
							});
						}
					});

					$("input[name='schoolRadio']").change(function(){
						$('#school_desc').val('');
						$('#school_id').val('');
					});
				});
				
		</script>

		<h1><fmt:message key="course.add.section.header"/></h1>
		<form:form commandName="course" method="post">
			<form:hidden path="id"/>
			<form:hidden path="externalId"/>
			<form:hidden path="provider"/>
			<div class="form_settings">
    			<form:errors path="school" cssClass="ui-state-error"/>				
				<p>
					<span><fmt:message key="course.fields.school"/></span>
					
					<c:if test="${not empty school}"><input id="school_name" disabled="true" type="text" value="${school.name}" /></c:if>
					<c:if test="${empty school}"><input type="text" id="school_name" disabled="true"/></c:if>
					<form:hidden id="school_id" path="school"/>
					<a id="schoolsOpenner" href="#">Centros</a>
				</p>
    			
    			<form:errors path="title" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.title"/></span><form:input path="title"/></p>
           		<form:errors path="start" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.start"/></span><form:input path="start" id="datepicker_start"/></p>
            	<form:errors path="end" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.end"/></span><form:input path="end" id="datepicker_end"/></p>
            	<form:errors path="information" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.information"/></span><form:textarea path="information" rows="8" cols="70"/></p>
            	<form:errors path="url" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.url"/></span><form:input path="url"/></p>
            	
            	<form:errors path="tags" cssClass="ui-state-error"/>
            	<p><span><fmt:message key="course.fields.tags"/></span><form:input id="tags" path="tags"/><a id="tagsOpenner" href="/course/ajax/tags"><fmt:message key='course.fields.tags'/></a></p>
            	<p style="padding-top: 15px"><span>&nbsp;</span>
            	
            	<input class="submit" type="submit" value="<fmt:message key='generic.submit.message'/>" title="<fmt:message key='generic.submit.message'/>"/>
            	</p>
			</div>
		</form:form>
		
		<div id="schools-dialog">
			<p>
				<select id="startLetter">
					<option value=""></option>
					<option value="A">A</option>
					<option value="B">B</option>
					<option value="C">C</option>
					<option value="D">D</option>
					<option value="E">E</option>
					<option value="F">F</option>
					<option value="G">G</option>
					<option value="H">H</option>
					<option value="I">I</option>
					<option value="J">J</option>
					<option value="K">K</option>
					<option value="L">L</option>
					<option value="M">M</option>
					<option value="N">N</option>
					<option value="Ñ">Ñ</option>
					<option value="O">O</option>
					<option value="P">P</option>
					<option value="Q">Q</option>
					<option value="R">R</option>
					<option value="S">S</option>
					<option value="T">T</option>
					<option value="U">U</option>
					<option value="V">V</option>
					<option value="W">W</option>
					<option value="X">X</option>
					<option value="Y">Y</option>
					<option value="Z">Z</option>
				</select>
			</p>
			<table id="schoolsTable">
				<thead>
					<tr>
						<th></th>
						<th><fmt:message key="school.fields.name"/></th>
						<th><fmt:message key="contactInfo.fields.city"/></th>
						<th><fmt:message key="contactInfo.fields.country"/></th>
					</tr>
				</thead>
				<tbody>
				</tbody>				
			</table>
		</div>
	<%@ include file="/WEB-INF/views/common/dialog/tags-dialog.jsp"%>