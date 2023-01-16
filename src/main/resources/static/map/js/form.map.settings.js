
	async function bordersGeoJson() {
		const response = await fetch("/data/border");
		const data = await response.json();
			borders.addData(data);
			defBounds = borders.getBounds();
			defCenter = borders.getBounds().getCenter();
			map.setView(defCenter,defZoom,{animation: true});
			//map.fitBounds(borders.getBounds());
	}
	
	async function roadsGeoJson() {
		const response = await fetch("/data/roads");
		const data = await response.json();
			roads.addData(data);
	}

	var defBounds;
	var defCenter;
	var defZoom = 10;
	
		proj4.defs('EPSG:2180', '+proj=tmerc +lat_0=0 +lon_0=19 +k=0.9993 +x_0=500000 +y_0=-5300000 +ellps=GRS80 +units=m +no_defs');
	var crs = new L.Proj.CRS(
		"EPSG:2180",
		"+proj=tmerc +lat_0=0 +lon_0=19 +k=0.9993 +x_0=500000 +y_0=-5300000 +ellps=GRS80 +units=m +no_defs", {});
	
	var map = new L.Map('map', {
		home: true,
		zoom: defZoom,
		maxZoom: 19,
		minZoom: 10
	});
	
	var basemaps = {

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
          format: 'image/jpeg',
          transparent: false,
          version: '1.1.1',
          srs: 'EPSG:3857',
          tiled:false,
          attribution: 'Map data &copy; <a href="https://geoportal.gov.pl">GeoPortal.gov.pl</a> Krajowa Integracja GESUT',
        })
	}
	
	var layerControl = L.control.layers(basemaps, {}, {collapsed: false}).addTo(map);
	basemaps.OpenStreetMap.addTo(map);
	
	var borders =  L.geoJSON('', {

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
	
	var roads =  L.geoJSON('', {

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

	//var layerControl = L.control.layers(baseLayers, overlays).addTo(map);
	
	bordersGeoJson();
	roadsGeoJson();
	
	
	const legend = L.control.Legend({
		position: "bottomright",
		title: "Legenda",
		collapsed: false,
		symbolWidth: 16,
		opacity: 1,
		column: 1,
		legends: [{
			label: "Drogi jednostki",
			type: "polyline",
			color: "#000000",
			fillColor: "#000000",
			opacity: 0.3,
			weight: 2,
			layers: roads
		},{
			label: "Granica jednostki",
			type: "polyline",
			color: "#0000FF",
			fillColor: "#0000FF",
			dashArray: [5, 5],
			weight: 2,
			layers: borders
		}]
	})
	.addTo(map);
	
		var logo = L.control({position: 'bottomright'});

		logo.onAdd = function (map) {

			var div = L.DomUtil.create('div', 'info logo'),
				labels = [
				'<i><img src="/data/logo" alt="Powiat Piaseczyński" height="75" width="75"></i>',

				],
				from, to;
			div.innerHTML = labels.join('<br>');
			return div;
		};

		logo.addTo(map);


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

	L.Icon.Default.imagePath = 'css/images';
	var marker;
	map.on('click', function (e) {
		if (marker) {
		map.removeLayer(marker);
		}
		marker = new L.Marker(e.latlng).addTo(map);
		var m = marker.getLatLng();
		var latInput = document.getElementById("lat")
		var lonInput = document.getElementById("lon")
		lonInput.value = m.lng;
		latInput.value = m.lat;
	});

