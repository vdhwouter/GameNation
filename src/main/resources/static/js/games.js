/* ================================================================================================================
 Start loading added games on page
 =============================================================================================================== */
$(document).ready(function () {

// show the add button if user is logged in
    if (session.authenticated) {
        $("#addConfirm").css('display',null);
    }
    else {
        $("#addConfirm").css('display',"none");
    }

    loadGames();
});



/* ================================================================================================================
 Local variables
 =============================================================================================================== */
// all the games that user already have (array with game id's)
var alreadyAddedGames = new Array();

// get the id from 1 selected game
var gameId = new Array();

// all the properties from 1 selected game (name, description)
var gameProperties = new Array();

// all errors during add game
var errorArray = new Array();



/* ================================================================================================================
 load all games
 =============================================================================================================== */
var loadGames = function () {
    axios.get("/games").then(function (response) {
        var data = response.data;

        // create html tage with games
        var ul = document.getElementsByClassName('games-list')[0];
        $(ul).empty();

        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                var value = data[key];

                var li = document.createElement("li");
                li.className = "games-list__item";
                li.setAttribute("data-search-term", value.name.toLowerCase());
                li.setAttribute("data-remodal-target", "ShowGameDetailsModal");
                li.setAttribute("onclick", "infoGame(this)");

                var img = document.createElement("img");
                img.className = 'games-list__item--image-small';
                img.setAttribute("src", "img/games/" + value.imageName);
                img.setAttribute("alt", value.id);
                img.setAttribute("title", value.name);

                var p = document.createElement("p");
                p.className = 'games-list__item--text';
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
}


/******************************************** live search filter *************************************************/
$('#search_box').on('keyup', function () {
    var searchTerm = $(this).val().toLowerCase();

    $('.games-list li').each(function () {
        if ($(this).filter('[data-search-term *= ' + searchTerm + ']').length > 0 || searchTerm.length < 1) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
});



/* ================================================================================================================
 get already added games from user
 =============================================================================================================== */
axios.get("/users/" + session.id + "/games").then(function (result) {
    var data = result.data;

    for (key in data) {
        if (data.hasOwnProperty(key)) {
            var value = data[key];
            alreadyAddedGames.push(value['game'].id);
        }
    }
})



/* ================================================================================================================
 show info in modal
 =============================================================================================================== */
// give info about the game in the modal
var infoGame = function (e) {
    gameProperties = [];
    gameId = [];
    $("#detailsGames").html("");

    // take game properties out of html
    $(e).children().each(function (i, v) {
        gameProperties[i] = $(this)[0].innerHTML;
    });

    // get id from img alt attribute
    var str = $(e)[0].innerHTML;
    var tmp = document.createElement('div');
    tmp.innerHTML = str;
    gameId[0] = tmp.getElementsByTagName('img')[0].alt;


    // create html tags with info about the game
    var firstH4 = document.createElement("h4");
    firstH4.innerHTML = "Over dit spel";

    var firstP = document.createElement("p");
    firstP.innerHTML = gameId[0];
    firstP.setAttribute('style', 'display: none');

    var secondP = document.createElement("p");
    secondP.innerHTML = gameProperties[2];

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

    //get friends who are playing the game
    axios.get("/games/" + gameId[0] + "/users").then(function (response) {
        var data = response.data;

        for (key in data) {
            if (data.hasOwnProperty(key)) {
                var value = data[key];
                //console.log(value);

                var firstImg = document.createElement("img");
                firstImg.setAttribute("src", value["user"].avatar);
                firstImg.setAttribute("alt", "avatar-member");
                firstImg.setAttribute("title", value["user"].username + '\nLevel: ' + value["user"].level);
                firstImg.className += "friendImg";

                element.appendChild(firstImg);
            }
        }
    });
}


/************************************************ add button ****************************************************/
// by click on the add button
$('#addConfirm').click(function () {
    if (gameId.length == 0) {
        errorArray.push("There are no games selected, try again");
    }
    else {
        // check if user is logged in
        if (session.authenticated) {
            // check if user already have the game
            if (!eleContainsInArray(alreadyAddedGames, gameId[0])) {
                //add game to user
                axios.post("/users/" + session.id + "/games", gameId, {
                }).then(function (data) {
                    loadGames();
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
        $('.error-list').empty();
        $(errorArray).each(function(index, value){ $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle" aria-hidden="true"></i><p class="error-list__item__text">' + value + '</p></li>') });
        $('.error-list').slideDown();

        errorArray = [];
    }
});




/* ================================================================================================================
 Help classes
 =============================================================================================================== */
// check if a element already exists in array
var eleContainsInArray = function(array, element) {
    if (array != null && array.length > 0) {
        for (var i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return true;
            }
        }
    }
    return false;
}
