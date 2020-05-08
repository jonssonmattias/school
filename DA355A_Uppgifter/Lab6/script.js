$("#get-joke").on("click", function() {
    $.ajax({
        url: getURL(),
        dataType: "JSON"
    }).done(function(data) {
        $(".joke").remove();
        for(var i = 0; i < data.value.length; i++){
            var joke = $("<div class='joke'></div>").text(data.value[i].joke);
            $(".xs-col-12").append(joke);
            joke.fadeIn();
        }
    }).fail(function(data) {
        console.log(data);
    });
});

function getURL() {
    var cat = $(".cat").val();
    var firstname = $("#firstname").val();
    var lastname = $("#lastname").val();
    var numOfJokes = $("#numOfJokes").val();
    if(numOfJokes=="") numOfJokes=1;
    if(numOfJokes>10) alert("Max 10 jokes");

    if(cat == "All categories")
        return "http://api.icndb.com/jokes/random/"+numOfJokes+"?escape=javascript&firstName="+firstname+"&lastName="+lastname;
    else
        return "http://api.icndb.com/jokes/random/"+numOfJokes+"?imitTo=["+cat+"]&escape=javascript&firstName="+firstname+"&lastName="+lastname;
}
