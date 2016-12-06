/* ================================================================================================================
 Start loading added games on page
 =============================================================================================================== */
$(document).ready(function () {
    loadGames();
});



/* ================================================================================================================
 Local variables
 =============================================================================================================== */
 // array with selected games in add game modal (array with game id's)
 var selectedGames = new Array();

 // array with already added games (array with game id's)
 var alreadyAddedGames = new Array();



/* ================================================================================================================
 load all games that user already have
 =============================================================================================================== */
var loadGames = function () {
	axios.get("/users/" + session.id + "/games").then(function (result) {
		var data = result.data

		// clear already added games from user
		alreadyAddedGames = [];

		// create html tage with added games
		var ul = document.getElementsByClassName('games-list')[0];
		$(ul).empty();

		for (key in data) {
			if (data.hasOwnProperty(key)) {
				var value = data[key];

			    // add game that user already added
			    alreadyAddedGames.push(value['game'].id);

				var li = document.createElement("li");
				li.className = 'games-list__item';
				li.setAttribute("data-remodal-target", "EditGameModal");
				li.setAttribute("onclick", "editGame(this)");

				var img = document.createElement("img");
				img.className = 'games-list__item--image-small';
				img.setAttribute("src", "img/games/" + value['game'].imageName);
				img.setAttribute("alt", value['game'].id);
				img.setAttribute("title", value['game'].name);

				var thirdP = document.createElement("p");
				thirdP.innerHTML = value['game'].id;
				thirdP.setAttribute("style", "display: none");

				var firstP = document.createElement("p");
				firstP.className = 'games-list__item--level';
				firstP.innerHTML = value['skill_level'];

				var secondP = document.createElement("p");
				secondP.className = 'games-list__item--text';
				secondP.innerHTML = value['game'].name;

				li.appendChild(img);
				li.appendChild(firstP);
				li.appendChild(secondP);
				li.appendChild(thirdP);
				ul.appendChild(li);
			}
		}

		// add li tag for add a new game
		var li = document.createElement("li");
		li.className = 'games-list__item';

		var a = document.createElement("a");
		a.className = 'games-list__item--link';
		a.setAttribute("data-remodal-target", "AddNewGameModal");

		var i = document.createElement("i");
		i.className = 'games-list__item--icon fa fa-5x fa-plus-circle';
		i.setAttribute('aria-hidden', 'true');
		// i.setAttribute("id", "addGame");

		var p = document.createElement("p");
		p.className = 'games-list__item--text games-list__item--text-small';
		p.innerHTML = "New game";

		a.appendChild(i);
		a.appendChild(p);
		li.appendChild(a);
		ul.appendChild(li);
	});
}



/* ================================================================================================================
 Game Modal
 =============================================================================================================== */
// show all games in game modal
$(document).on('opened', '.remodal-addGames', function () {
    console.log('Add Game Modal is opened');
    // clear array with selected games
    selectedGames = [];

    axios.get("/games").then(function (response) {
        var data = response.data

        // create html tage with games
        var ul = document.getElementsByClassName('add-games-list')[0];
        $(ul).empty();

        for (key in data) {
            if (data.hasOwnProperty(key)) {
                var value = data[key];

                if (!eleContainsInArray(alreadyAddedGames, value.id)) {
                    var li = document.createElement("li");
                    li.className = "add-games-list__item";
                    li.setAttribute("data-search-term", value.name.toLowerCase());

                    var img = document.createElement("img");
                    img.className = 'add-games-list__item--image-large addGame';
                    img.setAttribute("src", "img/games/" + value.imageName);
                    img.setAttribute("alt", value.id);
                    img.setAttribute("title", value.name);
                    img.setAttribute("onclick", "selectGame(this)");

                    var p = document.createElement("p");
                    p.className = 'add-games-list__item--text add-games-list__item--text-large';
                    p.innerHTML = value.name;

                    li.appendChild(img);
                    li.appendChild(p);
                    ul.appendChild(li);
                }
            }
        }
    });
});


/******************************************** live search filter *************************************************/
$('#live-search-box').on('keyup', function () {
    var searchTerm = $(this).val().toLowerCase();

    $('.add-games-list__item').each(function () {
        if ($(this).filter('[data-search-term *= ' + searchTerm + ']').length > 0 || searchTerm.length < 1) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
});


/*********************************************** selected games **************************************************/
// what if a user click on a game?
var selectGame = function (e) {
    $(e).toggleClass('active');

    if (!eleContainsInArray(selectedGames, $(e).attr("alt"))) {
        selectedGames.push($(e).attr("alt"));
    }
    else {
        var index = selectedGames.indexOf($(e).attr("alt"));
        selectedGames.splice(index, 1);
    }
}


/************************************************* add button ****************************************************/
// add games to user profile
$('#addGameConfirm').click(function () {
    if (selectedGames.length == 0) {
        console.log("no items selected");
    }
    else {
        //toevoegen game aan user
        axios.post("/users/" + session.id + "/games", selectedGames, {
        }).then(function (result) {
            loadGames();
        });
    }
});



/* ================================================================================================================
 Edit / Delete Modal
 =============================================================================================================== */
// give values in edit delete modal
var editModalArray = [];
var editGame = function (e) {
	$(e).children().each(function (i, v) {
		if (i >= 0 && i < 5) {
			editModalArray[i] = $(this)[0].innerHTML;
		}
	});

	$('#editGameTitle').text("Edit " + editModalArray[2]);
	$('#editGame input[type="number"]').val(editModalArray[1].replace(/[^0-9]/g, ''));
}


/************************************************* edit button ***************************************************/
$('#modelSkilEditEdit').click(function () {
	//toevoegen game aan user
	axios.post('/users/' + session.id + '/games/' + editModalArray[3], {
		level: document.getElementById('level').value
	}).then(function (result) {
		loadGames();
	});
});


/************************************************ delete button **************************************************/
$('#modelSkilEditDelete').click(function () {
	//delete game van user
	axios.delete('/users/' + session.id + '/games/' + editModalArray[3]).then(function (result) {
		loadGames();
	});
});



/* ================================================================================================================
 Avatar modal
 =============================================================================================================== */
$(document).on('opened', '.remodal-avatars', function () {
    for (i = 1; i <= 60; i++) $('#avatarList').append('<li class="grow avatarListItem"><img id="avatar(' + i + ')" src="img/avatars/avatar(' + i + ').png" alt="Select avatar ' + i + '"/></li>');

	$('.avatarListItem').on('click', function(e){
		$("#previewAvatar").attr("src","img/avatars/" + e.target.id + ".png");
		$("#avatar").val("img/avatars/" + e.target.id + ".png");
		$('[data-remodal-id=ChangeAvatar]').remodal().close();
	});
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



/* ================================================================================================================
 Update user info
 =============================================================================================================== */
$('.form--settings').on('submit', function (e) {
	e.preventDefault();

	// Getting all vars from profile editing form
	var firstname= $('#first_name').val();
	var lastname= $('#last_name').val();
	var username= $('#username').val();
	var email= $('#email').val();
	var level= $('#level').val();
	var password= ($('#password').val() === '') ? null : $('#password').val();
	var teamspeak= $('#teamspeak').val();
	var discord= $('#discord').val();
	var description= $('#description').val();
	var userID= $('#user_id').val();
	var avatar= $('#avatar').val();

	// Validate user input
	// Return error array as list items
	var errorArray = formchecking(email, password, username, userID);
	$('.error-list').empty();
	$(errorArray).each(function(index, value){ $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle" aria-hidden="true"></i><p class="error-list__item__text">' + value + '</p></li>') });
	$('.error-list').slideDown();

	// Post user data to db
	if (!$('.error-list')[0].innerHTML) {
		axios.post('/users/' + userID, { firstname: firstname, lastname: lastname, username: username, email: email, password: password, teamspeak: teamspeak, discord: discord, description: description, level: level, avatar: avatar })
			.then((res) => {
				console.info("User edited succesfully, redirecting to profile page")
				navigateTo(username, username);
			}).catch((err) => {
				console.log("Error when updating user:" + err)
		});
	} else {
		console.log("From validation not passed!")
	}
});


/************************************************ formchecking ***************************************************/
var formchecking = function(email, password, username, id){
	var securePassword = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,99}$/;
	var validEmail = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var errors = []

	if (!email.match(validEmail)) errors.push('Email should be a valid email');

	axios.get('/users?username=' + username).then(res => { if (res.data.length > 0) $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle" aria-hidden="true"></i><p class="error-list__item__text"> A user with username \'' + username +'\' already exists</p></li>')});

	var parsedErrors = errors.reduce(function (prev, current) {
		if (prev) prev += "</p></li>"
		return prev += current
	}, "")

	return errors;
}







