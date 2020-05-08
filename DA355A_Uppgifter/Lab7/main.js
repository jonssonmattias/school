// Vår lista över filmer
let movies = [
    { title: "Star Wars", grade: 5 },
    { title: "Titanic", grade: 4 },
    { title: "Drive", grade: 1 }
];

sessionStorage.setItem('orderBy', 'none');

function printMovies(movies) {
    var movieList = document.getElementById("movie-list");
    while (movieList.firstChild) movieList.removeChild(movieList.firstChild)

    for(var i = 0; i < movies.length; i++){
        var movie = $("<li></li>").text(movies[i].title + " - " + movies[i].grade + "/5").css({"height": "25px", "width": "30%"});
        var button = $("<button></button>").text("Ta bort").addClass("delete-movie").css("float", "right");
        button.val(movies[i].title);
        movie.append(button);
        $("#movie-list").append(movie);
    }
    $(".delete-movie").on("click", function() {
        localStorage.removeItem(movies);
        movies.splice(getMovie($(this).val()), 1);
        localStorage.setItem("movies", JSON.stringify(movies));
        loadMovies();
    });
}

function getMovie(title){
    for(var i = 0; i < movies.length; i++) if(movies[i].title == title) return movies[i];
}

function getStars(grade) {
    // TODO:
    // baserat på `grade` så returnerar denna funktionen HTML till
    // när vi ska visa filmen
}

function loadMovies() {
    let jsonMovies = localStorage.getItem("movies");
    movies = JSON.parse(jsonMovies);
    console.log("Loaded movies: ", movies);
    printMovies(movies);
}

function compare(a, b) {
    if (a > b) return 1;
    else if (a < b) return -1;
    return 0;
}

function orderByTitle(){
    function compareTitle(a, b) {
      return compare(a.title.toUpperCase(), b.title.toUpperCase());
    }
    movies.sort(compareTitle);
}

function orderByGrade(){
    function compareGrade(a, b) {
      return compare(a.grade, b.grade);
    }
    movies.sort(compareGrade);
}

$("#save-movie").on("click", function() {
    var title = document.getElementById("title").value;
    var grade = document.getElementById("grade").value;
    if(getMovie(title) == null){
        var movie = { title: title , grade: grade };
        movies.push(movie);
        var orderBy = sessionStorage.getItem("orderBy");
        if(orderBy == "title") orderByTitle();
        else if (orderBy == "grade") orderByGrade();
        printMovies(movies);
    }
    else{
        alert("Filmen finns redan tillagd");
    }
    return false;
});

$("#save-movies").on("click", function() {
    let jsonMovies = JSON.stringify(movies);
    localStorage.setItem("movies", jsonMovies);
});

$("#order-alphabetic").on("click", function() {
    orderByTitle();
    printMovies(movies);
    sessionStorage.setItem('orderBy', 'title');
});

$("#order-grade").on("click", function() {
    orderByGrade();
    printMovies(movies);
    sessionStorage.setItem('orderBy', 'grade');
});

// Skriver ut filmerna i vår lista när sidan laddats klart
$(document).ready(function() {
    loadMovies();
});
