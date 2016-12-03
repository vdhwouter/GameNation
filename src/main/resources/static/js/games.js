/**
 * Created by tijs on 24/11/2016.
 */

var alreadyAddedGames = new Array();

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


    /* ===========================================
     get id's that user already added
     =========================================== */
     axios.get("/users/" + session.id + "/games").then(function (result) {
        var data = result.data

        for (key in data) {
            if (data.hasOwnProperty(key)) {
                var value = data[key]
                alreadyAddedGames.push(value['game'].id);
            }
        }
    })

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
    var firstH4 = document.createElement("h4");
    firstH4.innerHTML = "Over dit spel";

    var firstP = document.createElement("p");
    firstP.innerHTML = addedGame[0];
    firstP.setAttribute('style', 'display: none');

    var secondP = document.createElement("p");
    secondP.innerHTML = allGames[2];

    var secondH4 = document.createElement("h4");
    secondH4.innerHTML = "Vrienden";

    var thirdP = document.createElement("p");
    thirdP.innerHTML = "Deze vrienden spelen dit spel ook:";


    var element = document.getElementById('detailsGames');
    element.appendChild(firstH4);
    element.appendChild(firstP);
    element.appendChild(secondP);
    element.appendChild(secondH4);
    element.appendChild(thirdP);

    for (i = 0; i < 20; i++) {
        var firstImg = document.createElement("img");
        firstImg.setAttribute("src", "img/avatar-member.jpg");
        firstImg.setAttribute("alt", "avatar-member" + i);
        firstImg.setAttribute("title", 'avatar-member\nlevel 5' + i);
        firstImg.className += "friendImg";

        element.appendChild(firstImg);
    }
}


/* ===========================================
 add a game to user profile
 =========================================== */
 var errorArray = new Array();

// by click on the add button
$('#addConfirm').click(function () {
    if (addedGame.length == 0) {
        errorArray.push("There are no games selected, try again")
    }
    else {
        // check if user is logged in
        if (session.authenticated) {
            // check if user already have the game
            if (!eleContainsInArray(alreadyAddedGames, addedGame[0])) {
                //add game to user
                axios.post("/users/" + session.id + "/games", addedGame).then(function (data) {
                    location.reload();
                });
            }
            else {
                errorArray.push("Game is already added to your profile")
            }
        }
        else {
            errorArray.push("User must login before adding games");
        }
        // show errors on top page
        $('#register-errors').empty();
        $(errorArray).each(function(index, value){ $('#register-errors').append('<li><img src="img/error.png" /><p>' + value + '</p></li>') });
        $('#register-errors').slideDown();

        errorArray = [];
    }
});


// check if game is already in array
function eleContainsInArray(arr, element) {
    if (arr != null && arr.length > 0) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] == element) {
                return true;
            }
        }
    }
    return false;
}
