var slideIndex = 1;
var images = document.getElementsByClassName("slideshowImg");
var storage = window.localStorage;
var mobileMenu = false;
var stylesheets = {};
stylesheets["TouchOfGold"] = "Touch of Gold";
stylesheets["DiBlae"] = "Di Blåe";
stylesheets["Darkweb"] = "Darkweb";

function changeSlides() {
  showSlides(slideIndex += 1);
}

function showSlides(n) {
    var slides = document.getElementsByClassName("slideshowImg");
    if (n > slides.length) slideIndex = 1;
    if (n < 1) slideIndex = slides.length;
    for (var i = 0; i < slides.length; i++) slides[i].style.display = "none";
    slides[slideIndex-1].style.display = "block";
    console.log(window.innerWidth);
    if(window.innerWidth > 900){
        document.getElementsByTagName("article")[0].style.height = (slides[slideIndex-1].offsetHeight+150)+"px";
    }
}

function start() {
   setInterval(changeSlides, 3000);
}

function loadStyleSheet() {
    var styleSheet = storage.getItem("stylesheet");
    document.getElementById("stylesheet").setAttribute("href", "stylesheet//"+styleSheet+".css");
    document.getElementById("styleSelect").value  = styleSheet;
}

function changeStyle() {
    var styleSheet = document.getElementById("styleSelect").value;
    storage.setItem("stylesheet", styleSheet);
    document.getElementById("stylesheet").setAttribute("href", "stylesheet//"+styleSheet+".css");
}

function toggleMenu() {
    if(window.innerWidth<=600){
        var menu = document.getElementsByTagName("nav")[0];
        var body = document.getElementsByTagName("body")[0];
        if(!mobileMenu){
            menu.style.display = "block";
            body.style.overflow = "hidden";
        }
        else {
            menu.style.display = "none";
            body.style.overflow = "scroll";
        }
        mobileMenu = !mobileMenu;
    }
}
