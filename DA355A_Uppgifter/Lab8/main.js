let options = {
    // Försök tvinga enheten till en så precis position som möjligt
    enableHighAccuracy: true,
    // Maximal tid i millisekunder som enheten har på sig att ge oss en position
    timeout: 5000,
    // Hur länge vår position får tillfälligt lagras (millisekunder)
    maximumAge: 0
};
let lat = document.getElementById("lat");
let lon = document.getElementById("lon");
let prec = document.getElementById("prec");
let alt = document.getElementById("alt");
let prec_alt = document.getElementById("prec_alt");
let speed =  document.getElementById("speed");
let speedNotice = document.getElementById("speedNotice")

function success(position) {
    // Ta en titt i er webbkonsol och se vad den innehåller.
    console.log("This is our position: ", position);
    lat.innerHTML = "Longitud: " + position.coords.longitude;
    lon.innerHTML = "Latitud: " + position.coords.latitude;
    prec.innerHTML = "Precision: " + position.coords.accuracy;
    alt.innerHTML = "Altitud: " + position.coords.altitude;
    prec_alt.innerHTML = "Precision, altitud: " + position.coords.altitudeAccuracy;
    speed.innerHTML = "Hastighet: " + position.coords.speed;
    speedNotice.innerHTML = "Du rör dig i " + Math.round(position.coords.speed*3.6) + " km/h";

    if(Math.round(position.coords.speed) < 5) speedNotice.style.color = "red";
    else if (Math.round(position.coords.speed) < 10) speedNotice.style.color = "yellow";
    else speedNotice.style.color = "green";
}

function error(err) {
    console.warn("Something went wrong: ", err.message);
}

function round_to_precision(x, precision) {
    var y = +x + (precision === undefined ? 0.5 : precision/2);
    return y - (y % (precision === undefined ? 1 : +precision));
}

// Skicka med våra funktioner och inställningar,
// dessa kommer sedan anropas kontinuerligt medan vi rör på oss.
let watchID = navigator.geolocation.watchPosition(success, error, options);
