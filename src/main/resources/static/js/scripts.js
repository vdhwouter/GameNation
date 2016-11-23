$(document).ready(function () {
    'use strict';

    crossroads.addRoute('/', function () {
        window.location += 'login';
    }, 100);

    crossroads.addRoute('/login', function () {
        $('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
        $('body').load('login.html');
    }, 100);

    crossroads.addRoute('/register', function () {
        $('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
        $('body').load('register.html');
    }, 100);

    crossroads.addRoute('/settings/{username}', function (username) {
        axios.get('/users?username=' + username)
            .then(function (data) {
                var user = data.data[0]
                $('head').append($('<link rel="stylesheet" href="css/stylesProfile.css">'));
                $('head').append($('<link rel="stylesheet" href="css/remodal.css">'));
                $('head').append($('<link rel="stylesheet" href="css/remodal-default-theme.css">'));
                $('body').load('settings.html', function () {
                    $('#set')[0].href = username;
                    $('#username')[0].value = user.username;
                    $('#email')[0].value = user.email;
                    $('#firstname')[0].value = user.firstname;
                    $('#lastname')[0].value = user.lastname;
                    $('#teamspeakAddr')[0].value = user.teamspeak;
                    $('#DiscordAddr')[0].value = user.discord;
                    $('#descriptionText')[0].value = user.description;
                    $('#formdata')[0].action += user.id;
                });
            })
    }, 100);

    crossroads.addRoute('/{username}', function (username) {
        axios.get('/users?username=' + username).then(function (data) {
            var user = data.data[0]
            $('head').append($('<link rel="stylesheet" href="css/stylesProfile.css">'))
            $('body').load('profile.html', function () {
                $('#set')[0].href += username;
                $('#usernameVal')[0].innerHTML = user.username;
                $('#emailVal')[0].innerHTML = user.email;
                $('#namelVal')[0].innerHTML = user.firstname + ' ' + user.lastname;
                $('#teamspeakVal')[0].innerHTML = user.teamspeak;
                $('#descriptionVal')[0].innerHTML = user.description;
                $('#discordVal')[0].innerHTML = user.discord;
            });
        })
    }, 1);

    crossroads.parse(document.location.pathname);
});
