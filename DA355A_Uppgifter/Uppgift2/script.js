var request = new XMLHttpRequest();
var WEATHER_API_KEY = "7f7205229672eebf4f57146158c38bbc";
var MOVIE_API_KEY = "43057dc7";

class Weather {
    loadWeather() {
        var temp = document.getElementById("temp");
        var place = document.getElementById("place");
        var image = document.getElementById("image");
        var desc = document.getElementById("desc");
        if (navigator.geolocation)navigator.geolocation.getCurrentPosition(this.getWeather);
        else alert("Geolocation is not supported by this browser.");
    }
    getWeather(position) {
        var url = 'https://api.openweathermap.org/data/2.5/weather?lat='+position.coords.latitude+'&lon='+position.coords.longitude+'&appid='+WEATHER_API_KEY;
        request.open('GET', url , true);
        request.onload = function() {
            var data = JSON.parse(this.response);
            if (request.status >= 200 && request.status < 400) {
                temp.innerHTML = Math.round(data.main.temp-272.15) + " &#176;C";
                place.innerHTML = data.name;
                image.src = "http://openweathermap.org/img/wn/"+data.weather[0].icon+"@2x.png";
                desc.innerHTML = data.weather[0].description;
             }
        }
        request.send();
    }
}

class Movie {
    searchMovie() {
        var movieTitle = document.getElementById("movieTitle").value;
        var movieYear = document.getElementById("movieYear").value;
        var title = document.getElementById("title");
        var year = document.getElementById("year");
        var url = "";
        if(movieYear.length>0)
            url = 'https://www.omdbapi.com/?t='+movieTitle+'&y='+movieYear+'apikey='+MOVIE_API_KEY+'&plot=full';
        else
            url = 'https://www.omdbapi.com/?t='+movieTitle+'&apikey='+MOVIE_API_KEY+'&plot=full';

        request.open('GET', url , true);
        request.onload = function() {
            var data = JSON.parse(this.response);
            if (request.status >= 200 && request.status < 400) {
                title.innerHTML = data.Title;
                year.innerHTML = data.Year;
            }
        }
        request.send();
    }

    saveMovie() {
        var movie = document.getElementById("title").textContent;
        alert(movie);
    }

    showMovies(){
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var splitted = this.responseText.split("\n");
                var list = document.getElementsByClassName("list-library")[0];
                for (var i = 0; i < splitted.length; i++){
                    var listItem = document.createElement("LI");
                    var span = document.createElement("SPAN");
                    listItem.innerHTML = splitted[i];
                    listItem.setAttribute("id", "listItem"+(i+1));
                    listItem.setAttribute("class", "list-group-item d-flex justify-content-between align-items-center bg-dark text-white");
                    listItem.setAttribute("onclick", "movie.showMovieInfo('listItem"+(i+1)+"')");
                    span.innerHTML = "X";
                    span.setAttribute("class", "badge badge-primary badge-pill");
                    span.setAttribute("onclick", "movie.deleteMovie(" + (i+1) + ")");
                    listItem.appendChild(span);
                    list.appendChild(listItem);
                }
            }
        };
        xhttp.open("GET", "movies.txt", true);
        xhttp.send();
    }

    deleteMovie(index){
        var table = document.getElementsByClassName("table-library")[0];
        table.deleteRow(index);
    }

    showMovieInfo(id) {
        $(document).ready(function () {
            $("#"+id).click(function () {
                alert(id);
            });
        });
    }
}

var movie = new Movie();
var weather = new Weather();
