$(document).ready(function() {
	'use strict';

	crossroads.addRoute('/', function() {
		$('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
		$('body').load('login.html');
	}, 100);

	crossroads.addRoute('/login', function() {
		$('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
		$('body').load('login.html');
	}, 100);

	crossroads.addRoute('/register', function() {
		$('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
		$('body').load('register.html');
	}, 100);

	crossroads.addRoute('/settings/{username}', function(username) {
		$('head').append($('<link rel="stylesheet" href="css/stylesProfile.css">'))
		$('head').append($('<link rel="stylesheet" href="css/remodal.css">'))
		$('head').append($('<link rel="stylesheet" href="css/remodal-default-theme.css">'))
		$('body').load('settings.html', function() {
			$.ajax({
				url: 'http://localhost:8080/api/users/' + username,
			}).done(function(data) {
				$('#set')[0].href = username;
				$('#username')[0].value = data.username;
				$('#email')[0].value = data.email;
				$('#firstname')[0].value = data.firstname;
				$('#lastname')[0].value = data.lastname;
				$('#teamspeakAddr')[0].value = data.teamspeak;
				$('#DiscordAddr')[0].value = data.discord;
				$('#descriptionText')[0].value = data.description;
			});
		});
	}, 100);

	crossroads.addRoute('/{username}', function(username) {
		$('head').append($('<link rel="stylesheet" href="css/stylesProfile.css">'))
		$('body').load('profile.html', function() {
			$.ajax({
				url: 'http://localhost:8080/api/users/' + username,
			}).done(function(data) {
				$('#set')[0].href += username;
				$('#usernameVal')[0].innerHTML = data.username;
				$('#emailVal')[0].innerHTML = data.email;
				$('#namelVal')[0].innerHTML = data.firstname + ' ' + data.lastname;
				$('#teamspeakVal')[0].innerHTML = data.teamspeak;
				$('#descriptionVal')[0].innerHTML = data.description;
				$('#discordVal')[0].innerHTML = data.discord;
			});
		});
	}, 1);

	crossroads.parse(document.location.pathname.replace('/GameNation/web', ''));
});

