/**
 * Created by tijs on 24/11/2016.
 */
$(document).ready(function () {

    // show the add button if user is logged in
    if (session.authenticated) {
        $("#addConfirm").css('display',null);
    }
    else {
        $("#addConfirm").css('display',"none");
    }

    /* ===========================================
     show all games
     =========================================== */
    axios.get("/games").then(function (response) {
        var data = response.data

        // create html tage with games
        var ul = document.createElement("ul");
        ul.className = "live-search-list";
        document.getElementById('games').appendChild(ul);

        for (key in data) {
            if (data.hasOwnProperty(key)) {
                var value = data[key];

                var li = document.createElement("li");
                li.className = "file";
                li.setAttribute("data-search-term", value.name.toLowerCase());
                li.setAttribute("data-remodal-target", "ShowGameDetailsModal");
                li.setAttribute("onclick", "infoGame(this)");

                var img = document.createElement("img");
                img.setAttribute("src", "img/games/" + value.imageName);
                img.setAttribute("alt", value.id);
                img.setAttribute("title", value.name);
                img.className += "addGamesImg";

                var p = document.createElement("p");
                p.innerHTML = value.name;

                var secondP = document.createElement("p");
                secondP.setAttribute("style", "display: none");
                secondP.innerHTML = value.description;

                li.appendChild(img);
                li.appendChild(p);
                li.appendChild(secondP);
                ul.appendChild(li);
            }
        }
    });

    /* ===========================================
     live search filter games
     =========================================== */
    $('.live-search-box').on('keyup', function () {
        var searchTerm = $(this).val().toLowerCase();

        $('.live-search-list li').each(function () {
            if ($(this).filter('[data-search-term *= ' + searchTerm + ']').length > 0 || searchTerm.length < 1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });


});

/* ===========================================
 create info in game modal
 =========================================== */
// array with selected game
var addedGame = new Array();

// give info about the game in the modal
var infoGame = function (e) {
    var allGames = [];
    $("#detailsGames").html("");

    $(e).children().each(function (i, v) {
        allGames[i] = $(this)[0].innerHTML;
    });

    // get id from img alt attribute
    var str = $(e)[0].innerHTML;
    var tmp = document.createElement('div');
    tmp.innerHTML = str;
    var alt = tmp.getElementsByTagName('img')[0].alt;
    addedGame[0]= alt;


    // create html tags with info about the game
    var h4 = document.createElement("h4");
    h4.innerHTML = "Over dit spel";

    var firstP = document.createElement("p");
    firstP.innerHTML = addedGame[0];
    firstP.setAttribute('style', 'display: none');

    var secondP = document.createElement("p");
    secondP.innerHTML = allGames[2];

    var element = document.getElementById('detailsGames');
    element.appendChild(h4);
    element.appendChild(firstP);
    element.appendChild(secondP);

    console.log(addedGame);
    console.log(allGames);
}


/* ===========================================
 add a game to user profile
 =========================================== */
// by click on the add button
$('#addConfirm').click(function () {
    if (addedGame.length == 0) {
        console.log("no items selected");
    }
    else {
        if (session.authenticated) {
            //toevoegen game aan user
            axios.post("/users/" + session.id + "/games", addedGame).then(function (data) {
                location.reload();
            });
        }
        else {
            console.log("user moet inloggen om game te kunnen toevoegen");
        }
    }
});
