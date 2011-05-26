<script type="text/javascript" src="<c:url value='/static/js/jquery-1.5.1.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/static/js/jquery-ui-1.8.10.custom.min.js'/>"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="<c:url value='/static/js/jquery.chainedSelects.js'/>"></script>

<script type="text/javascript">

	var languages = [];

	<c:forEach items="${langs}" var="lang">
		languages.push(new language('<fmt:message key="${lang.key}"/>', '${lang.key}'));
	</c:forEach>

	
	
	function language(desc, lang) {
		this.desc = desc;
		this.lang = lang;	
	}

	
	function getTags(url) {
		var url = '/${rc.locale.language}' + url;
		$.getJSON(url, function(data){	
			$.each(data, function(i, val){
				var tagVal = val.category.trim().toLowerCase()
				var check = '<input type="checkbox" value=\'' + tagVal + '\'/> ' + tagVal + '<br>';
				$('#tags-dialog').append(check);
				if(data.length-1 == i) {
					<%-- TODO same code than markTags. Try to unify --%>
					var tags = [];
					var tagsText = $('#tags').val().trim();
					if(tagsText != null) tags = tagsText.split(',');
					$('#tags-dialog input:checkbox').removeAttr('checked');
					$.each(tags, function(i, tag){
						var checks = $('#tags-dialog input:checkbox');
						$.each(checks, function(j, check){
							var checkTag = $(check);
							if(tag.trim().toLowerCase() == checkTag.val()) checkTag.attr('checked', 'checked');
						});
					});
				}
			});	
		 });
	}

	
	
	function markTags() {
		var tags = [];
		var tagsText = $('#tags').val().trim();
		if(tagsText != null) tags = tagsText.split(',');
		$('#tags-dialog input:checkbox').removeAttr('checked');
		$.each(tags, function(i, tag){
			var checks = $('#tags-dialog input:checkbox');
			$.each(checks, function(j, check){
				var checkTag = $(check);
				if(tag.trim().toLowerCase() == checkTag.val()) checkTag.attr('checked', 'checked');
			});
		});
	}

	
	
	$(document).ready(function(){

		
		$('#multilanguage-dialog').dialog({
			title: '<fmt:message key="multilanguage.action.edit.dialog.title"/>',
			autoOpen: false
		});
		
		
		$(".expand-menu").click(function(){
			$('.content-options-menu', $(this).parent()).slideToggle("slow");
		});

		$("table td:.description-collapse").each(function(i, row){
			cutAndHide(row, i);
		});

		$("a:.edit-multilanguage-link").click(function(){
			$("#multilanguage-dialog ul").empty();
			var itemLink = $(this).attr('href');
			for(var i=0; i < languages.length; i++) { 
				var langLink = '<li><a href="' + '<c:url value="/" />' + languages[i].lang + itemLink + '">' + languages[i].desc + '</a></li>'; 
				$("#multilanguage-dialog ul").append(langLink);
			}
			$('#multilanguage-dialog').dialog('open');
			return false;
		});

		<%-- Adds a link into th header to sort table with the order param --%>
		$("table thead th:.sortable").each(function(i, column){
			var orderActual = '${order}';
			var orderParam = $(this).attr('order');
			var descColumn = $(this).text();
			if(orderActual != null && orderActual != '' && orderActual == orderParam) {
				orderParam = '-' + orderParam;
			}
			var link = '<a href="?order=' + orderParam + '">' + descColumn + '</a>';
			$(this).text('');
			$(this).append(link);
		});
		
		var selectedMenu = "${menuId}";
		$("#" + selectedMenu).addClass('selected');
		
	});

	function cutAndHide(row, i) {
		$("table td:.description-collapse a:.hide-row-"+i).remove();
		var oldDesc = $(row).html();
		var newDesc = '';
		if(oldDesc.length > 100) {
			var moreMessage = 'More';
			var moreLink = '<a class="more-row-'+ i + '" href="#row-' + i + '">' + moreMessage + '</a>';
			var divMore = '<div id="row-' + i + '">' + oldDesc + '</div>';
			newDesc = oldDesc.substring(0, 100) + '...' + moreLink + divMore;
			$(row).html(newDesc);

			$("#row-"+i).hide();
			
			$("table td:.description-collapse a:.more-row-"+i).click(function(){
				var hideMessage = 'Hide';
				var hideLink = '<br><a class="hide-row-'+ i + '" href="#row-' + i + '">' + hideMessage + '</a>';
				$(row).html($("#row-"+i).html() + hideLink);
				$("#row-"+i).slideToggle("slow");

				$("table td:.description-collapse a:.hide-row-"+i).click(function(){
					cutAndHide(row, i);
				});
			});
		}
	}
</script>