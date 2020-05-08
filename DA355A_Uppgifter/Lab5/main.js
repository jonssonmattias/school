$(document).ready(function(){
    $('#tal1').keyup(calculate);
    $('#tal2').keyup(calculate);
    $('#age').keyup(checkNumeric);
    $("#newsletter").on("submit", function(event) {
        event.preventDefault();
        let name = $("#name");
        let age = $("#age");
        let email = $("#email");
        $("#newsletter")[0].submit();
    });
    $("#movies").on("submit", function(event) {
        event.preventDefault();
        let title = $("#title").val();
        let grade = $("#grade").val();
        let movieList = $("#movie-list");
        var movie = $("<li></li>").addClass("list-group-item").text(title+" - "+grade+"/5").attr("data-grade", grade).attr("data-title", title);
        movie.append($("<button></button>").text("Ta bort").addClass("delete-movie").css("float", "right").val(title));
        movieList.append(movie);

        $(".delete-movie").on("click", function() {
            $("#movie-list").find($(this).parent()).remove();
        });
    });
    $("#order-alphabetic").on("click", function(event) {
        $("#movie-list").find("li").sort(sortByTitle).appendTo($("#movie-list"));
    });
    $("#order-grade").on("click", function(event) {
        $("#movie-list").find("li").sort(sortByGrade).appendTo($("#movie-list"));
    });

});
function calculate(e){
    $('#sum').val($('#tal1').val() * $('#tal2').val());
}
function checkNumeric() {
    if($.isNumeric($(this).val()) || $(this).val()=="")
        $(this).css("background-color", "white");
    else
        $(this).css("background-color", "red");
}

function sortByGrade(movieA, movieB) {
    let gradeA = $(movieA).attr("data-grade");
    let gradeB = $(movieB).attr("data-grade");

    return gradeB - gradeA;
}

function sortByTitle(movieA, movieB) {
    let titleA = $(movieA).attr("data-title");
    let titleB = $(movieB).attr("data-title");

    if (titleA < titleB) return -1;
    else if (titleA > titleB)return 1;
    else return 0;
}
