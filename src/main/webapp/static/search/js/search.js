$(document).ready(function(){

	var chat_token = $('#channel_api_params').attr('chat_token');
	alert(chat_token);
	var channel = new goog.appengine.Channel(chat_token);
	var socket = channel.open();
	socket.onopen = function(){};
	socket.onmessage = function(m){
						
						var data = $.parseJSON(m.data);
						//alert($('#total_courses').html());
						$('#total_courses').empty();
						$('#total_courses').append(data['html']);
						$('#total_courses').append(m);
					};
    socket.onerror = function(err){
    					alert("Error => " + err.description);
    				};
    
    socket.onclose = function(){
    		alert("channel closed");
    };

    
});