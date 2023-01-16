let splash = document.querySelector('.splash');

document.addEventListener('DOMContentLoaded',(e) => {
	setTimeout(() => {
		splash.classList.add('display-none');
	},2000)
})

function openTab(elmnt) {
	formTab = document.getElementById("smartwizard");
	mapTab = document.getElementById("iframeDiv");
  if (elmnt === 'mapTab') {
	  formTab.style.display = "none";
	  mapTab.style.display = "block";
	  document.getElementById("formBtn").classList.remove("active");
	  let iframeMap = document.getElementById('mapIframe');
	  let url = (urlParams.get('search') !== null) ? '/map/map.html?search=' + urlParams.get('search') : '/map/map.html';
	  if (iframeMap.getAttribute("src") === "about:blank") iframeMap.setAttribute("src",url);
	  document.getElementById("mapBtn").classList.add("active");
  }
  if (elmnt === 'formTab') {
	  formTab.style.display = "block";
	  mapTab.style.display = "none";
	  document.getElementById("formBtn").classList.add("active");
	  document.getElementById("mapBtn").classList.remove("active");
	  map.invalidateSize();
  }
}

/* Map setings */
async function bordersGeoJson() {
    const response = await fetch("/data/border");
    const data = await response.json();
        borders.addData(data);
        defBounds = borders.getBounds();
        defCenter = borders.getBounds().getCenter();
        map.fitBounds(borders.getBounds());
}
	
async function roadsGeoJson() {
    const response = await fetch("/data/roads");
    const data = await response.json();
    roads.addData(data);
}

	let defBounds;
	let defCenter;
	let defZoom = 6;
	
	let map = new L.Map('map', {
		home: true,
		zoom: defZoom,
		maxZoom: 19,
		minZoom: defZoom
	});
	
	let logo = L.control({position: 'topright'});
	logo.onAdd = function (map) {
		let div = L.DomUtil.create('div', ' logo');
		div.innerHTML = '<i><img src="/data/logo" alt="Logo jednostki" height="50" width="50"></i>';
		return div;
	};
	
	let basemaps = {

		OpenStreetMap: L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
			maxZoom: 20,
			attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, ' +
				'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
			id: 'mapbox/streets-v11',
			tileSize: 512,
			zoomOffset: -1
		}),
		
    'Ortofotomapa': new L.TileLayer.WMTS( "https://mapy.geoportal.gov.pl/wss/service/PZGIK/ORTO/WMTS/StandardResolution" ,
           {
               layer: 'ORTOFOTOMAPA',
               maxZoom: 19,
               style: "default",
               tilematrixSet: "EPSG:3857",
               format: "image/jpeg",
               attribution: 'Map data &copy; <a href="https://geoportal.gov.pl">GeoPortal.gov.pl</a> Ortofotomapa',
           }),
    'Mapa zasadnicza': L.tileLayer.wms('https://integracja.gugik.gov.pl/cgi-bin/KrajowaIntegracjaUzbrojeniaTerenu', {
          layers: 'przewod_wodociagowy,przewod_urzadzenia,przewod_niezidentyfikowany,przewod_specjalny,przewod_telekomunikacyjny,przewod_cieplowniczy,przewod_gazowy,przewod_elektroenergetyczny,przewod_kanalizacyjny',
          maxZoom: 19,
          minZoom:16,
          format: 'image/jpeg',
          transparent: false,
          version: '1.1.1',
          srs: 'EPSG:3857',
          tiled:false,
          attribution: 'Map data &copy; <a href="https://geoportal.gov.pl">GeoPortal.gov.pl</a> Krajowa Integracja GESUT',
        })

	}

	let layerControl = L.control.layers(basemaps, {}, {collapsed: true}).addTo(map);
	basemaps.OpenStreetMap.addTo(map);
	
	let borders =  L.geoJSON('', {

		onEachFeature: null,
		interactive: false,

	style: function (feature) {
			return     {
                type: "polyline",
                color: "#0000FF",
                fillColor: "#0000FF",
				fillOpacity: 0,
                dashArray: [5, 5],
                weight: 4
			}
		},
	}).addTo(map);
	
	let roads =  L.geoJSON('', {

		onEachFeature: null,
		interactive: false,

	style: function (feature) {
			return     {
                type: "polyline",
                color: "#000000",
                opacity: 0.3,
                fillColor: "#000000",
				fillOpacity: 0.5,
                weight: 4
			}
		},
	}).addTo(map);

	bordersGeoJson();
	roadsGeoJson();
	
	
	const legend = L.control.Legend({
		position: "bottomright",
		title: "Legenda",
		collapsed: true,
		symbolWidth: 16,
		opacity: 1,
		column: 1,
		legends: [{
			label: " Drogi jednostki",
			type: "polyline",
			color: "#000000",
			fillColor: "#000000",
			opacity: 0.3,
			weight: 2,
			layers: roads
		},{
			label: " Granica jednostki",
			type: "polyline",
			color: "#0000FF",
			fillColor: "#0000FF",
			dashArray: [5, 5],
			weight: 2,
			layers: borders
		}]
	})
	.addTo(map);


lc = L.control.locate({
	tap: false,
	follow: true,
	flyTo:true,
	locateOptions: {
       maxZoom: 18
	},
	strings: {
		title: "Pokaż gdzie jestem!",
		outsideMapBoundsMsg: "Jesteś poza zakresem mapy"
	}
}).addTo(map);

map.on('startfollowing', function() {
	map.on('dragstart', lc._stopFollowing, lc);
}).on('stopfollowing', function() {
	map.off('dragstart', lc._stopFollowing, lc);
});

logo.addTo(map);

L.Icon.Default.imagePath = './map/css/images/';
let marker = null;
map.on('click', function (e) {
    let latInput = document.getElementById("lat")
    let lonInput = document.getElementById("lon")
    if (marker) {
        map.removeLayer(marker);
        lonInput.value = '';
        latInput.value = '';
    }
    marker = new L.Marker(e.latlng);
    let inside = false;
    borders.eachLayer(function (layer) {
        inside = turf.booleanPointInPolygon(
            marker.toGeoJSON(),
            layer.toGeoJSON()
        );
    });
    if (inside) {
        marker.addTo(map);
        let m = marker.getLatLng();
        lonInput.value = m.lng;
        latInput.value = m.lat;
    }
});

	
/* form settings */

/* chang size by canvas*/

const MAX_WIDTH = 640;
const MAX_HEIGHT = 360;
const MIME_TYPE = "image/jpeg";
const QUALITY = 0.7;

const input = document.getElementById("imgInp");
input.onchange = function (ev) {
  const file = ev.target.files[0]; // get the file
  const blobURL = URL.createObjectURL(file);
  const img = new Image();
  img.src = blobURL;
  img.onerror = function () {
    URL.revokeObjectURL(this.src);
    console.log("Cannot load image");
  };
  img.onload = function () {
	let elem = document.getElementById("imagePreload");
	if (elem != null) elem.parentElement.removeChild(elem);
    URL.revokeObjectURL(this.src);
    const [newWidth, newHeight] = calculateSize(img, MAX_WIDTH, MAX_HEIGHT);
    const canvas = document.createElement("canvas");
	canvas.id="imagePreload";
    canvas.width = newWidth;
    canvas.height = newHeight;
    const ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0, newWidth, newHeight);
    canvas.toBlob(
      (blob) => {
        // Handle the compressed image. es. upload or save in local state
        //displayInfo('Original file', file);
        //displayInfo('Compressed file', blob);
      },
      MIME_TYPE,
      QUALITY
    );
    document.getElementById("step-3").append(canvas);
    let base64Canvas = canvas.toDataURL('image/jpeg', 1.0).replace('data:', '').replace(/^.+,/, '');                                                         //				.replace(/^.+,/, '');;
	$('#imgBase64').val(base64Canvas);
  };
};

function calculateSize(img, maxWidth, maxHeight) {
  let width = img.width;
  let height = img.height;

  // calculate the width and height, constraining the proportions
  if (width > height) {
    if (width > maxWidth) {
      height = Math.round((height * maxWidth) / width);
      width = maxWidth;
    }
  } else {
    if (height > maxHeight) {
      width = Math.round((width * maxHeight) / height);
      height = maxHeight;
    }
  }
  return [width, height];
}

function displayInfo(label, file) {
  const p = document.createElement('p');
  p.innerText = `${label} - ${readableBytes(file.size)}`;
  document.getElementById('step-3').append(p);
}

function readableBytes(bytes) {
  const i = Math.floor(Math.log(bytes) / Math.log(1024)),
    sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

  return (bytes / Math.pow(1024, i)).toFixed(2) + ' ' + sizes[i];
}
	
function submitForm(e) {
e.preventDefault();
if ( validateForm(3) ) {
    $( "#reportForm" ).submit();
}
}
	
let btnFinish = $('<button></button>').text('Wyślij')
							 .addClass('btn btn-info')
							 .attr('id', 'finish')
							 .on('click', function(event){
							    submitForm(event);
							 });

$("#smartwizard").on("leaveStep", function(e, anchorObject, stepIndex, stepDirection) {
	
	if (!validateForm(stepIndex) && stepDirection > stepIndex) {
		return false
	};
	closeAlert();
	
});

$("#smartwizard").on("stepContent", function(e, anchorObject, stepIndex, stepDirection) {
   	if (stepIndex == 3){
		$(".sw-btn-next").css("display", "none");
		$("#finish").css("display", "inline-block");
	} else {
		$(".sw-btn-next").css("display", "inline-block");
	}
});


$('#smartwizard').smartWizard({
	selected: 0,
	theme: 'arrows',
	 autoAdjustHeight: false,
	transition: {
		animation: 'slide-horizontal',
	},
	toolbarSettings: {
		toolbarPosition: 'bottom', // both bottom
		toolbarExtraButtons: [btnFinish]
	},
	  lang: { // Language variables for button
		  next: 'Dalej',
		  previous: 'Wróć'
	}
});
			
$("#finish").css("display", "none");

function openAlert(message) {
 	  let alertObj = document.getElementsByClassName("alert")[0];
 	  alertObj.style.display='block';
 	  alertObj.classList.add("showModal");
 	  alertObj.getElementsByTagName('p')[0].innerHTML = message;
}

function closeAlert(){
  let alertObj = document.getElementsByClassName("alert")[0];
  alertObj.style.display='none';
  alertObj.classList.remove("showModal");
  alertObj.getElementsByTagName('p')[0].innerHTML = "";
}

function chekBindingsErrors(){
    let alertObj = document.getElementsByClassName("alert")[0];
    let text = alertObj.getElementsByTagName('p')[0].innerHTML;
    if (text.length > 1){
    alertObj.style.display='block';
    alertObj.classList.add("showModal");
    }
}

function validateForm(stepIndex) {
  let message='';
  let x, y, i, valid = true;
  x = document.getElementsByClassName("tab-pane");
  y = x[stepIndex].getElementsByTagName("input");
  for (i = 0; i < y.length; i++) {
    if (y[i].value == "" && y[i].type != 'file' && y[i].name != 'photo' && y[i].type != 'email') {
      y[i].className += " invalid";
      valid = false;
	  message += 'Nie uzupełniono wymaganych informacji; ';
    }
	if (y[i].type == 'email') {
		let mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		if(!y[i].value.match(mailformat)){
			valid = false;
			y[i].className += " invalid";
			message += 'Niepoprawny adres email; ';
		}
	}
	if (y[i].type == 'checkbox') {
		if(!y[i].checked){
			valid = false;
			message += 'Zaznacz wyrażenie zgody; ';
		}
	}
  }
    if (stepIndex == 1 && x[1].getElementsByTagName("select")[0].value == "") {
    	  x[1].getElementsByTagName("select")[0].className += " invalid";
    	   valid = false;
    	   message += 'Wybierz typ zgłoszenia; ';
    }
   if (stepIndex == 1 && x[1].getElementsByTagName("textarea")[0].value == "") {
	  x[1].getElementsByTagName("textarea")[0].className += " invalid";
	   valid = false;
	   message += 'Uzupełnij opis zgłoszenia; ';
  }
  if (!valid) {
	   if (stepIndex != 0 ){
			openAlert(message);
	   } else {
		   openAlert('Wskaż lokalizację zgłoszenia w granicach jednostki; ');
	   }
  }

  return valid;
}

chekBindingsErrors();

const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);


if (urlParams.get('search') !== null) {
    openTab('mapTab');
}
