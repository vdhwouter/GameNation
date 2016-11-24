$(document).ready(function () {

    /* ===========================================
     show all games that user added
     =========================================== */

    axios.get("/users/5835cd4b0e402cf3a0f6a1ac/games")
        .then(function (result) {
            var data = result.data

            // create html tage with added games
            var ul = document.createElement("ul");
            ul.setAttribute("id", "listAddedGames");
            document.getElementById('games').appendChild(ul);

            for (key in data) {
                if (data.hasOwnProperty(key)) {
                    var value = data[key];

                    var li = document.createElement("li");

                    var a = document.createElement('a');
                    a.setAttribute("data-remodal-target", "EditGameModal");
                    a.setAttribute("onclick", "editGame(this)");

                    var firstP = document.createElement("p");
                    firstP.innerHTML = value['game'].id;
                    firstP.setAttribute("style", "display: none");

                    var img = document.createElement("img");
                    img.setAttribute("src", "img/games/" + value['game'].imageName);
                    img.setAttribute("alt", value['game'].id);
                    img.setAttribute("title", value['game'].name);
                    img.className += "game";

                    var secondImg = document.createElement("img");
                    secondImg.setAttribute("class", "levelGames");
                    secondImg.setAttribute("src", "img/cirkel.png");

                    var thirdP = document.createElement("p");
                    thirdP.innerHTML = value.skill_level;
                    thirdP.setAttribute("class", "levelGames");

                    var secondP = document.createElement("p");
                    secondP.innerHTML = value['game'].name;

                    a.appendChild(firstP);
                    a.appendChild(img);
                    a.appendChild(secondImg);
                    a.appendChild(thirdP);
                    a.appendChild(secondP);
                    li.appendChild(a);
                    ul.appendChild(li);
                }
            }

            // add li tag for add a new game
            var ul = document.getElementById("listAddedGames");
            var li = document.createElement("li");

            var a = document.createElement("a");
            a.setAttribute("data-remodal-target", "AddNewGameModal");

            var img = document.createElement("img");
            img.setAttribute("src", "img/addGame.png");
            img.setAttribute("alt", "addGame");
            img.setAttribute("title", "Add new Game");
            img.setAttribute("id", "addGame");
            img.className += "game";

            var p = document.createElement("p");
            p.innerHTML = "New game";

            a.appendChild(img);
            a.appendChild(p);
            li.appendChild(a);
            ul.appendChild(li);
        })


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
     show all games in remodal
     make an array from all selected games
     =========================================== */
    // array with selected games
    var addedGames = new Array();


    axios.get("/games")
        .then(function (response) {
            var data = response.data

            // create html tage with games
            var ul = document.createElement("ul");
            ul.className = "live-search-list";
            document.getElementById('addGames').appendChild(ul);

            for (key in data) {
                if (data.hasOwnProperty(key)) {
                    var value = data[key];

                    var li = document.createElement("li");
                    li.className = "file";
                    li.setAttribute("data-search-term", value.name.toLowerCase());

                    var img = document.createElement("img");
                    img.setAttribute("src", "img/games/" + value.imageName);
                    img.setAttribute("alt", value.id);
                    img.setAttribute("title", value.name);
                    img.className += "addGamesImg";

                    var p = document.createElement("p");
                    p.innerHTML = value.name;

                    li.appendChild(img);
                    li.appendChild(p);
                    ul.appendChild(li);
                }
            }

            // what if a user click on a game?
            $('.addGamesImg').click(function () {
                $(this).toggleClass('active');

                if (!eleContainsInArray(addedGames, $(this).attr("alt"))) {
                    addedGames.push($(this).attr("alt"));
                }
                else {
                    var index = addedGames.indexOf($(this).attr("alt"));
                    addedGames.splice(index, 1);
                }

            });
        });

    // check if game is already in array
    function eleContainsInArray(arr, element) {
        if (arr != null && arr.length > 0) {
            for (var i = 0; i < arr.length; i++) {
                if (arr[i] == element)
                    return true;
            }
        }
        return false;
    }


    // by click on the add button
    $('#addConfirm').click(function () {
        if (addedGames.length == 0) {
            console.log("no items selected");
        }
        else {
            console.log("addedgames" + addedGames);

            //toevoegen game aan user
            axios.post("/users/5835cd4b0e402cf3a0f6a1ac/games", addedGames)
                .then(function (data) {
                    console.log(data)
                    location.reload();
                })
        }
    });
});


/* ===========================================
 edit skillset / delete game van user game modal
 =========================================== */
var td_list = [];
var editGame = function (e) {

    $(e).children().each(function (i, v) {
        if (i >= 0 && i < 5) {
            td_list[i] = $(this)[0].innerHTML;
        }
    });
    console.log(td_list);
    $('#editGameTitle').text("Edit " + td_list[4]);
    $('#editGame input[type="number"]').val(td_list[3].replace(/[^0-9]/g, ''));
}

// by click on the add button
$('#modelSkilEditEdit').click(function () {
    //toevoegen game aan user
    axios.post('/users/5835cd4b0e402cf3a0f6a1ac/games/' + td_list[0], {
        level: document.getElementById("skillset").value
    }).then(function (result) {
        console.log("toevoegen game aan user", result)
        location.reload()
    })
});

// by click on the delete button
$('#modelSkilEditDelete').click(function () {
    console.log("id " + td_list[0]);
    //delete game van user
    axios.delete('/users/5835cd4b0e402cf3a0f6a1ac/games/' + td_list[0])
        .then(function (result) {
            location.reload()
        })
});