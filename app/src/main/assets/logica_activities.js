/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

var markers = [];
var markers_gps = [];
var circles_gps = [];
var markerCluster = null;
var map = null; //VARIABLE GENERAL PARA EL MAPA
var center = null;
var infowindows = [];
var auxclick = 0;

function initialize(){
    console.log("initialize()");

	var latitude = 6.24022;
	var longitude = -75.577698;
	center = new google.maps.LatLng(latitude, longitude);
	map = new google.maps.Map(document.getElementById('map'), {
		zoom: 12,
		center: center,
		disableDefaultUI: true,
		zoomControl: true,
		//mapTypeId: google.maps.MapTypeId.ROADMAP
	});

	$("#map").on("click",function(){
		cerrarInfowindows();
	});

	// No aparecen infowindows.
	fixInfoWindow();
}

function fixInfoWindow() {
    //Here we redefine set() method.
    //If it is called for map option, we hide InfoWindow, if "noSupress" option isnt true.
    //As Google doesn't know about this option, its InfoWindows will not be opened.
    var set = google.maps.InfoWindow.prototype.set;
    google.maps.InfoWindow.prototype.set = function (key, val) {
        if (key === 'map') {
            if (!this.get('noSupress')) {
                console.log('This InfoWindow is supressed. To enable it, set "noSupress" option to true');
                return;
            }
        }
        set.apply(this, arguments);
    }
}

//FUNCION PARA QUITAR MARCADORES DE MAPA
function limpiar_marcadores(lista){
    for(i in lista)
    {
        //QUITAR MARCADOR DEL MAPA
        lista[i].setMap(null);
    }
}

function limpiar_infowindows(lista){
    lista = [];
}

function getAuxClick() {
    return auxclick;
}

function cerrarInfowindows(){
	for(i in infowindows)
    {
        //QUITAR MARCADOR DEL MAPA
        infowindows[i].close();
    }
}

mostrarMarcadores = function(activities){
	
	//Android.showDialog(activities.items[0].info.nombre+"");	
	if(parseInt(activities.totalItems) != 0){
		// Borrar Marcadores
		limpiar_marcadores(markers);
		limpiar_infowindows(infowindows);
		markerCluster = null;
		
		for(var i=0;i<activities.totalItems;i++){
			var activity = activities.items[i];

			var id = activity.id;
			var cx_latitude = activity.latitude;
			var cy_longitude = activity.longitude;
			var info = activity.info;
			var ofertas = activity.ofertas;
			var is_active = activity.is_active;
			
			//Android.showDialog(id+" "+cx_latitude+" "+cy_longitude+" "+descripcion+" "+info.nombre+" "+info.barrio.nombre+" "+is_active+" "+imagen_url);	

			var coordenada = new google.maps.LatLng(cx_latitude, cy_longitude);
		    
		    var marker = new google.maps.Marker({
		           //titulo:prompt("Titulo del marcador?"),
		           position:coordenada,
		           map: map, 
		           //animation:google.maps.Animation.DROP,
		           //animation:google.maps.Animation.BOUNCE, 
		           draggable:false,
		       	   //icon: img,
		       	   id: i,
                   title: info.nombre,
		    });

			var infowindowId = "info-"+i;
			var infowindow = new google.maps.InfoWindow({
				  	content: "<div id='"+ infowindowId +"' class='iw-container'>"+info.nombre+'</div>',
				  	enableEventPropagation: true,
				  	noSupress: true, //<-- here we tell InfoWindow to bypass our blocker
				 });

			infowindow.close();
		    infowindows.push(infowindow);

		    bindInfoWindow(marker, map);
		    
		   //ALMACENAR UN MARCADOR EN EL ARRAY markers
		   	markers.push(marker);
		}

		markerCluster = new MarkerClusterer(map, markers);

	}

}

function bindInfoWindow(marker, mapa) {

    google.maps.event.addListener(marker, 'click', function() {
    	//Android.showDialog("Dialog "+aux);
      	cerrarInfowindows();
		mapa.setCenter(this.getPosition());
		for (var j=0;j<markers.length;j++) {
			if(markers[j] == this){
			   var infoId = "info-"+j;
			   infowindows[j].open(mapa,this);
			   google.maps.event.addListener(infowindows[j], 'domready', function() {
			   	   $("#"+ infoId).click(function() {
						var position = $(this).attr("id").split("-");
				   		//Android.showDialog("Dialog "+position[1]);
				   		/*var latitude = parseFloat(markers[position[1]].getPosition().lat());
					   	var longitude = parseFloat(markers[position[1]].getPosition().lng());
					   	var name = markers[position[1]].getTitle()+"";
					   	//Android.showDialog("Dialog " + " " + latitude + " " + longitude + " " + name);*/
						Android.onActivitiesItemInfoWindowClick(parseInt(position[1]));
				   });

				   // Reference to the DIV that wraps the bottom of infowindow
				    var iwOuter = $('.gm-style-iw');

				    /* Since this div is in a position prior to .gm-div style-iw.
				     * We use jQuery and create a iwBackground variable,
				     * and took advantage of the existing reference .gm-style-iw for the previous div with .prev().
				    */
				    var iwBackground = iwOuter.prev();

				    // Removes background shadow DIV
				    iwBackground.children(':nth-child(2)').css({'display' : 'none'});

				    // Removes white background DIV
				    iwBackground.children(':nth-child(4)').css({'display' : 'none'});

				    // Moves the infowindow 10px to the right.
				    iwOuter.parent().parent().css({left: '0px'});
				    // Moves the infowindow 10px to the top.
				    iwOuter.parent().parent().css({top: '25px'});
				    
				    // Moves the shadow of the arrow 76px to the left margin.
				    //iwBackground.children(':nth-child(1)').attr('style', function(i,s){ return s + 'left: 76px !important;'});

				    // Moves the arrow 76px to the left margin.
				    //iwBackground.children(':nth-child(3)').attr('style', function(i,s){ return s + 'left: 576px !important;'});
				    iwBackground.children(':nth-child(3)').css({'display' : 'none'});
				    iwBackground.children(':nth-child(1)').css({'display' : 'none'});

				    // Changes the desired tail shadow color.
				    //iwBackground.children(':nth-child(3)').find('div').children().css({'box-shadow': 'rgba(72, 181, 233, 0.6) 0px 1px 6px', 'z-index' : '1'});

				    // Reference to the div that groups the close button elements.
				    var iwCloseBtn = iwOuter.next();

				    // Apply the desired effect to the close button
				    iwCloseBtn.css({'display' : 'none', opacity: '1', right: '38px', top: '3px', border: '7px solid #48b5e9', 'border-radius': '13px', 'box-shadow': '0 0 5px #3990B9'});
			   });
			}
		}

    });
}


mostrarMiUbicacion = function(mLatitude,mLongitude){
	var coordenada = new google.maps.LatLng(parseFloat(mLatitude), parseFloat(mLongitude));

	limpiar_marcadores(markers_gps);
	limpiar_marcadores(circles_gps);

	var marker = new MarkerWithLabel({
	    position: coordenada,
	    icon: {
	      path: google.maps.SymbolPath.CIRCLE,
	      scale: 0, //tamaño 0
	    },
	    map: map,
	    draggable: true,
	    labelAnchor: new google.maps.Point(10, 10),
	    labelClass: "labelMarker", // the CSS class for the label
	});
		    
    var gpsCircle = new google.maps.Circle({
		strokeColor: '#40A738',
		strokeOpacity: 0.5,
		strokeWeight: 0.2,
		fillColor: '#40A738',
		fillOpacity: 0.05,
		map: map,
		center: {lat: mLatitude, lng: mLongitude},
		radius: Math.sqrt(5) * 10
	});

    markers_gps.push(marker);
    circles_gps.push(gpsCircle);

    map.setCenter(marker.getPosition());
    map.setZoom(17);

}

actualizarMiUbicacion = function(mLatitude,mLongitude){
	var coordenada = new google.maps.LatLng(parseFloat(mLatitude), parseFloat(mLongitude));

	limpiar_marcadores(markers_gps);
	limpiar_marcadores(circles_gps);
		    
    var marker = new MarkerWithLabel({
	    position: coordenada,
	    icon: {
	      path: google.maps.SymbolPath.CIRCLE,
	      scale: 0, //tamaño 0
	    },
	    map: map,
	    draggable: true,
	    labelAnchor: new google.maps.Point(10, 10),
	    labelClass: "labelMarker", // the CSS class for the label
	});

    var gpsCircle = new google.maps.Circle({
		strokeColor: '#40A738',
		strokeOpacity: 0.5,
		strokeWeight: 0.2,
		fillColor: '#40A738',
		fillOpacity: 0.05,
		map: map,
		center: {lat: mLatitude, lng: mLongitude},
		radius: Math.sqrt(5) * 10
	});

    markers_gps.push(marker);
    circles_gps.push(gpsCircle);
}

