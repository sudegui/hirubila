// JavaScript Document
//Ocultar/mostrar Compartir
 function muestra_oculta(id){
    if (document.getElementById){ //se obtiene el id
		var el = document.getElementById('caja_compartir_'+id); //se define la variable "el" igual a nuestro div
		var link = document.getElementById('link_compartir_'+id);
		if(el != null) el.style.display = (el.style.display == 'none') ? 'block' : 'none'; //damos un atributo display:none que oculta el div
		if(link != null) link.className = (link.className == 'comparte_link') ? 'comparte_link_top' : 'comparte_link'; //damos un atributo display:none que oculta el div
    }
 }

window.onload = function(){/*hace que se cargue la función lo que predetermina que div estará oculto hasta llamar a la función nuevamente*/
	muestra_oculta('caja_compartir');/* "contenido_a_mostrar" es el nombre que le dimos al DIV */
}

   
//