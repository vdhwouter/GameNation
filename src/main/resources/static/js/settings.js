$(document).ready(function () {

	/* ===========================================
	 show all games that user added
	 =========================================== */


	loadGames();

	// get id's from all games that user added


	/* ===========================================
	 live search filter games
	 =========================================== */
	$('#live-search-box').on('keyup', function () {
		var searchTerm = $(this).val().toLowerCase();
		console.log(searchTerm);

		$('.add-games-list__item').each(function () {
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

	// what if a user click on a game?
	$('.addGame').click(function () {
		console.log(this);
		$(this).toggleClass('active');

		if (!eleContainsInArray(addedGames, $(this).attr("alt"))) {
			addedGames.push($(this).attr("alt"));
		}
		else {
			var index = addedGames.indexOf($(this).attr("alt"));
			addedGames.splice(index, 1);
		}
	});


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

	/* ===========================================
	 Display all avatars available for selection
	 =========================================== */
	for (i = 1; i <= 60; i++) $('#avatarList').append('<li class="grow avatarListItem"><img id="avatar(' + i + ')" src="img/avatars/avatar(' + i + ').png" alt="Select avatar ' + i + '"/></li>');

	$('.avatarListItem').on('click', function(e){
		$("#previewAvatar").attr("src","img/avatars/" + e.target.id + ".png");
		$("#avatar").val("img/avatars/" + e.target.id + ".png");
		$('[data-remodal-id=ChangeAvatar]').remodal().close();
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

	$('#editGameTitle').text("Edit " + td_list[2]);
	$('#editGame input[type="number"]').val(td_list[1].replace(/[^0-9]/g, ''));
}

// by click on the add button
$('#modelSkilEditEdit').click(function () {
	//toevoegen game aan user
	axios.post('/users/' + session.id + '/games/' + td_list[3], {
		level: document.getElementById('level').value
	}).then(function (result) {
		loadGames();
	});
});

// by click on the delete button
$('#modelSkilEditDelete').click(function () {
	//delete game van user
	axios.delete('/users/' + session.id + '/games/' + td_list[3]).then(function (result) {
		loadGames();
	});
});

/* ===========================================
 Update user info via axios post
 =========================================== */


$('.form--settings').on('submit', function (e) {
	e.preventDefault();

	// Getting all vars from profile editing form
	var firstname= $('#first_name').val();
	var lastname= $('#last_name').val();
	var username= $('#username').val();
	var email= $('#email').val();
	var level= $('#level').val();
	var password= $('#password').val();
	var teamspeak= $('#teamspeak').val();
	var discord= $('#discord').val();
	var description= $('#description').val();
	var userID= $('#user_id').val();
	var avatar= $('#avatar').val();

	// Validate user input
	// Return error array as list items
	var errorArray = HierKanHetTochNietAanLiggen(email, password, username, userID);
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

var HierKanHetTochNietAanLiggen = function(email, password, username, id){
	var securePassword = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,99}$/;
	var validEmail = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var errors = []

	if (!email.match(validEmail)) errors.push('Email should be a valid email');
	// if (!password.match(securePassword)) errors.push('Password should have a minimum length of 6');
	// if (!password.match(securePassword)) errors.push('Password should contain one lower and one uppercase letter');
	// if (!password.match(securePassword)) errors.push('Password should contain one digit');




	axios.get('/users?username=Maes').then(res => { if(res.data[0].password != password) errors.push('Password is incorrect'); })



	axios.get('/users?username=' + username).then(res => { if (res.data.length > 0) $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle" aria-hidden="true"></i><p class="error-list__item__text"> A user with username \'' + username +'\' already exists</p></li>')});

	var parsedErrors = errors.reduce(function (prev, current) {
		if (prev) prev += "</p></li>"
		return prev += current
	}, "")

	return errors;
	/*	 var regexDigit = /^(?=.*\d)$/;	var regexLowercase = /^(?=.*[a-z])$/;	var regexUppercase = /^(?=.*[A-Z])$/;	*/
}








var loadGames = function () {
	axios.get("/users/" + session.id + "/games").then(function (result) {
		var data = result.data

		// create html tage with added games
		var ul = document.getElementsByClassName('games-list')[0];
		$(ul).empty();

		for (key in data) {
			if (data.hasOwnProperty(key)) {
				var value = data[key];

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

		getIdAddedUserGames();
		showAllGamesModal();
	});
}

var getIdAddedUserGames = function() {
	var td_list = [];
	var ul = document.getElementsByClassName('games-list')[0];

	$(ul).children().each(function (i, v) {
		td_list[i] = this.getElementsByTagName('img').alt;
	});
	// remove last item in array
	td_list.splice(-1,1);

	return td_list;
};

var showAllGamesModal = function() {

	var gamesAlreadyExists = getIdAddedUserGames();

	axios.get("/games").then(function (response) {
		var data = response.data

		// create html tage with games
		var ul = document.getElementsByClassName('add-games-list')[0];

		for (key in data) {
			if (data.hasOwnProperty(key)) {
				var value = data[key];

				if (!eleContainsInArray(gamesAlreadyExists, value.id)) {
					var li = document.createElement("li");
					li.className = "add-games-list__item";
					li.setAttribute("data-search-term", value.name.toLowerCase());

					var img = document.createElement("img");
					img.className = 'add-games-list__item--image-large addGame';
					img.setAttribute("src", "img/games/" + value.imageName);
					img.setAttribute("alt", value.id);
					img.setAttribute("title", value.name);

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
};

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
};
