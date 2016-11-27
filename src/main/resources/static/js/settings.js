$(document).ready(function () {

    /* ===========================================
     show all games that user added
     =========================================== */
    axios.get("/users/" + session.id + "/games").then(function (result) {
        var data = result.data

        // create html tage with added games
        var ul = document.createElement("ul");
        ul.setAttribute("id", "listAddedGames");
        document.getElementById('games').appendChild(ul);

        for (key in data) {
            if (data.hasOwnProperty(key)) {
                var value = data[key];

                var li = document.createElement("li");
                li.setAttribute("data-remodal-target", "EditGameModal");
                li.setAttribute("onclick", "editGame(this)");

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

                li.appendChild(firstP);
                li.appendChild(img);
                li.appendChild(secondImg);
                li.appendChild(thirdP);
                li.appendChild(secondP);
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

        getIdAddedUserGames();
        showAllGamesModal();
    })

    // get id's from all games that user added
    var getIdAddedUserGames = function() {
        var td_list = [];
        var ul = document.getElementById("listAddedGames");

        $(ul).children().each(function (i, v) {
            var str = $(this)[0].innerHTML;
            var tmp = document.createElement('div');
            tmp.innerHTML = str;
            var alt = tmp.getElementsByTagName('img')[0].alt;
            td_list[i] = alt;
        });
        // remove last item in array
        td_list.splice(-1,1)

        return td_list;
    }


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

    var showAllGamesModal = function() {

        var gamesAlreadyExists = getIdAddedUserGames();

        axios.get("/games").then(function (response) {
            var data = response.data

            // create html tage with games
            var ul = document.createElement("ul");
            ul.className = "live-search-list";
            document.getElementById('addGames').appendChild(ul);

            for (key in data) {
                if (data.hasOwnProperty(key)) {
                    var value = data[key];

                    if (!eleContainsInArray(gamesAlreadyExists, value.id)) {
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
    }


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


    // by click on the add button
    $('#addConfirm').click(function () {
        if (addedGames.length == 0) {
            console.log("no items selected");
        }
        else {
            //toevoegen game aan user
            axios.post("/users/" + session.id + "/games", addedGames).then(function (data) {
                location.reload();
            });
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

    $('#editGameTitle').text("Edit " + td_list[4]);
    $('#editGame input[type="number"]').val(td_list[3].replace(/[^0-9]/g, ''));
}

// by click on the add button
$('#modelSkilEditEdit').click(function () {
    //toevoegen game aan user
    axios.post('/users/' + session.id + '/games/' + td_list[0], {
        level: document.getElementById("skillset").value
    }).then(function (result) {
        location.reload()
    });
});

// by click on the delete button
$('#modelSkilEditDelete').click(function () {
    //delete game van user
    axios.delete('/users/' + session.id + '/games/' + td_list[0]).then(function (result) {
        location.reload()
    });
});




/* ===========================================
 Update user info via axios post
 =========================================== */


$('#settings-form').on('submit', function (e) {
    e.preventDefault();

    // Getting all vars from profile editing form
    var firstname= $('#settings-form input[name=firstname]').val();
    var lastname= $('#settings-form input[name=lastname]').val();
    var username= $('#settings-form input[name=username]').val();
    var email= $('#settings-form input[name=email]').val();
    var password= $('#settings-form input[name=password]').val();
    var confirmation= $('#settings-form input[name=confirmpass]').val();
    var teamspeak= $('#settings-form input[name=teamspeak]').val();
    var discord= $('#settings-form input[name=discord]').val();
    var description= $('#settings-form input[name=description]').val();
    var level= $('#settings-form input[name=level]').val();
    var userID= $('#settings-form input[name=userID]').val();

    //
    $('#register-errors')[0].innerHTML = CheckFormInput(email, password, confirmation, username);

    if (!$('#register-errors')[0].innerHTML) {
        axios.post('/users/' + userID, { firstname: firstname, lastname: lastname, username: username, email: email, password: password, teamspeak: teamspeak, discord: discord, description: description, level: level })
            .then((res) => {
                console.log(res);
                navigateTo(username, username);
            }).catch((err) => {
                console.log(err)
            });
    }
});


var CheckFormInput = function(email, password, confirmation, username){
    var securePassword = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,99}$/;
    var validEmail = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/
    var errors = []

    if (!email.match(validEmail)) errors.push('Email should be a valid email')
    if (!password.match(securePassword)) errors.push('Password should have a minimum length of 6 and should contain atleast 1 lowercase, 1 uppercase and 1 digit')
    if (password !== confirmation) errors.push('Password and confirmation should match')
    axios.get('/users?username=' + username).then(res => { if (res.data.length > 0) $('#register-errors')[0].innerHTML += '</br> a user with this username already exists'})

    var parsedErrors = errors.reduce(function (prev, current) {
        if (prev) prev += "</br>"
        return prev += current
    }, "")



    return errors;
}