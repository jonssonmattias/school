/*--------UPPGIFT 1----------------*/

let buttonSuccess = document.getElementById("success");
let buttonInfo = document.getElementById("info");
let buttonError = document.getElementById("error");
let box = document.getElementById("message-box");

function onButtonSuccessClick(event) {
    box.classList.remove("info");
    box.classList.remove("error");
    box.classList.add("success");
}

function onButtonInfoClick(event) {
    box.classList.remove("success");
    box.classList.remove("error");
    box.classList.add("info");
}

function onButtonErrorClick(event) {
    box.classList.remove("success");
    box.classList.remove("info");
    box.classList.add("error");
}

buttonSuccess.addEventListener("click", onButtonSuccessClick);
buttonInfo.addEventListener("click", onButtonInfoClick);
buttonError.addEventListener("click", onButtonErrorClick);

/*--------UPPGIFT 2----------------*/

let addItemButton = document.getElementById("add-item");
let removeItemButton = document.getElementById("remove-item");
let list = document.getElementById("items");

function addItem(){
    let item = window.prompt("Add item");
    if(item != null && item.length > 0){
        var li = document.createElement("li");
        li.appendChild(document.createTextNode(item));
        list.appendChild(li);
    }
}

function removeItem() {
    var item = list.lastElementChild;
    list.removeChild(item);
}

addItemButton.addEventListener("click", addItem);
removeItemButton.addEventListener("click", removeItem);

/*--------UPPGIFT 3----------------*/

let buttons = document.getElementsByClassName("remove-list-item");

function removeListItem() {
    if(window.confirm())this.parentElement.remove();
}

for (var i=0; i < buttons.length; i++) {
    buttons[i].addEventListener("click", removeListItem);
};

/*--------UPPGIFT 4----------------*/

var header = document.getElementsByTagName("h2");
var section = document.getElementsByTagName("section");

function hide() {
    for(var i=0;i<section.length;i++)
        if(section[i].style.display = "block")
            section[i].style.display = "none";
}

function show() {
    hide();
    var sec = this.parentElement.lastElementChild;
    if(sec.style.display == "none") sec.style.display = "block";
    else sec.style.display = "none";
}

hide();
for(var i=0;i<header.length;i++){
    header[i].addEventListener("click", show);
}

/*--------UPPGIFT 5----------------*/

var sec = document.getElementById("seconds");
var startButton = document.getElementById("start-timer");
var stopButton = document.getElementById("stop-timer");
var resetButton = document.getElementById("reset-timer");
var intervalID;
var seconds = 0;

function start() {
    function print() {
        sec.innerHTML = seconds++ + "s";
    }
     intervalID = setInterval(print, 1000);
}

function stop(){
    clearInterval(intervalID);
}

function reset() {
    clearInterval(intervalID);
    seconds = 0;
    sec.innerHTML = 0 + "s";
}

startButton.addEventListener("click", start);
stopButton.addEventListener("click", stop);
resetButton.addEventListener("click", reset);
