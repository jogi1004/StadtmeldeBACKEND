var map = L.map('map', {
    maxBounds: [
        [90, -180], // Obere linke Ecke (90°N, 180°W)
        [-90, 180]  // Untere rechte Ecke (90°S, 180°E)
    ]
}).setView([49.245408, 7.364850], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    minZoom: 2,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);




// Die URL zur API
var hostname = window.location.origin;
const url =  hostname + '/location/id/1';

// Die Konfiguration für den Fetch Request
const requestOptions = {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
    },
};

// Fetch Request, um die Daten von der API abzurufen
fetch(url, requestOptions)
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Daten erfolgreich abgerufen
        loadDataIntoMap(data);
    })
    .catch(error => {
        // Fehler behandeln
        console.error('Fetch Error:', error);
    });

// Die Funktion, um die Daten in die Karte zu laden
function loadDataIntoMap(data) {

    // Iteration über jedes Objekt und Hinzufügen von Markern in Leaflet
    data.forEach(obj => {
        const latitude = obj.latitude;
        const longitude = obj.longitude;

        // Hinzufügen eines Markers für jede Latitude und Longitude in Leaflet
        L.marker([latitude, longitude]).addTo(map)
            .bindPopup(obj.titleOrsubcategoryName); // Optional: Popup mit dem Titel/Subkategorie-Namen hinzufügen
    });
}